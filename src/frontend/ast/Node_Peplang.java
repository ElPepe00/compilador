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
public class Node_Peplang extends Node {
    
    private Node_Globs globs;
    private Node_Funcs funcs;
    private Node_Main main;

    public Node_Peplang(Node_Globs globs, Node_Funcs funcs, Node_Main main) {
        super("Peplang");
        this.globs = globs;
        this.funcs = funcs;
        this.main = main;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        // Suposam que el driver ja ha fet SymbolTable.entrarSubAmbit()
        // per crear l'àmbit global.
        if (globs != null) {
            globs.gestioSemantica(ts);
            
        }
        if (funcs != null) {
            funcs.gestioSemantica(ts);
        }
        if (main != null) {
            main.gestioSemantica(ts);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        if (globs != null) {
            globs.generaCodi3a(codi3a);
        }
        if (funcs != null) {
            funcs.generaCodi3a(codi3a);
        }
        if (main != null) {
            main.generaCodi3a(codi3a);
        }
        
        return null;
    }
    
    

    @Override
    public String toString() {
        return "Peplang(" + globs + ", " + funcs + ", " + main + ")";
    }
}
