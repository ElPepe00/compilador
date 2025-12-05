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
public class Node_IntElemsOpt extends Node {

    private Node_IntElems elems;
    
    public Node_IntElemsOpt(Node_IntElems elems) {
        super("IntElemsOpt");
        this.elems = elems;
    }
    
    public int comptarElements() {
        return (elems == null ? 0 : elems.comptarElements());
    }
    
}
