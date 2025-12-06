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
    
    private Simbol simbolMain;

    public Node_Main(Node_Elements elements) {
        super("Main");
        this.elements = elements;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        // Cream un simbol per main "un poc especial", per guardar la mida
        this.simbolMain = new Simbol("main", TipusSimbol.VOID, CategoriaSimbol.FUNCIO);
        this.simbolMain.setEtiqueta("main");
        
        // Àmbit propi per al main
        ts.entrarFuncio();

        if (elements != null) {
            elements.gestioSemantica(ts);
        }

        // Actualitzam la mida del frame
        this.simbolMain.setMidaFrame(ts.getOffsetActual());
        
        ts.sortirBloc();
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        //etiqueta d'entrada al programa
        codi3a.afegirEtiqueta("main");
        
        String aux = String.valueOf(this.simbolMain.getMidaFrame());
        codi3a.afegir(Codi.PMB, "main", aux, null);
        
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

