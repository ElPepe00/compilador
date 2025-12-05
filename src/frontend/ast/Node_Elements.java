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
public class Node_Elements extends Node {

    private Node_Elements anterior;
    private Node_Element elem;
    
    public Node_Elements(Node_Elements anterior, Node_Element elem) {
        super("Elements");
        this.anterior = anterior;
        this.elem = elem;
    }
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        if (anterior != null) {
            anterior.gestioSemantica(ts);
        }
        
        if (elem != null) {
            elem.gestioSemantica(ts);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        if (anterior != null) {
            anterior.generaCodi3a(codi3a);
        }
        
        if (elem != null) {
            elem.generaCodi3a(codi3a);
        }
        
        return null;
    }
    
    

    @Override
    public String toString() {
        return "Node_Elements(" + anterior + ", " + elem + ')';
    }
}
