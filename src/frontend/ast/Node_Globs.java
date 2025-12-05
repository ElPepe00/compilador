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
public class Node_Globs extends Node {

    private Node_Globs anterior;
    private Node_Decl_glob decl;

    public Node_Globs(Node_Globs anterior, Node_Decl_glob decl) {
        super("Globs");
        this.anterior = anterior;
        this.decl = decl;
    }
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (anterior != null) {
            anterior.gestioSemantica(ts);
        }
        
        if (decl != null) {
            decl.gestioSemantica(ts);
        }
    }
    
        @Override
    public String generaCodi3a(C3a codi3a) {
        if (anterior != null) {
            anterior.generaCodi3a(codi3a);
        }
        
        if (decl != null) {
            decl.generaCodi3a(codi3a);
        }
        
        return null;
    }

    @Override
    public String toString() {
        return "Node_Globs(" + anterior + " " + decl + ')';
    }
}
