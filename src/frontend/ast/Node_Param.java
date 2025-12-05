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
        int mida = TipusUtils.midaBytesTipusBase(t);

        // 1) Donar d'alta el símbol del paràmetre a la TS
        Simbol paramSym = new Simbol(id, t, CategoriaSimbol.PARAMETRE, 0, mida);
        paramSym.setGlobal(false);
        TaulaSimbols.inserirSimbol(paramSym);

        // 2) Afegir el tipus a la signatura de la funció
        funcSym.getArguments().add(t);

        // 3) Crear l'objecte Parameter i afegir-lo a la funció
        int posicio = funcSym.getParametres().size(); // 0,1,2,...
        Parametre p = new Parametre(id, t, posicio);
        funcSym.getParametres().add(p);
    }

    @Override
    public String toString() {
        return "Param(" + tipus + " " + id + ")";
    }
}
