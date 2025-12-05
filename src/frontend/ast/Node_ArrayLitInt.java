/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_ArrayLitInt extends Node {

    private Node_IntElemsOpt elemsOpt;
    
    public Node_ArrayLitInt(Node_IntElemsOpt elemsOpt) {
        super("ArrayLitInt");
        this.elemsOpt = elemsOpt;
    }
    
    public int comptarElements() {
        
        if (elemsOpt == null) {
            return 0;
        } else {
            return elemsOpt.comptarElements();
        }
    }
    
}
