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
 * Estructura de control WHILE
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
        // 1. Validar la condicio
        cond.gestioSemantica(ts);

        // 2. Cos del while amb nou àmbit
        ts.entrarBloc();
        if (cos != null) {
            cos.gestioSemantica(ts);
        }
        ts.sortirBloc();
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        String etCond = codi3a.novaEtiqueta();
        String etEnd = codi3a.novaEtiqueta();
        
        // Etiqueta de retorn
        codi3a.afegirEtiqueta(etCond);
        
        // Si fals, surt del bucle
        String condTemp = cond.generaCodi3a(codi3a);
        codi3a.afegir(Codi.IF_EQ, condTemp, "0", etEnd);
        
        // Cos del bucle
        if (cos != null) {
            cos.generaCodi3a(codi3a);
        }
        
        // torna a avaluar
        codi3a.afegir(Codi.GOTO, null, null, etCond);
        // Etiqueta de sortida
        codi3a.afegirEtiqueta(etEnd);
        
        return null;
    }
    
    

    @Override
    public String toString() {
        return "While_prog(cond=" + cond + ", cos=" + cos + ")";
    }
}
