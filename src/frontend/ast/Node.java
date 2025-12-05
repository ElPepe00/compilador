/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import frontend.taula_simbols.*;


/**
 * Classe base abstracta per a tots els nodes de l'AST.
 *
 * Cada node té:
 *    - un nom lògic (nomNode), sobretot per a debug
 *    - opcionalment, informació de posició (linia, columna)
 *
 * El mètode gestioSemantica(SymbolTable ts) té una implementació
 * buida per defecte: els nodes que han de fer anàlisi semàntica
 * l'han de sobreescriure; la resta poden ignorar-lo.
 */
public abstract class Node {
    
    // Nom del tipus que te cada node per diferenciarlos
    protected String nomNode;
    
    protected int linia = -1;
    protected int columna = -1;
    
    // CONTRUCTOR: Crea un node sense valor i amb un nom
    public Node(String nomNode) {
        this.nomNode = nomNode;
    }

    public String getNomNode() {
        return nomNode;
    }

    public void setPosicio(int linia, int columna) {
        this.linia = linia;
        this.columna = columna;
    }

    public int getLinia() {
        return linia;
    }

    public int getColumna() {
        return columna;
    }

    /**
     * Hook per a l'anàlisi semàntica.
     * Per defecte NO fa res.
     *
     * Les subclasses que realment han de:
     *  · consultar la taula de símbols
     *  · afegir símbols
     *  · comprovar tipus
     * han de sobreescriure aquest mètode.
     */
    public void gestioSemantica(TaulaSimbols ts) {
        // Per defecte, no fa res.
        // Això evita errors de compilació en subclasses que
        // no necessiten lògica semàntica específica.
    }
    
    /**
     * Mètode que retorna el codi de 3 adreces en format string de cada node
     * @param codi3a
     * @return 
     */
    public String generaCodi3a(C3a codi3a) {
        return null;
    }

    @Override
    public String toString() {
        return nomNode;
    }

}
