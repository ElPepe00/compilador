/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import backend.codi_intermedi.Codi;
import frontend.taula_simbols.*;
import java.util.List;

/**
 *
 * @author josep
 */
public class Node_CridaBase extends Node {

    private String id;
    private Node_ArgsOpt argsOpt; // pot ser null
    
    private Simbol simbolFuncio;

    public Node_CridaBase(String id, Node_ArgsOpt argsOpt) {
        super("CridaBase");
        this.id = id;
        this.argsOpt = argsOpt;
    }

    public String getId() {
        return id;
    }

    public Node_ArgsOpt getArgsOpt() {
        return argsOpt;
    }

    /**
     * Comprova la crida a un subprograma (funció/procediment).
     *
     * @param ts Taula de símbols
     * @param comFuncio true si esperam valor de retorn (Crida_func),
     *                  false si és Crida_proc
     * @return tipus de retorn del subprograma (VOID per procediment)
     */
    public TipusSimbol comprovarCrida(TaulaSimbols ts, boolean comFuncio) {
        
        // 1. Cercar la funcio (com que sempre son globals, sempre estan visibles)
        Simbol s = ts.cercarSimbol(id);
        
        if (s == null) {
            errorSemantic("Crida a subprograma no declarat: " + id);
            return TipusSimbol.ERROR;
        }

        // 2. Guardam el simbol
        this.simbolFuncio = s;
        CategoriaSimbol cat = s.getCategoria();

        if (comFuncio) {
            // Crida com a funció: ha de ser FUNCIO
            if (cat != CategoriaSimbol.FUNCIO) {
                errorSemantic("El subprograma '" + id + "' no és una funcio.");
                return TipusSimbol.ERROR;
            }
        } else {
            // Crida com a procediment: millor només PROCEDIMENT
            if (cat != CategoriaSimbol.PROCEDIMENT) {
                errorSemantic("El subprograma '" + id + "' no és un procediment (retorna valor).");
                return TipusSimbol.ERROR;
            }
        }

        // 3. Comprovar arguments
        // Tipus dels paràmetres formals
        List<TipusSimbol> formals = s.getArguments();

        // Tipus dels paràmetres reals
        List<TipusSimbol> reals = (argsOpt != null ? argsOpt.getTipusArguments(ts) : java.util.Collections.emptyList());

        if (formals.size() != reals.size()) {
            errorSemantic("Nombre de paràmetres incorrecte a la crida de '" + id +
                    "': esperats " + formals.size() + ", rebuts " + reals.size());
            return TipusSimbol.ERROR;
        }

        for (int i = 0; i < formals.size(); i++) {
            if (formals.get(i) != reals.get(i)) {
                errorSemantic("Tipus de paràmetre incompatible a '" + id +
                        "' posició " + (i + 1) + ": esperat " + formals.get(i) 
                        + ", trobat " + reals.get(i));
                return TipusSimbol.ERROR;
            }
        }

        // Si tot és correcte, retornam el tipus de retorn del subprograma
        return s.getTipus();
    }
    
    
    public String generaCodiCridaFunc(C3a codi3a) {
        
        generarParams(codi3a);
        
        String tResultat = codi3a.novaTemp();
        
        // Usam l'etiqueta real del símbol (ex: f_suma)
        String etiquetaFuncio = (simbolFuncio != null && simbolFuncio.getEtiqueta() != null) 
                                ? simbolFuncio.getEtiqueta() : id;
                                
        codi3a.afegir(Codi.CALL, etiquetaFuncio, null, tResultat);
        return tResultat;
    }

    public void generaCodiCridaProc(C3a codi3a) {
        
        generarParams(codi3a);
        
        String etiquetaFuncio = (simbolFuncio != null && simbolFuncio.getEtiqueta() != null) 
                                ? simbolFuncio.getEtiqueta() : id;

        codi3a.afegir(Codi.CALL, etiquetaFuncio, null, null);
    }
    
    private void generarParams(C3a codi3a) {
        if (argsOpt != null) {
            argsOpt.generaCodiParams(codi3a);
        }
    }
    
    @Override
    public String toString() {
        return "CridaBase(" + id + "(" + argsOpt + "))";
    }
}
