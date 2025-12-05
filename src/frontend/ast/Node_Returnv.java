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
public class Node_Returnv extends Node {

    private Node_Tipusv tipusRetorn; // pot ser null → VOID

    public Node_Returnv(Node_Tipusv tipusRetorn) {
        super("Returnv");
        this.tipusRetorn = tipusRetorn;
    }

    /**
     * Retorna el SymbolType del retorn, o VOID si no hi ha tipus (procediment).
     */
    public TipusSimbol getSymbolType() {
        if (tipusRetorn == null) {
            return TipusSimbol.VOID;
        }
        return tipusRetorn.getTipusSimbol();
    }

    @Override
    public String toString() {
        return "Node_Returnv(" + (tipusRetorn != null ? tipusRetorn : "void") + ")";
    }
}
