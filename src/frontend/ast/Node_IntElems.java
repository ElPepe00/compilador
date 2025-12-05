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
public class Node_IntElems extends Node {

    private Node_IntElems anterior;
    private Node_Num num;
    
    public Node_IntElems(Node_IntElems anterior, Node_Num num) {
        super("IntElems");
        this.anterior = anterior;
        this.num = num;
    }
    
    public int comptarElements() {
        return (anterior == null ? 1 : anterior.comptarElements() + 1);
    }
    
}
