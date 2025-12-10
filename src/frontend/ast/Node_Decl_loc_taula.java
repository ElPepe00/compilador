/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import frontend.taula_simbols.*;
import java.util.ArrayList;

/**
 * Declaracio local d'un array (taula)
 * @author josep
 */
public class Node_Decl_loc_taula extends Node_Decl_loc {

    private String tipusBaseStr;
    private String id;
    private ArrayList<Integer> dimensions;
    
    private Node_DeclTailTaulaInt tailInt;
    private Node_DeclTailTaulaChar tailChar;
    private Node_DeclTailTaulaBool tailBool;
    
    private Simbol simbolArray;
    
    // --- CONSTRUCTORS ---
    public Node_Decl_loc_taula(String tipusBase, String id, ArrayList<Integer> dims, Node_DeclTailTaulaInt tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.dimensions = dims;
        this.tailInt = tail;
    }
    
    public Node_Decl_loc_taula(String tipusBase, String id, ArrayList<Integer> dims, Node_DeclTailTaulaChar tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.dimensions = dims;
        this.tailChar = tail;
    }
    
    public Node_Decl_loc_taula(String tipusBase, String id, ArrayList<Integer> dims, Node_DeclTailTaulaBool tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.dimensions = dims;
        this.tailBool = tail;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        // 1. Calcular la mida total en bytes
        int nElems = 1;
        
        for(int d : dimensions) {
            if (d <= 0) {
                errorSemantic("Dimensio d'array invalida: " + d + ". Ha de ser > 0");
                return;
            }
            nElems *= d;
        }
        
        TipusSimbol tArray = TipusUtils.getTipusArrayDesdeNomBase(tipusBaseStr);
        TipusSimbol tBase = TipusUtils.getTipusBaseDesdeNomBase(tipusBaseStr);
        
        // 1. Calcul de la mida total
        int midaElem = tBase.getMidaBytes();
        int midaTotalBytes = nElems * midaElem;
        
        // 2. Cream el simbol
        this.simbolArray = new Simbol(id, tArray, CategoriaSimbol.VARIABLE);
        this.simbolArray.setOcupacio(midaTotalBytes);
        this.simbolArray.setEsArray(true);
        this.simbolArray.setMidaArray(nElems);
        this.simbolArray.setDimensions(this.dimensions);
        
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
        
        if (tailInt != null) {
            tailInt.generaCodiInicialitzacio(codi3a, id);
        } else if (tailChar != null) {
            tailChar.generaCodiInicialitzacio(codi3a, id);
        } else if (tailBool != null) {
            tailBool.generaCodiInicialitzacio(codi3a, id);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Node_Decl_loc_taula(" + tipusBaseStr + " " + id + "[" + dimensions.size() + "]...)";
    }
    
    
}
