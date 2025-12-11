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
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (anterior != null) anterior.gestioSemantica(ts);
        // Node_Num ja és INT per definició, no cal check extra
    }

    // Generacio de codi
    // Retorna l'índex següent disponible
    public int generaCodiElements(C3a codi3a, String nomArrayBase, int indexActual) {
        
        // 1. Primer processam els elements anteriors
        if (anterior != null) {
            // L'anterior actualitzarà l'índex fins on arribi
            indexActual = anterior.generaCodiElements(codi3a, nomArrayBase, indexActual);
        }
        
        // 2. Generam el codi per a AQUEST element
        String valor = num.generaCodi3a(codi3a); // ex: "2"
        
        int offsetBytes = indexActual * TipusSimbol.INT.getMidaBytes();
        String index = String.valueOf(offsetBytes);
        
        // Instrucció: array[index] = valor
        codi3a.afegir(Codi.IND_ASS, valor, index, nomArrayBase);
        
        // 3. Retornam l'índex incrementat per al següent (si n'hi ha)
        return indexActual + 1;
    }
}
