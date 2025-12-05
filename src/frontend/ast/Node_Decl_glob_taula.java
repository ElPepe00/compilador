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
public class Node_Decl_glob_taula extends Node_Decl_glob {

    private String tipusBase;
    private String id;
    private Node_Num mida;
    private Node_DeclTailTaulaInt tailInt;
    private Node_DeclTailTaulaChar tailChar;
    private Node_DeclTailTaulaBool tailBool;
    
    public Node_Decl_glob_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaInt tail) {
        super();
        this.tipusBase = tipusBase;
        this.id = id;
        this.mida = mida;
        this.tailInt = tail;
    }
    
    public Node_Decl_glob_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaChar tail) {
        super();
        this.tipusBase = tipusBase;
        this.id = id;
        this.mida = mida;
        this.tailChar = tail;
    }
    
    public Node_Decl_glob_taula(String tipusBase, String id, Node_Num mida, Node_DeclTailTaulaBool tail) {
        super();
        this.tipusBase = tipusBase;
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
        
        TipusSimbol tArray = TipusUtils.tipusArrayDesdeNomBase(tipusBase);
        
        TipusSimbol tBase;
        
        switch (tipusBase) {
            case "INT": tBase = TipusSimbol.INT; break;
            case "CARACTER": tBase = TipusSimbol.CARACTER; break;
            case "BOOL": tBase = TipusSimbol.BOOL; break;
            default:
                throw new RuntimeException("Tipus base desconegut a taula global: " + tipusBase);
        }
        
        int midaElem = TipusUtils.midaBytesTipusBase(tBase);
        int midaTotal = nElems * midaElem;
        
        Simbol s = new Simbol(id, tArray, CategoriaSimbol.CONSTANT, 0, midaTotal);
        s.setGlobal(true);
        s.setArray(true);
        s.setMidaArray(nElems);
        
        TaulaSimbols.afegirSimbol(s);
        
        if (tipusBase.equals("INT") && tailInt != null) {
            tailInt.gestioSemantica(ts, nElems);
        } else if (tipusBase.equals("CARACTER") && tailChar != null) {
            tailChar.gestioSemantica(ts, nElems);
        } else if (tipusBase.equals("BOOL") && tailBool != null) {
            tailBool.gestioSemantica(ts, nElems);
        }
    }

    @Override
    public String toString() {
        return "Node_Decl_glob_taula(" + tipusBase + " " + id + "[" + mida + "]...)";
    }
    
    
    
}
