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
public abstract class Node_Decl_loc extends Node {
    
    public Node_Decl_loc() {
        super("Decl_loc");
    }
    
    public abstract void gestioSemantica(TaulaSimbols ts);
}
