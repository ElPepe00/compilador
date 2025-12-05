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
public class Node_BoolElemsOpt extends Node {

    private Node_BoolElems elems;

    public Node_BoolElemsOpt(Node_BoolElems elems) {
        super("BoolElemsOpt");
        this.elems = elems;
    }

    public int comptarElements() {
        return (elems == null ? 0 : elems.comptarElements());
    }
    
}
