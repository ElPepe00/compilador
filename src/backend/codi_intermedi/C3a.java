/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.codi_intermedi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Gestor global del Codi de 3 Adreces (C3A): emmagatzema instruccions, genera temporals/etiquetes
 * i permet operacions de backpatching (modificació d'operands/destins).
 */
public class C3a {

    /** Llista d'instruccions (blocs) del C3A en ordre d'emissió. */
    private ArrayList<C3a_Instr> blocs;

    /** Comptadors per generar identificadors únics de temporals i etiquetes. */
    private int tempCont;
    private int etiquetaCont;

    /**
     * Inicialitza un C3A buit amb comptadors a zero.
     */
    public C3a() {
        this.blocs = new ArrayList<>();
        this.tempCont = 0;
        this.etiquetaCont = 0;
    }

    /**
     * Retorna la llista d'instruccions del C3A.
     */
    public ArrayList<C3a_Instr> getBlocs() {
        return blocs;
    }

    /**
     * Retorna el nombre de blocs/instruccions actuals.
     */
    public int getNumBlocs() {
        return blocs.size();
    }

    /**
     * Retorna l'índex on s'inserirà la pròxima instrucció (equivalent a size()).
     */
    public int nextIndexBloc() {
        return blocs.size();
    }

    /**
     * Genera i retorna el nom d'una nova variable temporal única (t0, t1, ...).
     */
    public String novaTemp() {
        String t = "t" + tempCont;
        tempCont++;
        return t;
    }

    /**
     * Genera i retorna el nom d'una nova etiqueta única (e0, e1, ...).
     */
    public String novaEtiqueta() {
        String e = "e" + etiquetaCont;
        etiquetaCont++;
        return e;
    }

    /**
     * Afegeix una instrucció al C3A sense etiqueta i en retorna la posició d'inserció.
     */
    public int afegir(Codi codi, String arg1, String arg2, String desti) {
        C3a_Instr b = new C3a_Instr(null, codi, arg1, arg2, desti);
        blocs.add(b);
        return blocs.size() - 1;
    }

    /**
     * Afegeix una instrucció al C3A amb etiqueta i en retorna la posició d'inserció.
     */
    public int afegir(String et, Codi codi, String arg1, String arg2, String desti) {
        C3a_Instr b = new C3a_Instr(et, codi, arg1, arg2, desti);
        blocs.add(b);
        return blocs.size() - 1;
    }

    /**
     * Emiteix una etiqueta com a instrucció independent (SKIP amb destí = etiqueta) i en retorna la posició.
     */
    public int afegirEtiqueta(String et) {
        return afegir(Codi.SKIP, null, null, et);
    }

    /**
     * Modifica el destí d'una instrucció (típic per completar salts un cop es coneix l'etiqueta).
     */
    public void modificaDesti(int index, String nouDesti) {
        if (index < 0 || index >= blocs.size()) {
            throw new IllegalArgumentException("Index de bloc fora de rang: " + index);
        }
        blocs.get(index).setDesti(nouDesti);
    }

    /**
     * Modifica l'operand arg1 d'una instrucció (típic en backpatching d'operands).
     */
    public void modificaArg1(int index, String nouArg1) {
        if (index < 0 || index >= blocs.size()) {
            throw new IllegalArgumentException("Index de bloc fora de rang: " + index);
        }
        blocs.get(index).setArg1(nouArg1);
    }

    /**
     * Modifica l'operand arg2 d'una instrucció (ús menys habitual, però disponible).
     */
    public void modificaArg2(int index, String nouArg2) {
        if (index < 0 || index >= blocs.size()) {
            throw new IllegalArgumentException("Index de bloc fora de rang: " + index);
        }
        blocs.get(index).setArg2(nouArg2);
    }

    /**
     * Retorna el nombre total d'instruccions (sinònim de getNumBlocs()).
     */
    public int getNumInstr() {
        return blocs.size();
    }

    /**
     * Construeix una representació en text del C3A, numerant cada instrucció amb el seu índex.
     */
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

    /**
     * Exporta el C3A a un fitxer de text, una instrucció per línia, amb numeració.
     */
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
