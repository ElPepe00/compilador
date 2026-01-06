/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.taula_simbols;

/**
 * Representa un paràmetre formal d'una funció/procediment.
 *
 * Es guarda dins el Symbol de la funció/proc (llista "parameters"),
 * i complementa la informació de "arguments" (que només guarda els tipus).
 *
 * Camps principals:
 *  - nom       : nom del paràmetre (id al codi)
 *  - tipus     : tipus del paràmetre (INT, BOOL, CHAR, ...)
 *  - posicio   : ordre dins la llista de paràmetres (0, 1, 2, ...)
 *  - mode      : VALOR / REFERENCIA (ara per ara, sempre VALOR)
 *  - offset    : desplaçament dins el frame d'activació (quan facis 3@)
 */
public class Parametre {

    private String nom;
    private TipusSimbol tipus;
    private int posicio;      // 0-based (primer paràmetre = 0)
    private int offset;       // per al codi de 3 adreces / frame

    public Parametre(String nom, TipusSimbol tipus, int posicio) {
        this(nom, tipus, posicio, 0);
    }

    /**
     * Crea un Paràmetre a partir dels valors passats per paràmetre
     */
    public Parametre(String nom,
                     TipusSimbol tipus,
                     int posicio,
                     int offset) {
        this.nom = nom;
        this.tipus = tipus;
        this.posicio = posicio;
        this.offset = offset;
    }

    // Getters i setters
    public String getNom() {
        return nom;
    }

    public TipusSimbol getTipus() {
        return tipus;
    }

    public int getPosicio() {
        return posicio;
    }

    public int getOffset() {
        return offset;
    }

    public void setTipus(TipusSimbol tipus) {
        this.tipus = tipus;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Parametre(nom:'" + nom + "', tsb:" + tipus + ", pos:" + posicio + ")";
    }
}
