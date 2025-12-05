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
public abstract class Node_Decl_glob extends Node {

    public Node_Decl_glob() {
        super("Decl_glob");
    }
    
    @Override
    public abstract void gestioSemantica(TaulaSimbols ts);
    
}
