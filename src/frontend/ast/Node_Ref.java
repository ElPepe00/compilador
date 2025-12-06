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
    
    private Simbol simbolAssoc;
    
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
    
    public Simbol getSimbolAssoc() {
        if (base != null) {
            return base.getSimbolAssoc();
        }
        
        return simbolAssoc;
    }
    
    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // cridam al mètode intern que resol i guarda el simbol
        getTipusSimbol(ts);
    }
    
    // Mètode que retorna el tipus de simbol de la referència, comprovant la TS
    // Si hi ha error es controla amb el gestor d'errors
    public TipusSimbol getTipusSimbol(TaulaSimbols ts) {
        if (base == null) {
            // 1. Cas base: id simple
            Simbol s = ts.cercarSimbol(idBase);
            if (s == null) {
                throw new RuntimeException("Identificador no declarat: " + idBase);
            }
            
            this.simbolAssoc = s; // guardam el simbol
            return s.getTipus();
            
        } else {
            // 2. Cas recursiu
            // primer gestionam la base
            TipusSimbol tBase = base.getTipusSimbol(ts);    //potser INT, CHAR, BOOL
            
            // l'array "a[i]" es refereix al mateix simbol de memoria que "a"
            // per tant, copiam la referencia del simbol de fill a pare
            this.simbolAssoc = base.getSimbolAssoc();
            
            // segon gestionam index
            TipusSimbol tIndex = index.getTipusSimbol(ts);
            
            if (tIndex != TipusSimbol.INT) {
                throw new RuntimeException("L'index de la taula ha de ser INT");
            }
            
            // Si tBase es taula, retornam el tipus de taula
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
        
        if (this.simbolAssoc == null) {
            throw new RuntimeException("ERROR INTERN: Node_Ref sense simbol associat");
        }

        String nomVar = this.simbolAssoc.getNom();
        
        //int offset = this.simbolAssoc.getOffset();
        
        if (!teIndex()) {
            // Cas simple: x
            // Si volem carregar el valor a un temporal
            String t = codi3a.novaTemp();
            codi3a.afegir(Codi.COPY, nomVar, null, t);
            return t;
        } else {
            // Cas array: a[i]
            // Aquí necessitam l'índex. Com que 'index' és un Node_Express, ell generarà el seu propi codi.
            String idxTemp = this.index.generaCodi3a(codi3a);
            
            String t = codi3a.novaTemp();
            // ind_val a i t  ->  t = a[i]
            codi3a.afegir(Codi.IND_VAL, nomVar, idxTemp, t);
            return t;
        }
        
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
