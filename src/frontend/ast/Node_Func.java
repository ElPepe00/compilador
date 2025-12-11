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

/**
 *
 * @author josep
 */
public class Node_Func extends Node {

    private Node_Returnv returnv;
    private String id;
    private Node_ParamsOpt paramsOpt;
    private Node_Elements elements;
    private Node_Returnfi returnfi;
    
    private Simbol simbolFuncio; //permet guardar el simbol que de la funcio

    public Node_Func(Node_Returnv returnv, String id, Node_ParamsOpt paramsOpt, Node_Elements elements, Node_Returnfi returnfi) {
        super("Func");
        this.returnv = returnv;
        this.id = id;
        this.paramsOpt = paramsOpt;
        this.elements = elements;
        this.returnfi = returnfi;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // 1. Tipus de retorn
        TipusSimbol tRetorn = (returnv != null ? returnv.getSymbolType() : TipusSimbol.VOID);

        // 2. Categoria funció/procediment
        CategoriaSimbol cat = (tRetorn == TipusSimbol.VOID ? CategoriaSimbol.PROCEDIMENT : CategoriaSimbol.FUNCIO);

        // 3. Crear simbol de la funcio global
        this.simbolFuncio = new Simbol(id, tRetorn, cat);
        this.simbolFuncio.setEtiqueta(id);

        // afegim el simbol de funcio a la TS
        if (!ts.afegirSimbol(this.simbolFuncio)) {
            errorSemantic("Funcio ja declarada: " + id);
        }
        
        // 4. Entrar àmbit nou per al cos de la funció
        ts.entrarBloc(simbolFuncio);

        // 5. Registrar parametres
        if (paramsOpt != null) {
            paramsOpt.gestioSemantica(ts, this.simbolFuncio);
        }

        // 5. Comprovam si hiha elements
        if (elements != null) {
            elements.gestioSemantica(ts);
        }

        // 6. Guardam la mida del frame
        int midaTotal = ts.getOffsetActual();
        this.simbolFuncio.setMidaFrame(midaTotal);
        
        // 7. Comprovam si te return
        if (returnfi != null) {
            returnfi.gestioSemantica(ts, tRetorn);
            
        } else if (tRetorn != TipusSimbol.VOID) {
            // funció amb tipus ha de tenir RETURN
            errorSemantic("La funció '" + id + "' ha de tenir una instrucció RETURN");
        }

        // 8) Sortir de l'àmbit de la funció
        ts.sortirBloc();
    }

    @Override
    public String generaCodi3a(C3a codi3a) {

        String etiqueta = this.simbolFuncio.getEtiqueta();
        String midaFrame = String.valueOf(this.simbolFuncio.getMidaFrame());
        
        codi3a.afegirEtiqueta(etiqueta);
        codi3a.afegir(Codi.PMB, etiqueta, midaFrame, null);
        
        // Cos de la funcio
        if (elements != null) {
            elements.generaCodi3a(codi3a);
        }
        
        // Retorn implicit
        if (returnfi != null) {
            returnfi.generaCodi3a(codi3a);
            
        } else {
            codi3a.afegir(Codi.RET, null, null, null);
        }
        
        return null;
    }
    

    @Override
    public String toString() {
        return "Func(" + id + ")";
    }
}
