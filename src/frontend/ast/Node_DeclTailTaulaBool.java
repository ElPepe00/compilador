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
public class Node_DeclTailTaulaBool extends Node {

    private Node_ArrayLitBool arrayLit;

    public Node_DeclTailTaulaBool(Node_ArrayLitBool arrayLit) {
        super("DeclTailTaulaBool");
        this.arrayLit = arrayLit;
    }

    public void gestioSemantica(TaulaSimbols ts, int midaEsperada) {
        if (arrayLit == null) return;
        int nElems = arrayLit.comptarElements();
        if (nElems != midaEsperada) {
            throw new RuntimeException("Inicialització de taula BOOL amb " + nElems +
                    " elements, però s'esperaven " + midaEsperada);
        }
    }
    
}
