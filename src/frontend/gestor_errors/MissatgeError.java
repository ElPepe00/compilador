/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.gestor_errors;

/**
 * Clase que genera el missatge d'error per mostrar per pantalla
 * @author josep
 */
public class MissatgeError {
    
    // Variables
    private TipusError tipus;
    private int lina;
    private int columna;
    private String error;

    // Constructor
    public MissatgeError(TipusError tipus, int linia, int columna, String error) {
        this.tipus = tipus;
        this.lina = linia;
        this.columna = columna;
        this.error = error;
    }

    // toString
    public String toString(){
        return "ERROR DE TIPUS: " + tipus.name()
                + ", LINIA: " + lina 
                + ", COLUMNA: " + columna 
                + ", ERROR: "+error;
    }

    // getters
    public TipusError getTipus() {
        return tipus;
    }

    public int getLine() {
        return lina;
    }

    public int getColumna() {
        return columna;
    }

    public String getError() {
        return error;
    }
}
