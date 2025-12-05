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
public class Node_CharElems extends Node {
    
    private Node_CharElems anterior;
    private Node_CharLit lit;

    public Node_CharElems(Node_CharElems anterior, Node_CharLit lit) {
        super("CharElems");
        this.anterior = anterior;
        this.lit = lit;
    }

    public int comptarElements() {
        return (anterior == null ? 1 : anterior.comptarElements() + 1);
    }
}
