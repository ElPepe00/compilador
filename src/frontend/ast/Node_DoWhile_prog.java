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
        // 1. Ambit del cos
        ts.entrarBloc();
        if (cos != null) {
            cos.gestioSemantica(ts);
        }
        ts.sortirBloc();

        // 2. Validam la condicio
        cond.gestioSemantica(ts);
    }
    
        @Override
    public String generaCodi3a(C3a codi3a) {
        String etInici = codi3a.novaEtiqueta();
        
        // Etiqueta inici
        codi3a.afegirEtiqueta(etInici);
        
        // Cos del bucle
        if (cos != null) {
            cos.generaCodi3a(codi3a);
        }
        
        // Condicio
        String condTemp = cond.generaCodi3a(codi3a);
        
        // Si true (-1) torna a l'inici, sino (0) surt
        codi3a.afegir(Codi.IF_NE, condTemp, "0", etInici);
        
        return null;
    }

    @Override
    public String toString() {
        return "DoWhile_prog(cos=" + cos + ", cond=" + cond + ")";
    }
}
