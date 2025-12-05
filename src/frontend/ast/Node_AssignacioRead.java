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
        // Tipus del lloc on escriurem
        TipusSimbol tDest = lvalue.getTipusSimbolLValue(ts);

        // Només té sentit llegir en un tipus bàsic
        if (tDest != TipusSimbol.INT &&
            tDest != TipusSimbol.BOOL &&
            tDest != TipusSimbol.CARACTER) {

            throw new RuntimeException("No es pot fer llegir() sobre tipus " + tDest);
        }

        // Marcam la variable com "assignada"
        String idBase = lvalue.getRef().getIdBase();
        Simbol s = TaulaSimbols.cercarSimbol(idBase);
        if (s != null) {
            s.setAssignacio(Boolean.TRUE);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        String t = codi3a.novaTemp();
        codi3a.afegir(Codi.CALL, "llegir", null, t);
        
        Node_Ref ref = lvalue.getRef();

        if (!ref.teIndex()) {
            codi3a.afegir(Codi.COPY, t, null, ref.getIdBase());
            
        } else {
            String base = ref.getRefAnterior().getIdBase();
            String idx = ref.getIndex().generaCodi3a(codi3a);
            codi3a.afegir(Codi.IND_ASS, t, idx, base);
        }
        
        return null;
    }
    
    

    @Override
    public String toString() {
        return "AssignacioRead(" + lvalue + " = llegir())";
    }
}
