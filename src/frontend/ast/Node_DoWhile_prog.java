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
public class Node_DoWhile_prog extends Node {

    private Node_Elements cos;
    private Node_Cond cond;

    public Node_DoWhile_prog(Node_Elements cos, Node_Cond cond) {
        super("DoWhile_prog");
        this.cos = cos;
        this.cond = cond;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // Cos amb àmbit propi
        TaulaSimbols.entrarBloc();
        if (cos != null) {
            cos.gestioSemantica(ts);
        }
        TaulaSimbols.sortirBloc();

        // Condició BOOL
        cond.gestioSemantica(ts);
    }
    
        @Override
    public String generaCodi3a(C3a codi3a) {
        String etCos = codi3a.novaEtiqueta();
        
        codi3a.afegirEtiqueta(etCos);
        
        if (cos != null) {
            cos.generaCodi3a(codi3a);
        }
        
        String condTemp = cond.generaCodi3a(codi3a);
        codi3a.afegir(Codi.IF_NE, condTemp, "0", etCos); // si true -> repeat
        
        return null;
    }

    @Override
    public String toString() {
        return "DoWhile_prog(cos=" + cos + ", cond=" + cond + ")";
    }
}
