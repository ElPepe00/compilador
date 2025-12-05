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
public class Node_ParamsOpt extends Node {

    private Node_ParamList paramList; // pot ser null

    public Node_ParamsOpt(Node_ParamList paramList) {
        super("ParamsOpt");
        this.paramList = paramList;
    }

    public void registrarParametres(TaulaSimbols ts, Simbol funcSym) {
        if (paramList != null) {
            paramList.registrarParametres(ts, funcSym);
        }
    }

    @Override
    public String toString() {
        return "ParamsOpt(" + paramList + ")";
    }
}
