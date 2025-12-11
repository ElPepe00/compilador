/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.codi_intermedi;

import frontend.taula_simbols.TipusSimbol;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Gestor global del Codi de 3 Adreces
 * @author josep
 */
public class C3a {
    
    // Llista d'instruccions
    private ArrayList<C3a_Instr> blocs;

    
    // Comptador de variables temporals i etiquetes
    private int tempCont;
    private int etiquetaCont;
    
    private Procediment procActual = null;
    private int offsetActual = 0;
    private int posicioParamActual = 0;
    
    public C3a () {
        this.blocs = new ArrayList<>();
        this.tempCont = 0;
        this.etiquetaCont = 0;
    }
    
    public ArrayList<C3a_Instr> getBlocs() {
        return blocs;
    }
    
    public int getNumBlocs() {
        return blocs.size();
    }
    
    public int nextIndexBloc() {
        return blocs.size();
    }
    
    
    // Generador de variables temporals
    public String novaTemp() {
        String t = "t" + tempCont;
        tempCont++;
        return t;
    }
    
    // Generador d'etiquetes
    public String novaEtiqueta() {
        String e = "e" + etiquetaCont;
        etiquetaCont++;
        return e;
    }
    
    //Afegir bloc a la llista de blocs i retorna la posicio on s'ha inserit
    public int afegir(Codi codi, String arg1, String arg2, String desti) {
        C3a_Instr b = new C3a_Instr(null, codi, arg1, arg2, desti);
        blocs.add(b);
        return blocs.size() - 1;
    }
    
    public int afegir(String et, Codi codi, String arg1, String arg2, String desti) {
        C3a_Instr b = new C3a_Instr(et, codi, arg1, arg2, desti);
        blocs.add(b);
        return blocs.size() - 1;
    }
    
    // Emissio d'una etiqueta com a bloc separat
    public int afegirEtiqueta(String et) {
        return afegir(Codi.SKIP, null, null, et);
    }
    
    // Modificar un Desti (per salts)
    public void modificaDesti(int index, String nouDesti) {
        
        if (index < 0 || index >= blocs.size()) {
            throw new IllegalArgumentException("Index de bloc fora de rang: " + index);
        }
        
        blocs.get(index).setDesti(nouDesti);
    }
    
    // Modificar un Arg1 (per backpatching)
    public void modificaArg1(int index, String nouArg1) {
        
        if (index < 0 || index >= blocs.size()) {
            throw new IllegalArgumentException("Index de bloc fora de rang: " + index);
        }
        
        blocs.get(index).setArg1(nouArg1);
    }
    
    // Modificar un Arg2 (no es sol modificar)
    public void modificaArg2(int index, String nouArg2) {
        
        if (index < 0 || index >= blocs.size()) {
            throw new IllegalArgumentException("Index de bloc fora de rang: " + index);
        }
        
        blocs.get(index).setArg2(nouArg2);
    }
    
    public int getNumInstr() {
        return blocs.size();
    }
    
    // Mètode toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        
        for (C3a_Instr b : blocs) {
            sb.append(String.format("%4d: ", i));
            sb.append(b.toString());
            sb.append("\n");
            i++;
        }
        
        return sb.toString();
    }
    
    // Exportar codi de 3 adreces
    public void guadarCodi3a(String nomFitxer) {
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(nomFitxer));
            int i = 0;
            
            for (C3a_Instr b : blocs) {
                bw.write(String.format("%4d: %s", i, b.toString()));
                bw.newLine();
                i++;
            }
            
            System.out.println("   > Codi de 3 Adreces (C3@) guardat correctament.");
            bw.close();
            
        } catch (IOException e) {
            System.err.println("Error al guardar el C3@ al fitxer: " + e.getMessage());
        }
    }
    
    
    
}
