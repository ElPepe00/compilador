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
    
    private Simbol simbolAssoc;
    
    public Node_Decl_loc_escalar(Node_Tipusv tipus, String id, Node_DeclTailEscalar tail) {
        super();
        this.tipus = tipus;
        this.id = id;
        this.tail = tail;
    }
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        TipusSimbol tVar = tipus.getTipusSimbol();
        int mida = tVar.getMidaBytes();
        
        // 1. Crear el simbol
        this.simbolAssoc = new Simbol(id, tVar, CategoriaSimbol.VARIABLE);
        this.simbolAssoc.setOcupacio(mida);
        
        // 2. Afegir el simbol a la TS
        ts.afegirSimbol(simbolAssoc);
        
        // 3. Gestionar inicialitzacio
        if (tail != null) {
            tail.gestioSemantica(ts, tVar); //comprova tipus
            this.simbolAssoc.setAssignacio(true);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {

        if (tail != null) {
            tail.generaCodiInicialitzacio(codi3a, this.simbolAssoc.getNom());
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        return "Node_Decl_loc_escalar(" + tipus + " " + id + ", tail=" + tail + ')';
    }
}
