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
    
    private Simbol simbolGlobal;

    public Node_Decl_glob_escalar(Node_Tipusv tipus, String id, Node_Express expr) {
        super();
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
        
        int mida = tConst.getMidaBytes();
        
        // Crear simbol CONSTANT o variable global
        this.simbolGlobal = new Simbol(id, tConst, CategoriaSimbol.CONSTANT);
        this.simbolGlobal.setOcupacio(mida);
        this.simbolGlobal.setEsGlobal(true);
        
        // Afegim a la taula de simbols
        ts.afegirSimbol(this.simbolGlobal);
        
        // Guardam el valor si es numeric per optimitzacions (opcional)
        if (exprInit.getNum() != null) {
            this.simbolGlobal.setValor(exprInit.getNum().getValorEnter());
        }
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
