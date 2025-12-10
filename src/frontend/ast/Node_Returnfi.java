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
public class Node_Returnfi extends Node {

    private Node_Express expr; // pot ser null

    public Node_Returnfi(Node_Express expr) {
        super("Returnfi");
        this.expr = expr;
    }

    public Node_Express getExpr() {
        return expr;
    }

    /**
     * Comprova que el RETURN sigui compatible amb el tipus de retorn
     * esperat de la funció.
     *
     * @param ts Taula de símbols
     * @param tipusEsperat tipus de retorn de la funció (VOID si no en té)
     */
    public void gestioSemantica(TaulaSimbols ts, TipusSimbol tipusEsperat) {
        if (tipusEsperat == TipusSimbol.VOID) {
            // procediment
            if (expr != null) {
                errorSemantic("No es pot retornar un valor en un procediment (retorn void)");
            }
        } else {
            // funció amb tipus
            if (expr == null) {
                errorSemantic("La funció ha de retornar un valor de tipus " + tipusEsperat);
            }
            TipusSimbol tExpr = expr.getTipusSimbol(ts);
            if (tExpr != tipusEsperat) {
                errorSemantic("El tipus del RETURN (" + tExpr +
                        ") no coincideix amb el tipus de retorn (" + tipusEsperat + ")");
            }
        }
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // No sabem el tipus esperat aquí; s'ha de cridar la versió amb tipus
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        if (expr == null) {
            codi3a.afegir(Codi.RET, null, null, null);
            
        } else {
            String t = expr.generaCodi3a(codi3a);
            codi3a.afegir(Codi.RET, t, null, null); //return t
        }
        return null;
    }

    
    @Override
    public String toString() {
        return "Returnfi(" + expr + ")";
    }
    
}
