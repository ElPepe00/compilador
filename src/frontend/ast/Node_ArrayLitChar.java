/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import frontend.taula_simbols.*;
import backend.codi_intermedi.*;
/**
 *
 * @author josep
 */
public class Node_ArrayLitChar extends Node {

    private Node_CharElems elems;

    public Node_ArrayLitChar(Node_CharElems elems) {
        super("ArrayLitChar");
        this.elems = this.elems;
    }

    public int comptarElements() {
        if (elems == null) return 0;
        return elems.comptarElements();
    }    
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (elems != null) elems.gestioSemantica(ts);
    }

    public void generaCodiInicialitzacio(C3a codi3a, String nomArrayBase) {
        if (elems != null) {
            elems.generaCodiElements(codi3a, nomArrayBase, 0);
        }
    }
}
