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
public class Node_Crida_func extends Node {

    private Node_CridaBase base;

    public Node_Crida_func(Node_CridaBase base) {
        super("Crida_func");
        this.base = base;
    }

    public Node_CridaBase getBase() {
        return base;
    }

    /**
     * Crida de funció en context d'expressió.
     * Retorna el tipus de retorn de la funció.
     */
    public TipusSimbol getTipusSimbol(TaulaSimbols ts) {
        // comFuncio = true, exigit que sigui FUNCIO
        return base.comprovarCrida(ts, true);
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // Amb això ja comprovam tota la crida
        getTipusSimbol(ts);
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        return base.generaCodiCridaFunc(codi3a);
    }
    
    
    @Override
    public String toString() {
        return "Crida_func(" + base + ")";
    }
    
}
