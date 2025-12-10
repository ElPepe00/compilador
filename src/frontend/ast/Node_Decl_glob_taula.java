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
 *
 * @author josep
 */
public class Node_Decl_glob_taula extends Node_Decl_glob {

    private String tipusBaseStr;
    private String id;
    private ArrayList<Integer> dimensions;
    private Node_DeclTailTaulaInt tailInt;
    private Node_DeclTailTaulaChar tailChar;
    private Node_DeclTailTaulaBool tailBool;
    
    private Simbol simbolArray;
    
    public Node_Decl_glob_taula(String tipusBase, String id, ArrayList<Integer> dims, Node_DeclTailTaulaInt tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.dimensions = dims;
        this.tailInt = tail;
    }
    
    public Node_Decl_glob_taula(String tipusBase, String id, ArrayList<Integer> dims, Node_DeclTailTaulaChar tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.dimensions = dims;
        this.tailChar = tail;
    }
    
    public Node_Decl_glob_taula(String tipusBase, String id, ArrayList<Integer> dims, Node_DeclTailTaulaBool tail) {
        super();
        this.tipusBaseStr = tipusBase;
        this.id = id;
        this.dimensions = dims;
        this.tailBool = tail;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        
        int nElems = 1;
        
        for(int d : dimensions) {
            if (d <= 0) {
                errorSemantic("Dimensió invàlida a '" + id + "': " + d);
                return;
            }
            nElems *= d;
        }
        
        TipusSimbol tArray = TipusUtils.getTipusArrayDesdeNomBase(tipusBaseStr);
        TipusSimbol tBase = TipusUtils.getTipusBaseDesdeNomBase(tipusBaseStr);
        
        int midaElem = tBase.getMidaBytes();
        int midaTotalBytes = nElems * midaElem;
        
        this.simbolArray = new Simbol(id, tArray, CategoriaSimbol.CONSTANT);
        this.simbolArray.setOcupacio(midaTotalBytes);
        this.simbolArray.setEsArray(true);
        this.simbolArray.setMidaArray(nElems);
        this.simbolArray.setDimensions(this.dimensions);
        
        this.simbolArray.setEsGlobal(true);
        this.simbolArray.setAmbit("GLOBAL");
        
        ts.afegirSimbol(this.simbolArray);

        if (tailInt != null) {
            if (tBase != TipusSimbol.INT) {
                errorSemantic("Inicialitzador INT en taula " + tipusBaseStr);
                return;
            }
            tailInt.gestioSemantica(ts, nElems);
            
        } else if (tailChar != null) {
            if (tBase != TipusSimbol.CARACTER) {
                errorSemantic("Inicialitzador CHAR en taula " + tipusBaseStr);
                return;
            }
            tailChar.gestioSemantica(ts, nElems);
            
        } else if (tailBool != null) {
            if (tBase != TipusSimbol.BOOL) {
                errorSemantic("Inicialitzador BOOL en taula " + tipusBaseStr);
                return;
            }
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
        return "Node_Decl_glob_taula(" + tipusBaseStr + " " + id + "[" + dimensions.size() + "]...)";
    }
    
    
    
}
