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
        
        String idBase = ref.getIdBase();
        Simbol s = TaulaSimbols.cercarSimbol(idBase);
        
        if (s == null) {
            throw new RuntimeException("Identificador no declarat a LValue: " + idBase);
        }
        
        CategoriaSimbol cat = s.getCategoria();
        
        if (cat == CategoriaSimbol.CONSTANT 
            || cat == CategoriaSimbol.FUNCIO 
            || cat == CategoriaSimbol.PROCEDIMENT) {
            
            throw new RuntimeException("No es pot assignar a: " + cat + " '" + idBase + "'");
        }
        
        // Node_Ref comprova indexs i ens dona el tipus final
        return ref.getTipusSimbol(ts);
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        return null;
    }

    
    
    @Override
    public String toString() {
        return "Node_LValue(" + ref.toString() + ")";
    }
}
