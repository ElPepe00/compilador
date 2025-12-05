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
public class Node_ParamList extends Node {

    private Node_ParamList anteriors;
    private Node_Param param;

    public Node_ParamList(Node_ParamList anteriors, Node_Param param) {
        super("ParamList");
        this.anteriors = anteriors;
        this.param = param;
    }

    /**
     * Dona d'alta tots els paràmetres a la TS i els afegeix a la signatura
     * de la funció.
     * @param ts
     * @param funcSym
     */
    public void registrarParametres(TaulaSimbols ts, Simbol funcSym) {
        if (anteriors != null) {
            anteriors.registrarParametres(ts, funcSym);
        }
        if (param != null) {
            param.registrarComParametre(ts, funcSym);
        }
    }

    @Override
    public String toString() {
        return "ParamList(" + anteriors + ", " + param + ")";
    }
}
