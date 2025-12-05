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
    
    // Informacio basica
    private String nom;                         // nom del simbol
    private TipusSimbol tipus;                  // INT, BOOL, CHAR, TAULA_INT...
    private CategoriaSimbol categoria;          // VARIABLE, CONSTANT, PARAMENTRE, FUNCIO...
    
    // Informacio per estructura 
    private int valor;                          // per constants o informacio extra
    private int ocupacio;                       // bytes: 4 int/bool, 1 char, N*4 int array...

    // Per arrays
    private boolean esArray;                    // boolea que determina si el simbol es una taula
    private int midaArray;                      // nombre d'elements (no bytes)
    
    // Per funcions o procediments
    private ArrayList<Parametre> llistaParametres;  // llista detallada dels parametres
    private ArrayList<TipusSimbol> arguments;       // domes per els tipus, per comprovar crides
    
    
    // --- DADES PER C3@ ---
    
    // Per variables i parametres
    private int offset;                         // direccio relativa dins la pila
    private String ambit;                       // nom del procediment al que pertany
    private boolean esGlobal;                   // Si es global
    private int posicioParam;                   // la posicio del parametre que ocupa

    // Per funcions i procediments
    private int midaFrame;                      // tamany total de les variables locals
    private String etiqueta;                    // nom de l'etiqueta
    private int instrInici;                     // numero d'instruccio d'inici (optimitzacio, opcional)
    
    // --- CONSTRUCTOR ---
    /**
     * Constructor d'un simbol que se li passen tots els paràmetres necessaris
     * @param nomSimbol
     * @param tipus
     * @param categoria
     */
    public Simbol(String nomSimbol, TipusSimbol tipus, CategoriaSimbol categoria) {
        this.nom = nomSimbol;
        this.tipus = tipus;
        this.categoria = categoria;
        this.llistaParametres = new ArrayList<>();
        this.arguments = new ArrayList<>();
        this.ambit = "GLOBAL"; //per defecte
    }

    // --- METODES GETTER I SETTER ---
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TipusSimbol getTipus() {
        return tipus;
    }

    public void setTipus(TipusSimbol tipus) {
        this.tipus = tipus;
    }

    public CategoriaSimbol getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaSimbol categoria) {
        this.categoria = categoria;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getOcupacio() {
        return ocupacio;
    }

    public void setOcupacio(int ocupacio) {
        this.ocupacio = ocupacio;
    }

    public boolean isEsArray() {
        return esArray;
    }

    public void setEsArray(boolean esArray) {
        this.esArray = esArray;
    }

    public int getMidaArray() {
        return midaArray;
    }

    public void setMidaArray(int midaArray) {
        this.midaArray = midaArray;
    }

    public ArrayList<Parametre> getLlistaParametres() {
        return llistaParametres;
    }

    public void setLlistaParametres(ArrayList<Parametre> llistaParametres) {
        this.llistaParametres = llistaParametres;
    }

    public ArrayList<TipusSimbol> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<TipusSimbol> arguments) {
        this.arguments = arguments;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getAmbit() {
        return ambit;
    }

    public void setAmbit(String ambit) {
        this.ambit = ambit;
    }

    public boolean isEsGlobal() {
        return esGlobal;
    }

    public void setEsGlobal(boolean esGlobal) {
        this.esGlobal = esGlobal;
    }

    public int getPosicioParam() {
        return posicioParam;
    }

    public void setPosicioParam(int posicioParam) {
        this.posicioParam = posicioParam;
    }

    public int getMidaFrame() {
        return midaFrame;
    }

    public void setMidaFrame(int midaFrame) {
        this.midaFrame = midaFrame;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public int getInstrInici() {
        return instrInici;
    }

    public void setInstrInici(int instrInici) {
        this.instrInici = instrInici;
    }
    
    // Métodes per accedir a les llistes
    public void addParametre(Parametre p) {
        this.llistaParametres.add(p);
    }
    
    public void addArgument(TipusSimbol t) {
        this.arguments.add(t);
    }

    // --- MÈTODE toString() ---
    @Override
    public String toString() {
        return String.format("%s [tipus=%s, cat=%s | offset=%s, ambit=%s]",
        nom,
        tipus,
        categoria,
        offset,
        ambit
        );
    }
}
