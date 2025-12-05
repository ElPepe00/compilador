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
public class Node_While_prog extends Node {

    private Node_Cond cond;
    private Node_Elements cos;

    public Node_While_prog(Node_Cond cond, Node_Elements cos) {
        super("While_prog");
        this.cond = cond;
        this.cos = cos;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // Condició BOOL
        cond.gestioSemantica(ts);

        // Cos del while amb nou àmbit
        TaulaSimbols.entrarSubAmbit();
        if (cos != null) {
            cos.gestioSemantica(ts);
        }
        TaulaSimbols.sortirSubAmbit();
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        String etCond = codi3a.novaEtiqueta();
        String etEnd = codi3a.novaEtiqueta();
        
        codi3a.afegirEtiqueta(etCond);
        
        String condTemp = cond.generaCodi3a(codi3a);
        codi3a.afegir(Codi.IF_EQ, condTemp, "0", etEnd); //si false -> surt del bucle
        
        if (cos != null) {
            cos.generaCodi3a(codi3a);
        }
        
        codi3a.afegir(Codi.GOTO, null, null, etCond);
        codi3a.afegirEtiqueta(etEnd);
        
        return null;
    }
    
    

    @Override
    public String toString() {
        return "While_prog(cond=" + cond + ", cos=" + cos + ")";
    }
}
