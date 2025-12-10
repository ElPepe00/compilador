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
public class Node_DeclTailTaulaChar extends Node {

    private Node_ArrayLitChar arrayLit;

    public Node_DeclTailTaulaChar(Node_ArrayLitChar arrayLit) {
        super("DeclTailTaulaChar");
        this.arrayLit = arrayLit;
    }

    public void gestioSemantica(TaulaSimbols ts, int midaEsperada) {
        if (arrayLit == null) return;
        int nElems = arrayLit.comptarElements();
        if (nElems != midaEsperada) {
            errorSemantic("Inicialització de taula CHAR amb " + nElems +
                    " elements, però s'esperaven " + midaEsperada);
            return;
        }
        
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
