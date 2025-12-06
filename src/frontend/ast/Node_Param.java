/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_Param extends Node {

    private Node_Tipusv tipus;
    private String id;

    private Simbol simbolParam;
    
    public Node_Param(Node_Tipusv tipus, String id) {
        super("Param");
        this.tipus = tipus;
        this.id = id;
    }

    public TipusSimbol getTipusSimbol() {
        return tipus.getTipusSimbol();
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
        
        // 2. Afegir a la taula de simbols
        ts.afegirSimbol(this.simbolParam);

        // 3. Actualitzam les dades de la funcio de la funcio
        Parametre p = new Parametre(id, t, pos);
        p.setOffset(this.simbolParam.getOffset());
        
        funcSym.addParametre(p);
        funcSym.addArgument(t);
    }

    @Override
    public String toString() {
        return "Param(" + tipus + " " + id + ")";
    }
}
