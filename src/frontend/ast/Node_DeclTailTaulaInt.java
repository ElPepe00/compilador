/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import frontend.taula_simbols.*;
import backend.codi_intermedi.*;

/**
 *
 * @author josep
 */
public class Node_DeclTailTaulaInt extends Node {

    private Node_ArrayLitInt arrayLit;

    public Node_DeclTailTaulaInt(Node_ArrayLitInt arrayLit) {
        super("DeclTailTaulaInt");
        this.arrayLit = arrayLit;
    }

    public void gestioSemantica(TaulaSimbols ts, int midaEsperada) {
        if (arrayLit == null) return;
        int nElems = arrayLit.comptarElements();
        if (nElems != midaEsperada) {
            throw new RuntimeException("Inicialització de taula INT amb " + nElems +
                    " elements, però s'esperaven " + midaEsperada);
        }
        
        // Validam tambe els tipus interns
        arrayLit.gestioSemantica(ts);
    }
    
    public void generaCodiInicialitzacio(C3a codi3a, String nomArrayBase) {
        if (arrayLit != null) {
            arrayLit.generaCodiInicialitzacio(codi3a, nomArrayBase);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        return null;
    }
    
    
    
}
