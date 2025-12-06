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
 * Node del costat esquerra d'una assignacio
 * Sempre encapsula una referencia
 * @author josep
 */
public class Node_LValue extends Node {

    private Node_Ref ref;
    
    public Node_LValue(Node_Ref ref) {
        super("LValue");
        this.ref = ref;
    }
    
    public Node_Ref getRef() {
        return ref;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        getTipusSimbolLValue(ts);
    }
    
    // Mètode que retorna el tipus de simbol assignable a LValue, comprovant:
    //  - no es pot assignar a una constant
    //  - no es pot assignar a una funcio amb retorn o sense
    public TipusSimbol getTipusSimbolLValue(TaulaSimbols ts) {
        
        // Obtenim el tipus de simbol del node
        TipusSimbol t = ref.getTipusSimbol(ts);
        
        // Recuperam el simbol
        Simbol s = ref.getSimbolAssoc();
        
        if (s == null) {
            throw new RuntimeException("Identificador no declarat a LValue");
        }
        
        CategoriaSimbol cat = s.getCategoria();
        String nom = s.getNom();
        
        if (cat == CategoriaSimbol.CONSTANT 
            || cat == CategoriaSimbol.FUNCIO 
            || cat == CategoriaSimbol.PROCEDIMENT) {
            
            throw new RuntimeException("No es pot assignar a: '" + nom + "' perque es " + cat + "");
        }
        
        return t;
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        return null; //LValue no genera codi per si mateix
    }

    
    
    @Override
    public String toString() {
        return "Node_LValue(" + ref.toString() + ")";
    }
}
