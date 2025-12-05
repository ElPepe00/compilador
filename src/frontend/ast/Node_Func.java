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
        Simbol funcSym = new Simbol(id, tRetorn, cat);
        funcSym.setEtiqueta("f_" + id);

        // afegim el simbol de funcio a la TS
        ts.afegirSimbol(funcSym);

        // 4. Entrar àmbit nou per al cos de la funció
        ts.entrarFuncio();

        // 5. Registrar parametres
        if (paramsOpt != null) {
            paramsOpt.registrarParametres(ts, funcSym);
        }

        // 5. Comprovam si hiha elements
        if (elements != null) {
            elements.gestioSemantica(ts);
        }

        // 6. Guardam la mida del frame
        int midaFrameTotal = ts.getOffsetActual();
        funcSym.setMidaFrame(midaFrameTotal);
        
        // 7. Comprovam si te return
        if (returnfi != null) {
            returnfi.gestioSemantica(ts, tRetorn);
            
        } else if (tRetorn != TipusSimbol.VOID) {
            // funció amb tipus ha de tenir RETURN
            throw new RuntimeException("La funció '" + id + "' ha de tenir una instrucció RETURN");
        }

        // 8) Sortir de l'àmbit de la funció
        ts.sortirBloc();
    }

    @Override
    public String generaCodi3a(C3a codi3a) {

        codi3a.afegirEtiqueta(id);
        codi3a.afegir(Codi.PMB, id, null, null);
        
        // Cos de la funcio
        if (elements != null) {
            elements.generaCodi3a(codi3a);
        }
        
        // Retorn explicit (si hi es)
        if (returnfi != null) {
            returnfi.generaCodi3a(codi3a);
            
        }
        
        codi3a.afegir(Codi.RET, id, null, null);
        return null;
    }
    

    @Override
    public String toString() {
        return "Func(" + id + ")";
    }
}
