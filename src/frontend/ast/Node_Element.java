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
public class Node_Element extends Node {

    private String tipus;
    private Node_Decl_loc declLoc;
    private Node_Instr instr;
    
    public Node_Element(String tipus, Node_Decl_loc declLoc) {
        super("Element");
        this.tipus = tipus;
        this.declLoc = declLoc;
    }
    
    public Node_Element(String tipus, Node_Instr instr) {
        super("Element");
        this.tipus = tipus;
        this.instr = instr;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        if ("decl".equals(tipus) && declLoc != null) {
            declLoc.gestioSemantica(ts);
            
        } else if ("instr".equals(tipus) && instr != null) {
            instr.gestioSemantica(ts);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        if ("decl".equals(tipus) && declLoc != null) {
            declLoc.generaCodi3a(codi3a);
        
        } else if ("instr".equals(tipus) && instr != null) {
            instr.generaCodi3a(codi3a);
        }
        
        return null;
    }

    
    @Override
    public String toString() {
        return "Node_Element(" + tipus 
                + (declLoc != null ? declLoc : instr) + ')';
    }
}
