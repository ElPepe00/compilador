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
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (anterior != null) anterior.gestioSemantica(ts);
    }

    // Generacio de codi dels elements
    public int generaCodiElements(C3a codi3a, String nomArrayBase, int indexActual) {
        
        if (anterior != null) {
            indexActual = anterior.generaCodiElements(codi3a, nomArrayBase, indexActual);
        }
        
        // charLit.generaCodi3a normalment retorna el valor numèric ASCII o el caràcter entre cometes
        String valor = lit.generaCodi3a(codi3a);
        
        int offsetBytes = indexActual * TipusSimbol.CHAR.getMidaBytes();
        String index = String.valueOf(offsetBytes);
        
        codi3a.afegir(Codi.IND_ASS, valor, index, nomArrayBase);
        
        return indexActual + 1;
    }
}
