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
public class Node_Main extends Node {

    private Node_Elements elements;

    public Node_Main(Node_Elements elements) {
        super("Main");
        this.elements = elements;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // Àmbit propi per al main
        TaulaSimbols.entrarBloc();

        if (elements != null) {
            elements.gestioSemantica(ts);
        }

        TaulaSimbols.sortirBloc();
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        //etiqueta d'entrada al programa
        codi3a.afegirEtiqueta("main");
        
        if (elements != null) {
            elements.generaCodi3a(codi3a);
        }
        
        codi3a.afegir(Codi.HALT, null, null, null); // aturada de programa
        return null;
    }
    
    

    @Override
    public String toString() {
        return "Main(" + elements + ")";
    }
}

