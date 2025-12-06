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
 * Declaracio local d'un array (taula)
 * @author josep
 */
public class Node_Decl_loc_taula extends Node_Decl_loc {

    private String tipusBaseStr;
    private String id;
    private Node_Num midaNode;
    
    private Node_DeclTailTaulaInt tailInt;
    private Node_DeclTailTaulaChar tailChar;
    private Node_DeclTailTaulaBool tailBool;
    
    private Simbol simbolArray;
    
    // --- CONSTRUCTORS ---
    public Node_Decl_loc_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaInt tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.midaNode = mida;
        this.tailInt = tail;
    }
    
    public Node_Decl_loc_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaChar tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.midaNode = mida;
        this.tailChar = tail;
    }
    
    public Node_Decl_loc_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaBool tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.midaNode = mida;
        this.tailBool = tail;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        // 1. Calcular la mida total en bytes
        int nElems = midaNode.getValorEnter();
        
        if (nElems <= 0) {
            throw new RuntimeException("Mida de taula no vàlida per '" + id + "': " + nElems);
        }
        
        TipusSimbol tArray = TipusUtils.getTipusArrayDesdeNomBase(tipusBaseStr);
        TipusSimbol tBase = TipusUtils.getTipusBaseDesdeNomBase(tipusBaseStr);
        
        // 1. Calcul de la mida total
        int midaElem = tBase.getMidaBytes();
        int midaTotal = nElems * midaElem;
        
        // 2. Cream el simbol
        this.simbolArray = new Simbol(id, tArray, CategoriaSimbol.CONSTANT);
        this.simbolArray.setOcupacio(midaTotal);
        this.simbolArray.setEsArray(true);
        this.simbolArray.setMidaArray(nElems);
        
        // 3. Afegim el simbol a la TS
        ts.afegirSimbol(this.simbolArray);
        
        if (tipusBaseStr.equals("INT") && tailInt != null) {
            tailInt.gestioSemantica(ts, nElems);
        } else if (tipusBaseStr.equals("CARACTER") && tailChar != null) {
            tailChar.gestioSemantica(ts, nElems);
        } else if (tipusBaseStr.equals("BOOL") && tailBool != null) {
            tailBool.gestioSemantica(ts, nElems);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        /*
        TODO
        */
        
        return null;
        
    }

    @Override
    public String toString() {
        return "Node_Decl_loc_taula(" + tipusBaseStr + " " + id + "[" + midaNode + "]...)";
    }
    
    
}
