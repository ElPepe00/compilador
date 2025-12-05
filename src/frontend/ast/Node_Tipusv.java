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

    private  String nomTipus;   // int, char, bool
    
    public Node_Tipusv(String nomTipus) {
        super("Tipusv");
        this.nomTipus = nomTipus;
    }
    
    // Mètode que retorna el TipusSimbol corresponent al text
    public TipusSimbol getTipusSimbol() {

        switch (nomTipus) {
            case "INT": return TipusSimbol.INT;
            case "CARACTER": return TipusSimbol.CARACTER;
            case "BOOL": return TipusSimbol.BOOL;
            default:
                throw new IllegalStateException("Tipus desconegut a Node_Tipusv: " + nomTipus);
        }
    }
    
    public int getMidaBytes() {
        switch (nomTipus) {
            case "INT": return 4;
            case "CARACTER": return 1;
            case "BOOL": return 1;
            default:
                throw new IllegalStateException("Tipus desconegut a Node_Tipusv: " + nomTipus);
        }
    }

    public String getNomTipus() {
        return nomTipus;
    }

    @Override
    public String toString() {
        return "Node_Tipusv(" + nomTipus + ")";
    }
}
