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
        // 1) Tipus de retorn
        TipusSimbol tRetorn = (returnv != null ? returnv.getSymbolType() : TipusSimbol.VOID);

        // 2) Categoria funció/procediment
        CategoriaSimbol cat = (tRetorn == TipusSimbol.VOID ? CategoriaSimbol.PROCEDIMENT : CategoriaSimbol.FUNCIO);

        // 3) Donar d'alta la funció al nivell global
        Simbol funcSym = new Simbol(id, tRetorn, cat, 0, 0);
        funcSym.setGlobal(true);
        funcSym.setEtiqueta("f_" + id);

        TaulaSimbols.inserirSimbol(funcSym);

        // 4) Entrar àmbit nou per al cos de la funció
        TaulaSimbols.entrarSubAmbit();

        // 5) Donar d'alta paràmetres (com a variables/params locals)
        if (paramsOpt != null) {
            paramsOpt.registrarParametres(ts, funcSym);
        }

        // 6) Analitzar semàntica de declaracions i instruccions del cos
        if (elements != null) {
            elements.gestioSemantica(ts);
        }

        // 7) Comprovar RETURN coherent
        if (returnfi != null) {
            returnfi.gestioSemantica(ts, tRetorn);
            
        } else if (tRetorn != TipusSimbol.VOID) {
            // funció amb tipus ha de tenir RETURN
            throw new RuntimeException("La funció '" + id + "' ha de tenir una instrucció RETURN");
        }

        // 8) Sortir de l'àmbit de la funció
        TaulaSimbols.sortirSubAmbit();
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
