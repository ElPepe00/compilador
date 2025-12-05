/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import frontend.taula_simbols.*;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author josep
 */
public class Node_ArgsOpt extends Node {

    private Node_ArgList argList; // pot ser null

    public Node_ArgsOpt(Node_ArgList argList) {
        super("ArgsOpt");
        this.argList = argList;
    }

    public List<TipusSimbol> getTipusArguments(TaulaSimbols ts) {
        if (argList == null) return Collections.emptyList();
        return argList.getTipusArguments(ts);
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (argList != null) argList.gestioSemantica(ts);
    }
    
    public void generaCodiParams(C3a codi3a) {
        if (argList != null) {
            argList.generaCodi3a(codi3a);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        generaCodiParams(codi3a);
        return null;
    }
    
    

    @Override
    public String toString() {
        return "ArgsOpt(" + argList + ")";
    }
    
}
