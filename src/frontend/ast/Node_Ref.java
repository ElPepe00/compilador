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
import java.util.ArrayList;


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
            return TipusUtils.getTipusBaseDeTipusArray(tBase);
        }
    }
    
    // Mètode auxiliar per recollir tots els índexs de la cadena recursiva
    // a[i][j] -> Retorna [i, j]
    public void recollirIndexos(ArrayList<Node_Express> llista) {
        if (base != null && base.teIndex()) {
            base.recollirIndexos(llista);
        }
        if (index != null) {
            llista.add(index);
        }
    }

    // Calcula l'índex pla final (offset) i retorna el temporal on està guardat
    public String generaCodiIndexAplanat(C3a codi3a) {
        Simbol s = getSimbolAssoc();
        ArrayList<Integer> dims = s.getDimensions();
        
        // Recollim les expressions dels índexs
        ArrayList<Node_Express> indexExprs = new ArrayList<>();
        recollirIndexos(indexExprs);
        
        if (indexExprs.size() != dims.size()) {
            throw new RuntimeException("Error semàntic: L'array '" + getSimbolAssoc().getNom() + 
            "' té " + dims.size() + " dimensions, però s'han proporcionat " + indexExprs.size() + " índexs.");
        }

        // FÓRMULA D'APLANAMENT (Row-Major Order)
        // Per a [D1][D2]:  flat = i*D2 + j
        // Per a [D1][D2][D3]: flat = (i*D2 + j)*D3 + k
        
        String tAcumulat = indexExprs.get(0).generaCodi3a(codi3a); // i
        
        for (int k = 1; k < indexExprs.size(); k++) {
            // 1. Multiplicar l'acumulat per la dimensió següent
            int dimSize = dims.get(k); 
            String tMult = codi3a.novaTemp();
            codi3a.afegir(Codi.PROD, tAcumulat, String.valueOf(dimSize), tMult);
            
            // 2. Sumar l'índex actual
            String tIdx = indexExprs.get(k).generaCodi3a(codi3a);
            String tSuma = codi3a.novaTemp();
            codi3a.afegir(Codi.ADD, tMult, tIdx, tSuma);
            
            tAcumulat = tSuma;
        }
        
        return tAcumulat;
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        if (this.simbolAssoc == null) {
            throw new RuntimeException("ERROR INTERN: Node_Ref sense simbol associat");
        }

        String nomVar = getSimbolAssoc().getNom();
        
        
        if (!teIndex()) {
            // Cas simple: x
            // Si volem carregar el valor a un temporal
            String t = codi3a.novaTemp();
            codi3a.afegir(Codi.COPY, nomVar, null, t);
            return t;
            
        } else {
            // Cas array: a[i] o a[i][j]
            String tIndexFinal = generaCodiIndexAplanat(codi3a);
            
            String t = codi3a.novaTemp();
            // ind_val a i t  ->  t = a[i]
            codi3a.afegir(Codi.IND_VAL, nomVar, tIndexFinal, t);
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
