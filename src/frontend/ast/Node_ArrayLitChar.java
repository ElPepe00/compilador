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
public class Node_ArrayLitChar extends Node {

    private Node_CharElemsOpt elemsOpt;

    public Node_ArrayLitChar(Node_CharElemsOpt elemsOpt) {
        super("ArrayLitChar");
        this.elemsOpt = elemsOpt;
    }

    public int comptarElements() {
        if (elemsOpt == null) return 0;
        return elemsOpt.comptarElements();
    }    
}
