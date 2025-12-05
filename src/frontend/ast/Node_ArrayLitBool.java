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
public class Node_ArrayLitBool extends Node {

   private Node_BoolElemsOpt elemsOpt;

    public Node_ArrayLitBool(Node_BoolElemsOpt elemsOpt) {
        super("ArrayLitBool");
        this.elemsOpt = elemsOpt;
    }

    public int comptarElements() {
        if (elemsOpt == null) return 0;
        return elemsOpt.comptarElements();
    }
    
}
