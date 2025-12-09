/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import frontend.taula_simbols.*;
import java.util.ArrayList;

/**
 *
 * @author josep
 */
public class Node_Param extends Node {

    private Node_Tipusv tipus;
    private String id;
    
    private boolean esArray;

    private Simbol simbolParam;
    
    public Node_Param(Node_Tipusv tipus, String id, boolean esArray) {
        super("Param");
        this.tipus = tipus;
        this.id = id;
        this.esArray = esArray;
    }

    public TipusSimbol getTipusSimbol() {
        
        TipusSimbol tBase = tipus.getTipusSimbol();
        
        if (esArray) {
            return TipusUtils.getTipusArrayDesdeNomBase(tipus.getNomTipus());
        }
        
        return tBase;
    }

    public String getId() {
        return id;
    }

    /**
     * Dona d'alta el paràmetre a la TS i afegeix el tipus
     * a la signatura de la funció.
     */
    public void registrarComParametre(TaulaSimbols ts, Simbol funcSym) {
        
        TipusSimbol t = getTipusSimbol();
        int mida = t.getMidaBytes();

        // 1. Crear el simbol del parametre
        this.simbolParam = new Simbol(id, t, CategoriaSimbol.PARAMETRE);
        this.simbolParam.setOcupacio(mida);
        
        // guardam la posicio del parametre, per codi 3@
        int pos = funcSym.getLlistaParametres().size();
        this.simbolParam.setPosicioParam(pos);
        
        if (esArray) {
            this.simbolParam.setEsArray(true);
            
            ArrayList<Integer> dimsD = new ArrayList<>();
            dimsD.add(0);
            this.simbolParam.setDimensions(dimsD);
        }
        
        // 2. Afegir a la taula de simbols
        ts.afegirSimbol(this.simbolParam);

        // 3. Actualitzam les dades de la funcio de la funcio
        Parametre p = new Parametre(id, t, pos);
        
        funcSym.addParametre(p);
        funcSym.addArgument(t);
    }

    @Override
    public String toString() {
        return "Param(" + tipus + " " + id + ")";
    }
}
