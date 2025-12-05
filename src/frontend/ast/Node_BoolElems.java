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
public class Node_BoolElems extends Node {

    private Node_BoolElems anterior;
    private Node_BoolLit lit;

    public Node_BoolElems(Node_BoolElems anterior, Node_BoolLit lit) {
        super("BoolElems");
        this.anterior = anterior;
        this.lit = lit;
    }

    public int comptarElements() {
        return (anterior == null ? 1 : anterior.comptarElements() + 1);
    }
    
}
