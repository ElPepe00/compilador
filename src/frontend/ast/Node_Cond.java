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
 * Node per a condicions (a if, while i do-while)
 * @author josep
 */
public class Node_Cond extends Node {

    private Node_Express expr;
    
    public Node_Cond(Node_Express e) {
        super("Cond");
        this.expr = e;
    }
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        TipusSimbol t = expr.getTipusSimbol(ts);
        
        if (t != TipusSimbol.BOOL) {
            throw new RuntimeException("La condicio no és booleana, s'ha trobat: " + t);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        return expr.generaCodi3a(codi3a);
    }

    @Override
    public String toString() {
        return "Node_Cond(" + expr + ')';
    }
}
