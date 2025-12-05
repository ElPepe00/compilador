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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josep
 */
public class Node_ArgList extends Node {
    
    private Node_ArgList anteriors;
    private Node_Express expr;

    public Node_ArgList(Node_ArgList anteriors, Node_Express expr) {
        super("ArgList");
        this.anteriors = anteriors;
        this.expr = expr;
    }

    public List<TipusSimbol> getTipusArguments(TaulaSimbols ts) {
        List<TipusSimbol> l = new ArrayList<>();
        omplirTipus(ts, l);
        return l;
    }

    private void omplirTipus(TaulaSimbols ts, List<TipusSimbol> dest) {
        if (anteriors != null) {
            anteriors.omplirTipus(ts, dest);
        }
        if (expr != null) {
            dest.add(expr.getTipusSimbol(ts));
        }
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // Simplement forçam que totes les expressions siguin semànticament correctes
        if (anteriors != null) anteriors.gestioSemantica(ts);
        if (expr != null) expr.gestioSemantica(ts);
    }

    
    public void generaCodiParams(C3a codi3a) {  
        if (anteriors != null) {
            anteriors.generaCodiParams(codi3a);
        }
        
        String t = expr.generaCodi3a(codi3a);
        codi3a.afegir(Codi.PARAM_S, t, null, null);
        
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        generaCodiParams(codi3a);
        return null;
    }
    
    
    /*
    public void generaCodiWrite(C3a codi3a) {
        if (anteriors != null) {
            anteriors.generaCodi3a(codi3a);
            String t = expr.generaCodi3a(codi3a);
            codi3a.afegir(Codi.WRITE, t, null, null);
        }
    }
    */
    
    @Override
    public String toString() {
        return "ArgList(" + anteriors + ", " + expr + ")";
    }
}
