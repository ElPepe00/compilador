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
public class Node_Instr extends Node {

    private Node nodeInstr;
    
    public Node_Instr(Node nodeInstr) {
        super("Instr");
        this.nodeInstr = nodeInstr;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        if (nodeInstr != null) {
            nodeInstr.gestioSemantica(ts);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        if (nodeInstr != null) {
            nodeInstr.generaCodi3a(codi3a);
        }
        
        return null;
    }
    
    
    @Override
    public String toString() {
        return "Node_Instr(" + nodeInstr + ')';
    }
}
