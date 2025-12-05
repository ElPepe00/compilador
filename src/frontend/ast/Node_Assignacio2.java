/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import backend.codi_intermedi.Codi;
import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_Assignacio2 extends Node {

    private Node_LValue lvalue;
    private Node_Express expr;
    
    public Node_Assignacio2(Node_LValue lvalue, Node_Express expr) {
        super("Assignacio2");
        this.lvalue = lvalue;
        this.expr = expr;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        TipusSimbol t_left = lvalue.getTipusSimbolLValue(ts);
        TipusSimbol t_right = expr.getTipusSimbol(ts);
        
        if (t_left != t_right) {
            throw new RuntimeException("Tipus incompatibles a l'assignacio: " + t_left + " = " + t_right);
        }
        
        // Marcar la variable base com assignada
        String idBase = lvalue.getRef().getIdBase();
        Simbol s = TaulaSimbols.cercarSimbol(idBase);
        
        if (s != null) {
            s.setAssignacio(true);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        String s = expr.generaCodi3a(codi3a);
        Node_Ref ref = lvalue.getRef();
        
        if (!ref.teIndex()) {
            String desti = ref.getIdBase();
            codi3a.afegir(Codi.COPY, s, null, desti); // a = s
            
        } else {
            Node_Ref baseRef = ref.getRefAnterior();
            String base = baseRef.getIdBase();
            String idx = ref.getIndex().generaCodi3a(codi3a);
            
            codi3a.afegir(Codi.IND_ASS, s, idx, base); //base[idx] = s
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        return "Assignacio2(" + lvalue + " = " + expr + ")";
    }
}
