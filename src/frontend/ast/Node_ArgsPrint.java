/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_ArgsPrint extends Node {

    private Node_ArgList argList; // pot ser null

    public Node_ArgsPrint(Node_ArgList argList) {
        super("ArgsPrint");
        this.argList = argList;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (argList != null) {
            argList.gestioSemantica(ts);
        }
    }
    
    
    public void generaCodiParams(C3a codi3a) {
        if (argList != null) {
            argList.generaCodiParams(codi3a);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        generaCodiParams(codi3a);
        return null;
    }
    
    

    @Override
    public String toString() {
        return "ArgsPrint(" + argList + ")";
    }
    
}
