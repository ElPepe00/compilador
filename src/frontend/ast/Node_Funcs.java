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
public class Node_Funcs extends Node {

    private Node_Funcs anterior;
    private Node_Func func;

    public Node_Funcs(Node_Funcs anterior, Node_Func func) {
        super("Funcs");
        this.anterior = anterior;
        this.func = func;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        if (anterior != null) {
            anterior.gestioSemantica(ts);
        }
        
        if (func != null) {
            func.gestioSemantica(ts);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        if (anterior != null) {
            anterior.generaCodi3a(codi3a);
        }
        
        if (func != null) {
            func.generaCodi3a(codi3a);
        }
        
        return null;
    }
    
    

    @Override
    public String toString() {
        return "Node_Funcs(" + anterior + ", " + func + ')';
    }
}

