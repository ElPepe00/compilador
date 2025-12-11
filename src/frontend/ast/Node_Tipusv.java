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
public class Node_Tipusv extends Node {

    // Variable que emmagatzema el tipus de simbol passat del Parser
    private TipusSimbol tipus;   // int, char, bool
    
    /**
     * Constructor de Tipusv
     * @param tipus tipus passat del Parser
     */
    public Node_Tipusv(TipusSimbol tipus) {
        super("Tipusv");
        this.tipus = tipus;
    }
    
    /**
     * Mètode que retorna el TipusSimbol corresponent, sino existeix error
     */
    public TipusSimbol getTipusSimbol() {

        if (tipus != TipusSimbol.INT
            && tipus != TipusSimbol.CHAR
            && tipus != TipusSimbol.BOOL) {
            
            errorSemantic("Tipus desconegut a Node_Tipusv: " + tipus);
            
            return TipusSimbol.ERROR;
        }
        return tipus;
    }
    
    /**
     * Mètode que retorna el nom del tipus de simbol
     * @return retorna el name() del tipus de simbol
     */
    public String getNomTipus() {
        return tipus.name();
    }

    @Override
    public String toString() {
        return "Node_Tipusv(" + tipus.name() + ")";
    }
}
