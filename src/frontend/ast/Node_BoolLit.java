/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import backend.codi_intermedi.Codi;
import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_BoolLit extends Node {

    private boolean valor;
    
    public Node_BoolLit(String val) {
        super("BoolLit");
        switch (val) {
            case "true": this.valor = true; break;
            case "false": this.valor = false; break;
            default:
                errorSemantic("Valor boolea invalid al parser: " + val);
        }
    }
    
    public boolean getValor() {
        return valor;
    }
    
    public TipusSimbol getTipusSimbol() {
        return TipusSimbol.BOOL;
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        String t = codi3a.novaTemp();
        String v = this.valor ? "-1" : "0";
        codi3a.afegir(Codi.COPY, v, null, t);
        return t;
    }

    
    @Override
    public String toString() {
        return "Node_BoolLit(" + valor + ")";
    }
}
