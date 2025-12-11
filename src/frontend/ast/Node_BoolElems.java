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
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (anterior != null) anterior.gestioSemantica(ts);
        // Node_BoolLit ja es verifica a si mateix
    }

    // Generacio de codi dels elements
    public int generaCodiElements(C3a codi3a, String nomArrayBase, int indexActual) {
        
        // 1. Processam els anteriors
        if (anterior != null) {
            indexActual = anterior.generaCodiElements(codi3a, nomArrayBase, indexActual);
        }
        
        // 2. Generam codi per al literal actual
        // boolLit.generaCodi3a retorna un temporal o constant (ex: "-1" o "0")
        String valor = lit.generaCodi3a(codi3a);
        
        int offsetBytes = indexActual * TipusSimbol.BOOL.getMidaBytes();
        String index = String.valueOf(offsetBytes);
        
        // 3. Assignació: array[index] = valor
        codi3a.afegir(Codi.IND_ASS, valor, index, nomArrayBase);
        
        // 4. Retornam el següent índex
        return indexActual + 1;
    }
}
