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
 *
 * @author josep
 */
public class Node_Decl_glob_taula extends Node_Decl_glob {

    private String tipusBaseStr;
    private String id;
    private Node_Num mida;
    private Node_DeclTailTaulaInt tailInt;
    private Node_DeclTailTaulaChar tailChar;
    private Node_DeclTailTaulaBool tailBool;
    
    private Simbol simbolArray;
    
    public Node_Decl_glob_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaInt tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.mida = mida;
        this.tailInt = tail;
    }
    
    public Node_Decl_glob_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaChar tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.mida = mida;
        this.tailChar = tail;
    }
    
    public Node_Decl_glob_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaBool tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.mida = mida;
        this.tailBool = tail;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        int nElems = mida.getValorEnter();
        
        if (nElems <= 0) {
            throw new RuntimeException("Mida de taula no vàlida per '" + id + "': " + nElems);
        }
        
        TipusSimbol tArray = TipusUtils.getTipusArrayDesdeNomBase(tipusBaseStr);
        TipusSimbol tBase = TipusUtils.getTipusBaseDesdeNomBase(tipusBaseStr);
        
        int midaElem = tBase.getMidaBytes();
        int midaTotal = nElems * midaElem;
        
        this.simbolArray = new Simbol(id, tArray, CategoriaSimbol.CONSTANT);
        this.simbolArray.setEsGlobal(true);
        this.simbolArray.setEsArray(true);
        this.simbolArray.setMidaArray(nElems);
        this.simbolArray.setOcupacio(midaTotal);
        
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
        return super.generaCodi3a(codi3a); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public String toString() {
        return "Node_Decl_glob_taula(" + tipusBaseStr + " " + id + "[" + mida + "]...)";
    }
    
    
    
}
