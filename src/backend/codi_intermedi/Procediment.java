/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.codi_intermedi;

import frontend.taula_simbols.*;
import java.util.ArrayList;

/**
 * Classe que determina el que es un Procediment
 * @author josep
 */
public class Procediment {
    
    private final String nom;
    private TipusSimbol tipusRetorn;
    
    private int nivell;
    
    private int numParametres;
    private final ArrayList<TipusSimbol> tipusParam = new ArrayList<>();
    
    private int indexPrimeraVar = -1;
    private int numVariables = 0;
    
    private String etiquetaEntrada;
    private int instrInici = -1;
    private int instrFi = -1;
    
    private int midaFrame = 0;
    
    /**
     * Crea un Procediment amb els camps passats per paràmetre
     */
    public Procediment(String nom, TipusSimbol tipusRetorn, int nivell) {
        this.nom = nom;
        this.tipusRetorn = tipusRetorn;
        this.nivell = nivell;
    }

    // Getters i setters
    public String getNom() {
        return nom;
    }
    
    public void afegirParametre(TipusSimbol t) {
        tipusParam.add(t);
        numParametres++;
    }
    
    public ArrayList<TipusSimbol> getTipusParam() {
        return tipusParam;
    }

    public TipusSimbol getTipusRetorn() {
        return tipusRetorn;
    }

    public void setTipusRetorn(TipusSimbol tipusRetorn) {
        this.tipusRetorn = tipusRetorn;
    }

    public int getNivell() {
        return nivell;
    }

    public void setNivell(int nivell) {
        this.nivell = nivell;
    }

    public int getNumParametres() {
        return numParametres;
    }

    public void setNumParametres(int numParametres) {
        this.numParametres = numParametres;
    }

    public int getIndexPrimeraVar() {
        return indexPrimeraVar;
    }

    public void setIndexPrimeraVar(int indexPrimeraVar) {
        this.indexPrimeraVar = indexPrimeraVar;
    }

    public int getNumVariables() {
        return numVariables;
    }

    public void setNumVariables(int numVariables) {
        this.numVariables = numVariables;
    }

    public String getEtiquetaEntrada() {
        return etiquetaEntrada;
    }

    public void setEtiquetaEntrada(String etiquetaEntrada) {
        this.etiquetaEntrada = etiquetaEntrada;
    }

    public int getInstrInici() {
        return instrInici;
    }

    public void setInstrInici(int instrInici) {
        this.instrInici = instrInici;
    }

    public int getInstrFi() {
        return instrFi;
    }

    public void setInstrFi(int instrFi) {
        this.instrFi = instrFi;
    }

    public int getMidaFrame() {
        return midaFrame;
    }

    public void setMidaFrame(int midaFrame) {
        this.midaFrame = midaFrame;
    }

    @Override
    public String toString() {
        return "Procediment{" + "nom=" + nom 
                + ", tipusRetorn=" + tipusRetorn 
                + ", nivell=" + nivell 
                + ", numParametres=" + numParametres 
                + ", tipusParam=" + tipusParam 
                + ", indexPrimeraVar=" + indexPrimeraVar 
                + ", numVariables=" + numVariables 
                + ", etiquetaEntrada=" + etiquetaEntrada 
                + ", instrInici=" + instrInici 
                + ", instrFi=" + instrFi 
                + ", midaFrame=" + midaFrame + '}';
    }

    
    
    
}
