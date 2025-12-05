/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.codi_intermedi;

import frontend.taula_simbols.*;

/**
 * Clase que defineix el que es una entrada(variable) a la taula de variables
 * @author josep
 */
public class Variable {
    
    private final String nom;
    private final String nomProc;
    private final TipusSimbol tipus;
    
    private final boolean esParametre;
    private final int posicioParam;
    
    public final boolean esTaula;
    public final int numElements;
    
    private final int midaBytes;
    private int offset;

    public Variable(String nom, String nomProc, TipusSimbol tipus, boolean esParametre, int posicioParam, boolean esTaula, int numElements, int midaBytes, int offset) {
        this.nom = nom;
        this.nomProc = nomProc;
        this.tipus = tipus;
        this.esParametre = esParametre;
        this.posicioParam = posicioParam;
        this.esTaula = esTaula;
        this.numElements = numElements;
        this.midaBytes = midaBytes;
        this.offset = offset;
    }

    public String getNom() {
        return nom;
    }

    public String getNomProc() {
        return nomProc;
    }

    public TipusSimbol getTipus() {
        return tipus;
    }

    public boolean isEsParametre() {
        return esParametre;
    }

    public int getPosicioParam() {
        return posicioParam;
    }

    public boolean isEsTaula() {
        return esTaula;
    }

    public int getNumElements() {
        return numElements;
    }

    public int getMidaBytes() {
        return midaBytes;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Variable{" + "nom=" + nom 
                + ", nomProc=" + nomProc 
                + ", tipus=" + tipus 
                + ", esParametre=" + esParametre 
                + ", posicioParam=" + posicioParam 
                + ", esTaula=" + esTaula 
                + ", numElements=" + numElements 
                + ", midaBytes=" + midaBytes 
                + ", offset=" + offset + '}';
    }
}
