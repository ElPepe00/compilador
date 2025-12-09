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
public class Node_AssignacioRead extends Node {

    private Node_LValue lvalue;

    public Node_AssignacioRead(Node_LValue lvalue) {
        super("AssignacioRead");
        this.lvalue = lvalue;
    }

    public Node_LValue getLValue() {
        return lvalue;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // 1. Comprovam que el desti sigui valid i l'obtenim
        TipusSimbol tDest = lvalue.getTipusSimbolLValue(ts);

        // 2. Només té sentit llegir en un tipus bàsic
        if (tDest != TipusSimbol.INT &&
            tDest != TipusSimbol.BOOL &&
            tDest != TipusSimbol.CARACTER) {

            throw new RuntimeException("No es pot fer llegir() sobre tipus " + tDest);
        }

        // 3. Marcam la variable com "assignada"
        Simbol s = lvalue.getRef().getSimbolAssoc();
        
        if (s != null) {
            s.setAssignacio(true);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        // 1. Genera la crida a llegir(), retorna el valor llegit
        String tLlegit = codi3a.novaTemp();
        codi3a.afegir(Codi.CALL, "llegir", null, tLlegit);
        
        // 2. Recuperam informacio del desti
        Node_Ref ref = lvalue.getRef();
        Simbol s = ref.getSimbolAssoc();
        
        if (s == null) {
            throw new RuntimeException("Error intern: Símbol no trobat (Read)");
        }

        String nomDesti = s.getNom();
        
        if (!ref.teIndex()) {
            codi3a.afegir(Codi.COPY, tLlegit, null, nomDesti); // a = llegir()
            
        } else {
            String idx = ref.generaCodiIndexAplanat(codi3a);
            codi3a.afegir(Codi.IND_ASS, tLlegit, idx, nomDesti); // a[i][j] = llegir()
        }
        
        return null;
    }
    
    

    @Override
    public String toString() {
        return "AssignacioRead(" + lvalue + " = llegir())";
    }
}
