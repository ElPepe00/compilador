/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.taula_simbols;

import java.util.*;

/**
 * Classe que representa un Simbol que s'emmagatzemara a la pila de la taula
 * de simbols.
 * 
 * @author josep
 */
public class Simbol {
    
    private String nomSimbol;                   // nom del simbol
    private TipusSimbol tipus;                  // INT, BOOL, CHAR, TAULA_INT...
    private CategoriaSimbol categoria;          // VARIABLE, CONSTANT, PARAMENTRE, FUNCIO...
    private int valor;                          // per constants o informacio extra
    private int ocupacio;                       // bytes: 4 int/bool, 1 char, N*4 int array...

    // Paràmetres formals de les funcions o procediments
    private ArrayList<Parametre> parametres;
    // Tipus d'arguments (per comprovar crides)
    private ArrayList<TipusSimbol> arguments;
    // Indica si una variable s'ha emprat a una assignacio o no
    private boolean assignacio;
    
    // Camps per le codi intermedi C3@
    private int offset;                         // offset dins el frame
    private boolean esGlobal;                   // true si declarat al nivell global
    private boolean esArray;                    // true si és taula
    private int midaArray;                      // nombre d'elements (no bytes)
    private String etiqueta;                    // label per a salt (ex: "f_main")

    
    // --- CONSTRUCTOR ---
    /**
     * Constructor d'un simbol que se li passen tots els paràmetres necessaris
     * @param nomSimbol
     * @param tipus
     * @param categoria
     * @param valor
     * @param ocupacio 
     */
    public Simbol(String nomSimbol, TipusSimbol tipus, CategoriaSimbol categoria, int valor, int ocupacio) {
        this.nomSimbol = nomSimbol;
        this.tipus = tipus;
        this.categoria = categoria;
        this.valor = valor;
        this.ocupacio = ocupacio;
        this.parametres = new ArrayList<>();
        this.arguments = new ArrayList<>();
        this.assignacio = false;
        
        this.offset = 0;
        this.esGlobal = false;
        this.esArray = false;
        this.midaArray = 0;
        this.etiqueta = null;
    }

    // --- METODES GETTER I SETTER ---
    public String getNom() {
        return nomSimbol;
    }
    
    public TipusSimbol getTipus() {
        return tipus;
    }

    public CategoriaSimbol getCategoria() {
        return categoria;
    }

    public int getValor() {
        return valor;
    }
    
    public int getOcupacio() {
        return ocupacio;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public boolean isGlobal() {
        return esGlobal;
    }

    public boolean isArray() {
        return esArray;
    }
    
    public int getMidaArray() {
        return midaArray;
    }   
    
    public ArrayList<TipusSimbol> getArguments() {
        return arguments;
    }
    
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }
    
    public boolean getAssigacio() {
        return assignacio;
    }
    
    public String getEtiqueta() {
        return etiqueta;
    }
    
    
    //---
    
    public void setNom(String nomSimbol) {
        this.nomSimbol = nomSimbol;
    }
        
    public void setTipus(TipusSimbol tipus) {
        this.tipus = tipus;
    }
    
    public void setCategoria(CategoriaSimbol categoria) {
        this.categoria = categoria;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public void setOcupacio(int ocupacio) {
        this.ocupacio = ocupacio;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setGlobal(boolean esGlobal) {
        this.esGlobal = esGlobal;
    }
    
    public void setArray(boolean esArray) {
        this.esArray = esArray;
    }

    public void setMidaArray(int midaArray) {
        this.midaArray = midaArray;
    }
    
    public void setArguments(ArrayList<TipusSimbol> arguments) {
        this.arguments = arguments;
    }

    public void setParametres(ArrayList<Parametre> param) {
        this.parametres = param;
    }
    
    public void setAssignacio(boolean b) {
        this.assignacio = b;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // toString
    @Override
    public String toString() {
        return String.format(
        "%s [tipus=%s, cat=%s, valor=%s, ocupacio=%s%s%s%s%s]",
        nomSimbol,
        tipus,
        categoria,
        valor,
        ocupacio,
        esGlobal ? ", global" : "",
        esArray  ? String.format(", array(mida=%d)", midaArray) : "",
        offset != 0 ? String.format(", offset=%d", offset) : "",
        !parametres.isEmpty() ? String.format(", params=%s", parametres) : "",
        etiqueta != null ? String.format(", etq=%s", etiqueta) : ""
        );
    }
}
