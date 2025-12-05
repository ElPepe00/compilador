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
public class Node_CharElemsOpt extends Node {

    private Node_CharElems elems;

    public Node_CharElemsOpt(Node_CharElems elems) {
        super("CharElemsOpt");
        this.elems = elems;
    }

    public int comptarElements() {
        return (elems == null ? 0 : elems.comptarElements());
    }
}
