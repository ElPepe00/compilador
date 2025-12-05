/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.Codi;
import backend.codi_intermedi.C3a;
import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_Decl_glob_escalar extends Node_Decl_glob {

    private Node_Tipusv tipus;
    private String id;
    private Node_Express exprInit;

    public Node_Decl_glob_escalar(Node_Tipusv tipus, String id, Node_Express expr) {
        this.tipus = tipus;
        this.id = id;
        this.exprInit = expr;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        TipusSimbol tConst = tipus.getTipusSimbol();
        TipusSimbol tExpr = exprInit.getTipusSimbol(ts);
        
        if (tConst != tExpr) {
            throw new RuntimeException("Error de tipus a la constant global '"
            + id + "': esperant " + tConst + " però s'ha trobat: " + tExpr);
        }
        
        int mida = TipusUtils.midaBytesTipusBase(tConst);
        
        Simbol s = new Simbol(id, tConst, CategoriaSimbol.CONSTANT, 0, mida);
        s.setGlobal(true);
        
        TaulaSimbols.inserirSimbol(s);
    }
    
    @Override
    public String generaCodi3a(C3a codi3a) {
        
        String s = exprInit.generaCodi3a(codi3a);
        codi3a.afegir(Codi.COPY, s, null, id);
        return null;
    }
    @Override
    public String toString() {
        return "Node_Decl_glob_escalar(" + tipus + " " + id + " = " + exprInit + ")";
    }
}
