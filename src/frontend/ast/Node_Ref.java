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
import frontend.taula_simbols.TipusSimbol;
import frontend.gestor_errors.*;


/**
 * Node per una referència a identificador
 *  - Només ID:     x
 *  - Accés taula:  a[i] o a[i][j]...
 * @author josep
 */
public class Node_Ref extends Node {

    private String idBase;      // si es un identificador
    
    private Node_Ref base;      // referencia base (a de a[i])
    private Node_Express index; //expressio de l'index (i de a[i])
    
    public Node_Ref(String id) {
        super("Ref");
        this.idBase = id;
        this.base = null;
        this.index = null;
    }
    
    public Node_Ref(Node_Ref base, Node_Express index) {
        super("Ref");
        this.idBase = null;
        this.base = base;
        this.index = index;
    }
    
    public boolean esSimple() {
        return base == null && index == null;
    }
    
    public String getIdBase() {
        if (base == null) {
            return idBase;
        }
        return base.getIdBase();
    }
    
    public Node_Ref getBase() {
        return base;
    }
    
    public Node_Express getIndex() {
        return index;
    }
    
    public boolean teIndex() {
        
        if (index != null) {
            return true;
        }
        
        if (base != null) {
            return base.teIndex();
        }
        
        return false;
    }
    
    public Node_Ref getRefAnterior() {
        return base.getBase();
    }
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        getTipusSimbol(ts);
    }
    
    // Mètode que retorna el tipus de simbol de la referència, comprovant la TS
    // Si hi ha error es controla amb el gestor d'errors
    public TipusSimbol getTipusSimbol(TaulaSimbols ts) {
        if (base == null) {
            Simbol s = TaulaSimbols.cercaSimbol(idBase);
            if (s == null) {
                throw new RuntimeException("Identificador no declarat: " + idBase);
            }
            
            return s.getTipus();
            
        } else {
            TipusSimbol tBase = base.getTipusSimbol(ts);    //potser INT, CHAR, BOOL
            TipusSimbol tIndex = index.getTipusSimbol(ts); // ha de ser INT
            
            if (tIndex != TipusSimbol.INT) {
                throw new RuntimeException("L'index de la taula ha de ser INT");
            }
            
            // Comprovam que tBse es una taula i retornam el tipus de taula
            switch (tBase) {
                case TAULA_INT: return TipusSimbol.TAULA_INT;
                case TAULA_CARACTER: return TipusSimbol.TAULA_CARACTER;
                case TAULA_BOOL: return TipusSimbol.TAULA_BOOL;
                default:
                    throw new RuntimeException("Intent d'indexar un objecte que no es una taula");
            }
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        if (!teIndex()) {
            String t = codi3a.novaTemp();
            codi3a.afegir(Codi.COPY, getIdBase(), null, t);
            return t;
        }
        
        // Suposam una sola dimensio a[i]; per mes dimensions cal un desplaçament
        Node_Ref baseRef = this.base;
        String baseName = baseRef.getIdBase();
        String idxTemp = this.index.generaCodi3a(codi3a);
        
        String t = codi3a.novaTemp();
        codi3a.afegir(Codi.IND_VAL, baseName, idxTemp, t); // t = base[idx]
        return t;
        
    }

    @Override
    public String toString() {
        
        if (base == null) {
            return "Node_Ref(" + idBase + ")";
        } else {
            return "Node_Ref(" +  base.toString() + "[ " + index.toString() + " ])";
        }
    }
}
