/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.Codi;
import backend.codi_intermedi.C3a;
import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_Num extends Node {

    private String lexema; //el text tal qual ("123", "3", ...)
    
    public Node_Num(String lexema) {
        super("Num");
        this.lexema = lexema;
    }

    public String getLexema() {
        return lexema;
    }
    
    public int getValorEnter() {
        return Integer.parseInt(lexema);
    }
    
    public TipusSimbol getTipusSimbol() {
        return TipusSimbol.INT;
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        String t = codi3a.novaTemp();
        codi3a.afegir(Codi.COPY, lexema, null, t);
        return t;
    }

    @Override
    public String toString() {
        return "Node_Nom(" + lexema + ")";
    }
    
}
