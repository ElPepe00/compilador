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
public class Node_ArrayLitInt extends Node {

    private Node_IntElems elems;
    
    public Node_ArrayLitInt(Node_IntElems elems) {
        super("ArrayLitInt");
        this.elems = elems;
    }
    
    public int comptarElements() {
        
        if (elems == null) {
            return 0;
        } else {
            return elems.comptarElements();
        }
    }
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (elems != null) elems.gestioSemantica(ts);
    }

    // Generacio de codi
    public void generaCodiInicialitzacio(C3a codi3a, String nomArrayBase) {
        if (elems != null) {
            // Començam a omplir des de l'índex 0
            elems.generaCodiElements(codi3a, nomArrayBase, 0);
        }
    }
    
}
