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
 * Classe que defineix el que serà la taula de procediments, una llista
 * de procediments
 * @author josep
 */
public class TaulaProcediments {
    
    private final ArrayList<Procediment> procediments = new ArrayList<>();
    
    public TaulaProcediments() {}
    
    public Procediment afegirProcediment(Procediment p) {
        procediments.add(p);
        return p;
    }
    
    public Procediment crearProcediment(String nom, TipusSimbol tipusRetorn, int nivell) {
        Procediment p = new Procediment(nom, tipusRetorn, nivell);
        procediments.add(p);
        return p;
    }
    
    public Procediment cercaProcediment(String nom) {
        
        for (Procediment p : procediments) {
            if (p.getNom().equals(nom)) {
                return p;
            }
        }
        return null;
    }
    
    public ArrayList<Procediment> getProcediments() {
        return procediments;
    }
    
    public void guardarTaulaProcediments(String nomFitxer) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(nomFitxer));
            
            bw.write("TAULA DE PROCEDIMENTS\n");
            bw.write("------------------\n");
            
            for (Procediment v : procediments) {
                bw.write(v.toString());
                bw.newLine();
            }
            
            System.out.println("   Taula de procediments guardada correctament.");
            bw.close();
            
        } catch (IOException e) {
            System.err.println("Error al guardar la taula de variables: " + e.getMessage());
        }
    }
}
