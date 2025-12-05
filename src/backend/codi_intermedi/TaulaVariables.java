/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.codi_intermedi;

import frontend.taula_simbols.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Clase que defineix el que es una taula de variables
 * @author josep
 */
public class TaulaVariables {
    
    private final ArrayList<Variable> variables = new ArrayList<>();
    private int nv = 0;
    
    
    public Variable afegirVariable(Variable v) {
        variables.add(v);
        return v;
    }
    
    public Variable afegirVariable(String nom, String nomProc, TipusSimbol tipus, boolean esParametre, int posicioParam, boolean esTaula, int numElems, int midaBytes, int offset) {
        
        Variable v = new Variable(nom, nomProc, tipus, esParametre, posicioParam, esTaula, numElems, midaBytes, offset);
        variables.add(v);
        nv++;
        return v;
    }
    
    public ArrayList<Variable> getVariables() {
        return variables;
    }
    
    public Variable cercaVariable(String nom, String nomProc) {
        
        for (Variable v : variables) {
            if (v.getNom().equals(nom) && v.getNomProc().equals(nomProc)) {
                return v;
            }
        }
        return null;
    }
    
    public ArrayList<Variable> getVariablesDeProc(String nomProc) {
        ArrayList<Variable> res = new ArrayList<>();
        
        for (Variable v : variables) {
            if (v.getNomProc().equals(nomProc)) {
                res.add(v);
            }
        }
        
        return res;
    }
    
    public void guardarTaulaVariables(String nomFitxer) {
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(nomFitxer));
            
            bw.write("TAULA DE VARIABLES\n");
            bw.write("------------------\n");
            
            for (Variable v : variables) {
                bw.write(v.toString());
                bw.newLine();
            }
            
            System.out.println("   Taula de variables guardada correctament.");
            bw.close();
            
        } catch (IOException e) {
            System.err.println("Error al guardar la taula de variables: " + e.getMessage());
        }
    }
    
}
