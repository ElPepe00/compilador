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
public class Node_Decl_loc_escalar extends Node_Decl_loc {

    private Node_Tipusv tipus;
    private String id;
    private Node_DeclTailEscalar tail;
    
    public Node_Decl_loc_escalar(Node_Tipusv tipus, String id, Node_DeclTailEscalar tail) {
        super();
        this.tipus = tipus;
        this.id = id;
        this.tail = tail;
    }
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        TipusSimbol tVar = tipus.getTipusSimbol();
        int mida = TipusUtils.midaBytesTipusBase(tVar);
        
        Simbol s = new Simbol(id, tVar, CategoriaSimbol.VARIABLE, 0, mida);
        s.setGlobal(false);
        
        TaulaSimbols.afegirSimbol(s);
        
        if (tail != null) {
            tail.gestioSemantica(ts);
            s.setAssignacio(true);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        TipusSimbol t = tipus.getTipusSimbol();
        int mida = tipus.getMidaBytes();
        codi3a.registrarLocal(id, t, false, 0, mida);
        
        if (tail != null) {
            tail.generaCodiInicialitzacio(codi3a, id);
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        return "Node_Decl_loc_escalar(" + tipus + " " + id + ", tail=" + tail + ')';
    }
}
