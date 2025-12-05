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
public class Node_Crida_proc extends Node {

    private Node_ArgsPrint imprimirArgs; // per IMPRIMIR(...)
    private Node_CridaBase base;         // per procediments usuari

    public Node_Crida_proc(Node_ArgsPrint imprimirArgs, Node_CridaBase base) {
        super("Crida_proc");
        this.imprimirArgs = imprimirArgs;
        this.base = base;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        if (imprimirArgs != null) {
            // Crida al built-in IMPRIMIR: només comprovam expressions
            imprimirArgs.gestioSemantica(ts);
        } else if (base != null) {
            // Crida a procediment (no ha de retornar valor)
            base.comprovarCrida(ts, false);
        }
    }
    
    @Override
    public String generaCodi3a(C3a codi3a) {
        
        if (imprimirArgs != null) {
            imprimirArgs.generaCodiParams(codi3a);
            codi3a.afegir(Codi.CALL, "imprimir", null, null);
            
        } else if (base != null) {
            base.generaCodiCridaProc(codi3a);
        }
        
        return null;
    }

    @Override
    public String toString() {
        if (imprimirArgs != null) {
            return "Crida_proc(IMPRIMIR " + imprimirArgs + ")";
        } else {
            return "Crida_proc(" + base + ")";
        }
    }
}
