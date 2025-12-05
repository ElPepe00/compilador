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
public class Node_CharLit extends Node {

    private char valor;
    
    public Node_CharLit(Character c) {
        super("CharLit");
        this.valor = c;
    }

    public char getValor() {
        return valor;
    }

    public TipusSimbol getTipusSimbol() {
        return TipusSimbol.CARACTER;
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        String t = codi3a.novaTemp();
        int code = (int) this.valor;
        codi3a.afegir(Codi.COPY, Integer.toString(code), null, t);
        return t;
    }
    
    
    @Override
    public String toString() {
        return "Node_CharLit(" + valor + ")";
    }
}
