/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_If_fin extends Node {

    private Node_Elements cosElse; // pot ser null

    public Node_If_fin(Node_Elements cosElse) {
        super("If_fin");
        this.cosElse = cosElse;
    }

    public Node_Elements getCosElse() {
        return cosElse;
    }
    
    public boolean teElse() {
        return cosElse != null;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (cosElse != null) {
            // Àmbit per al bloc ELSE
            TaulaSimbols.entrarBloc();
            cosElse.gestioSemantica(ts);
            TaulaSimbols.sortirBloc();
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        if (cosElse != null) {
            cosElse.generaCodi3a(codi3a);
        }
        return null;
    }
    
    

    @Override
    public String toString() {
        return "If_fin(" + cosElse + ")";
    }
}
