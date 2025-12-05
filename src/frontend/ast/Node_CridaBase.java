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
        Simbol s = TaulaSimbols.cercarSimbol(id);
        if (s == null) {
            throw new RuntimeException("Crida a subprograma no declarat: " + id);
        }

        CategoriaSimbol cat = s.getCategoria();

        if (comFuncio) {
            // Crida com a funció: ha de ser FUNCIO
            if (cat != CategoriaSimbol.FUNCIO) {
                throw new RuntimeException("El subprograma '" + id +
                        "' no és una funció i no es pot usar en una expressió");
            }
        } else {
            // Crida com a procediment: millor només PROCEDIMENT
            if (cat != CategoriaSimbol.PROCEDIMENT) {
                throw new RuntimeException("El subprograma '" + id +
                        "' no és un procediment (retorna valor) i no es pot usar com a instrucció");
            }
        }

        // Tipus dels paràmetres formals
        List<TipusSimbol> formals = s.getArguments();

        // Tipus dels paràmetres reals
        List<TipusSimbol> reals = (argsOpt != null ? argsOpt.getTipusArguments(ts) : java.util.Collections.emptyList());

        if (formals.size() != reals.size()) {
            throw new RuntimeException("Nombre de paràmetres incorrecte a la crida de '" + id +
                    "': esperats " + formals.size() + ", rebuts " + reals.size());
        }

        for (int i = 0; i < formals.size(); i++) {
            TipusSimbol tf = formals.get(i);
            TipusSimbol tr = reals.get(i);
            if (tf != tr) {
                throw new RuntimeException("Tipus de paràmetre incompatible a '" + id +
                        "' posició " + (i + 1) + ": esperat " + tf + ", trobat " + tr);
            }
        }

        // Si tot és correcte, retornam el tipus de retorn del subprograma
        return s.getTipus();
    }
    
    
    public String generaCodiCridaFunc(C3a codi3a) {
        
        // PARAM per als arguments
        if (argsOpt != null) {
            argsOpt.generaCodiParams(codi3a);
        }
        
        // Crida de funcio: el resultat va a un temporal
        String t = codi3a.novaTemp();
        codi3a.afegir(Codi.CALL, id, null, t);
        return t;
    }

    public void generaCodiCridaProc(C3a codi3a) {
        if (argsOpt != null) {
            argsOpt.generaCodiParams(codi3a);
        }
        codi3a.afegir(Codi.CALL, id, null, null);
    }
    @Override
    public String toString() {
        return "CridaBase(" + id + "(" + argsOpt + "))";
    }
}
