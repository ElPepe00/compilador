/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import backend.codi_intermedi.Codi;
import frontend.taula_simbols.*;

/**
 *
 * @author josep
 */
public class Node_DeclTailEscalar extends Node {

    private String mode;        //assign o llegir
    private Node_Express expr;  // nomes si es mode assign
    
    public Node_DeclTailEscalar(String mode, Node_Express expr) {
        super("DeclTailEscalar");
        this.mode = mode;
        this.expr = expr;
    }
    
    public String getMode() {
        return mode;
    }
    
    public Node_Express getExpr() {
        return expr;
    }
 
    public void gestioSemantica(TaulaSimbols ts, TipusSimbol tipusEsperat) {
        
        if (mode == null) {
            return;
        }
        
        if (mode.equals("assign")) {
            TipusSimbol tExpr = expr.getTipusSimbol(ts);
            
            if (tExpr != tipusEsperat) {
                errorSemantic("Error de tipus a inicialitzacio local. Esperat: "
                + tipusEsperat + ", s'ha trobat: " + tExpr);
                return;
            }
            
        } else if (mode.equals("llegir")) {
            if (tipusEsperat != TipusSimbol.INT &&
                tipusEsperat != TipusSimbol.CARACTER &&
                tipusEsperat != TipusSimbol.BOOL) {
                
                errorSemantic("No es pot llegir() a tipus " + tipusEsperat);
                return;
            }
        }
    }
    
    public String generaCodiInicialitzacio(C3a codi3a, String nomVar) {
        
        // Cas expressio
        if (mode.equals("assign")) {
            String s = expr.generaCodi3a(codi3a);
            codi3a.afegir(Codi.COPY, s, null, nomVar);
        
        // Cas llegir()
        } else if (mode.equals("llegir")) {
            String s = codi3a.novaTemp();
            codi3a.afegir(Codi.CALL, "llegir", null, s);
            codi3a.afegir(Codi.COPY, s, null, nomVar);
        }
        
        return null;
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        return null;
    }
    
    
}
