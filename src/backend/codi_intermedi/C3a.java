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
    
    private TaulaVariables tv;
    private TaulaProcediments tp;
    
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
        this.tv = new TaulaVariables();
        this.tp = new TaulaProcediments();
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
        
        blocs.get(index).setDesti(nouArg1);
    }
    
    // Modificar un Arg2 (no es sol modificar)
    public void modificaArg2(int index, String nouArg2) {
        
        if (index < 0 || index >= blocs.size()) {
            throw new IllegalArgumentException("Index de bloc fora de rang: " + index);
        }
        
        blocs.get(index).setDesti(nouArg2);
    }
    
    public ArrayList<C3a_Instr> getCodi() {
        return blocs;
    }
    
    public int getNumInstr() {
        return blocs.size();
    }

    // ------------------------------
    // Accés a TV i TP
    // ------------------------------

    public TaulaVariables getTaulaVariables() {
        return tv;
    }

    public TaulaProcediments getTaulaProcediments() {
        return tp;
    }

    // ------------------------------
    // API per a procediments (TP)
    // ------------------------------

    public void iniciarProcediment(String nom, TipusSimbol tipusRetorn, int nivell) {
        procActual = new Procediment(nom, tipusRetorn, nivell);
        procActual.setInstrInici(getNumInstr());
        procActual.setEtiquetaEntrada(nom.equals("main") ? "main" : "f_" + nom);

        offsetActual = 0;
        posicioParamActual = 0;

        // Afegim entrada a TP
        tp.afegirProcediment(procActual);

        // Etiqueta d'entrada
        afegirEtiqueta(procActual.getEtiquetaEntrada());
        // Preàmbul (pmb nom)
        afegir(Codi.PMB, nom, null, null);

        // Marcam l'índex de la primera variable d'aquest procediment a TV
        procActual.setIndexPrimeraVar(tv.getVariables().size());
    }

    public void acabarProcediment() {
        if (procActual == null) return;

        // Fi de procediment: rtn nom
        afegir(Codi.RET, procActual.getNom(), null, null);

        procActual.setInstrFi(getNumInstr() - 1);
        procActual.setNumVariables(tv.getVariables().size() - procActual.getIndexPrimeraVar());
        procActual.setMidaFrame(offsetActual);

        procActual = null;
        offsetActual = 0;
        posicioParamActual = 0;
    }

    // ------------------------------
    // API per a variables (TV)
    // Aquestes funcions les cridaran els nodes de declaració
    // ------------------------------

    public Variable registrarParametre(String nom, TipusSimbol tipus, boolean esTaula, int numElements, int midaBytes) {
        
        if (procActual == null) throw new IllegalStateException("No hi ha procActual");

        Variable ev = new Variable(nom, procActual.getNom(), tipus, true, posicioParamActual, esTaula, numElements, midaBytes, offsetActual);
        tv.afegirVariable(ev);

        procActual.afegirParametre(tipus);
        posicioParamActual++;

        offsetActual += midaBytes;
        return ev;
    }

    public Variable registrarLocal(String nom, TipusSimbol tipus, boolean esTaula, int numElements, int midaBytes) {
        
        if (procActual == null) throw new IllegalStateException("No hi ha procActual");

        Variable ev = new Variable(nom, procActual.getNom(), tipus, false, -1, esTaula, numElements, midaBytes, offsetActual);
        tv.afegirVariable(ev);

        offsetActual += midaBytes;
        return ev;
    }

    public Variable registrarGlobal(String nom, TipusSimbol tipus, boolean esTaula, int numElements, int midaBytes, int offsetGlobal) {
        // Globals: nomProc = "GLOBAL"
        Variable ev = new Variable(nom, "GLOBAL", tipus,false, -1, esTaula, numElements, midaBytes, offsetGlobal);
        tv.afegirVariable(ev);
        return ev;
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
            
            System.out.println("   Codi de 3 Adreces (C3@) guardat correctament.");
            bw.close();
            
        } catch (IOException e) {
            System.err.println("Error al guardar el C3@ al fitxer: " + e.getMessage());
        }
    }
    
    
    
}
