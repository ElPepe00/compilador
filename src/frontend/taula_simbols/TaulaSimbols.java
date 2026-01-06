/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.taula_simbols;

import frontend.taula_simbols.Simbol;
import java.io.*;
import java.util.*;

/**
 * Classe que representa la taula de simbols que empra el compilador.
 *
 * - Gestiona àmbits amb una pila de taules (vars). - Manté un registre global
 * de tots els simbols declarats (taulaFinal). Ens servirà per generar el fitxer
 * de la taula de simbols.
 *
 * Perquè funcioni s'ha de cridar entrarSubAmbit() una vegada al principi (Ambit
 * global) abans d'afegir simbols.
 */
public class TaulaSimbols {

    // Pila de taules per a cada ambit local (ambits actius)
    private Stack<HashMap<String, Simbol>> pilaAmbits;

    // Pila per controlar l'offset actual de cada ambit
    // (el top de la pila és l'offset disponible actual)
    private Stack<Integer> pilaOffsets;

    // Control del procediment actual per assignar la midaFrame al sortir
    private Stack<Simbol> pilaProcediments;

    // Llista completa de tots els simbols introduits a la TS
    // (guarda tot l'historial dels Simbols)
    private ArrayList<Simbol> taulaCompleta;

    // --- CONSTRUCTOR ---
    /**
     * Contructor de la taula de simbols
     */
    public TaulaSimbols() {
        pilaAmbits = new Stack<>();
        pilaOffsets = new Stack<>();
        pilaProcediments = new Stack<>();
        taulaCompleta = new ArrayList<>();

        //cream ambit global
        pilaAmbits.push(new HashMap<>());
        pilaOffsets.push(0); //offset global comença a 0
    }

    // --- MÈTODES GESTIO AMBITS ---
    /**
     * Crea un nou ambit Si aquest subambit es una funcio, li hem de pasasr el
     * Simbol per l'offset
     */
    public void entrarBloc(Simbol procAssociat) {
        pilaAmbits.push(new HashMap<>());
        
        if (procAssociat != null) {
            // Bloc de tipus funció, offset a zero i nou marco de pila
            pilaOffsets.push(0);
            pilaProcediments.push(procAssociat);
            
        } else {
            // Bloc de tipus (if, while), mantenim l'offset si en te
            int offsetActual = pilaOffsets.isEmpty() ? 0 : pilaOffsets.peek();
            pilaOffsets.push(offsetActual);
            pilaProcediments.push(null);
        }
    }

    /**
     * Crea un nou ambit, per blocs anonims
     */
    public void entrarBloc() {
        entrarBloc(null);
    }

    /**
     * Elimina l'àmbit actual sortit del subambit
     */
    public void sortirBloc() {

        if (pilaAmbits.isEmpty()) {
            return;
        }

        // Obtenim ocupacio total de l'ambit actual
        int midaLocals = pilaOffsets.peek();

        // Si estem dins un procediment, guardam la informacio a un simbol
        // ens serveix per la generacio de codi
        if (!pilaProcediments.isEmpty()) {
            Simbol proc = pilaProcediments.pop();
            if (proc != null) {
                proc.setMidaFrame(Math.max(proc.getMidaFrame(), midaLocals));
            }
        }

        // Eliminam l'ambit antes de sortir
        pilaAmbits.pop();
        pilaOffsets.pop();
    }

    // --- METODES GESTIO SIMBOLS ---
    /**
     * Afegeix un simbol a l'ambit actual passant l'objecte Simbol
     *
     * @param simbol objecte que es vol inserir a la taula
     * @return true si s'ha afegit el simbol
     */
    public boolean afegirSimbol(Simbol s) {

        if (pilaAmbits.isEmpty()) {
            return false;
        }

        HashMap<String, Simbol> taulaActual = pilaAmbits.peek();

        if (taulaActual.containsKey(s.getNom())) {
            return false; // Error: símbol ja existent en aquest àmbit
        }

        // Posar nom a l'ambit
        String nomAmbit = "GLOBAL";
        if (!pilaProcediments.isEmpty()) {
            // Cerquem el procediment més proper (ignorant blocs anònims nulls)
            for (int i = pilaProcediments.size() - 1; i >= 0; i--) {
                Simbol proc = pilaProcediments.get(i);
                if (proc != null) {
                    nomAmbit = proc.getNom();
                    break;
                }
            }
        }
        s.setAmbit(nomAmbit);
        s.setEsGlobal("GLOBAL".equals(nomAmbit)); // Marquem si és global o no
        
        // Calcul de l'offset
        if (s.getCategoria() == CategoriaSimbol.VARIABLE
                || s.getCategoria() == CategoriaSimbol.PARAMETRE) {

            int offsetActual = pilaOffsets.pop(); // Recuperem l'offset disponible

            // Assignem l'offset al símbol
            s.setOffset(offsetActual);

            // Calculem quant ocupa aquest símbol
            int ocupacio = 0;
            if (s.isEsArray()) {
                // Si és array: MidaTipusBase * NombreElements
                ocupacio = s.getTipus().getMidaBytes() * s.getMidaArray();
            } else {
                // Si és escalar
                ocupacio = s.getTipus().getMidaBytes();
            }

            s.setOcupacio(ocupacio); // Guardem ocupació també per si de cas

            // Actualitzem l'offset per a la següent variable
            pilaOffsets.push(offsetActual + ocupacio);

        } else {
            // Si és funció, const, etc., no consumeix espai de pila local en aquest moment
            // (Les constants solen anar directes o a zona de dades, funcions a codi)
        }

        // Afegim a l'àmbit actual i a la llista global
        taulaActual.put(s.getNom(), s);
        taulaCompleta.add(s);

        return true;
    }

    /**
     * Cerca un simbol dins tots els ambits actius De l'actual al global
     *
     * @param nom nom del simbol a cercar
     * @return retorna el simbol si s'ha trobat sino null
     */
    public Simbol cercarSimbol(String nom) {

        // Es recorre des del subàmbit més intern (top) fins al global (bottom)
        for (int i = pilaAmbits.size() - 1; i >= 0; i--) {

            if (pilaAmbits.get(i).containsKey(nom)) {
                return pilaAmbits.get(i).get(nom);
            }
        }
        return null;
    }

    /**
     * Cerca un simbol dins l'ambit actual
     *
     * @param nom nom del simbol a cercar
     * @return retorna el simbol si hi es
     */
    public Simbol cercarSimbolLocal(String nom) {
        if (pilaAmbits.isEmpty()) {
            return null;
        }
        return pilaAmbits.peek().get(nom);
    }

    /**
     * Cerca un símbol pel seu àmbit i nom dins de la taula completa. Es fa
     * servir sobretot des del Backend (generació de codi) per mapar noms de C3@
     * a info de la taula de símbols.
     *
     * @param ambit nom de l'àmbit (p. ex. "GLOBAL", "main", "func1", ...)
     * @param nom nom del símbol
     * @return el símbol trobat o null si no existeix
     */
    public Simbol cercarSimbolAmbit(String ambit, String nom) {

        if (taulaCompleta == null || nom == null) {
            return null;
        }

        for (Simbol s : taulaCompleta) {

            // Primer comprovam el nom
            if (!nom.equals(s.getNom())) {
                continue;
            }

            String ambitSimbol = s.getAmbit();
            boolean mateixAmbit;

            if (ambit == null) {
                // Si no especificam àmbit, només acceptam els que també el tenguin null
                mateixAmbit = (ambitSimbol == null);
            } else {
                // Per evitar problemes "GLOBAL" vs "global", ho feim case-insensitive
                mateixAmbit = ambitSimbol != null && ambit.equalsIgnoreCase(ambitSimbol);
            }

            if (mateixAmbit) {
                return s;
            }
        }

        return null;
    }

    // Método especial para iniciar una función (resetea el offset)
    public void entrarFuncio() {
        pilaAmbits.push(new HashMap<>());
        pilaOffsets.push(0); // El offset local empieza en 0 para cada función
    }

    /**
     * Retorna el nivell actual d'ambit (0 és el primer creat)
     *
     * @return
     */
    public int getNivellActual() {
        return pilaAmbits.size() - 1;
    }

    /**
     * Retorna el tamany d'offset actual
     *
     * @return
     */
    public int getOffsetActual() {
        return pilaOffsets.peek();
    }

    // --- EXPORTACIÓ I VISUALITZACIÓ DE TAULA DE SIMBOLS ---
    public ArrayList<Simbol> getTotesLesVariables() {
        ArrayList<Simbol> vars = new ArrayList<>();
        for (Simbol s : taulaCompleta) {
            if (s.getCategoria() == CategoriaSimbol.VARIABLE || s.getCategoria() == CategoriaSimbol.PARAMETRE) {
                vars.add(s);
            }
        }
        return vars;
    }

    public ArrayList<Simbol> getTotsElsProcediments() {
        ArrayList<Simbol> procs = new ArrayList<>();
        for (Simbol s : taulaCompleta) {
            if (s.getCategoria() == CategoriaSimbol.FUNCIO || s.getCategoria() == CategoriaSimbol.PROCEDIMENT) {
                procs.add(s);
            }
        }
        return procs;
    }

    public ArrayList<Simbol> getTaulaCompleta() {
        return taulaCompleta;
    }
    

    /**
     * Guarda el fitxer de la taula de simbols
     *
     * @param rutaSortida nom del fitxer de sortida
     */
    public void guardarTaulaSimbols(String rutaSortida) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSortida))) {
            writer.write(mostrarTaulaSimbols());
            System.out.println("   > Taula de Simbols guardada a: " + rutaSortida);
        } catch (IOException e) {
            System.err.println("Error guardant la taula de símbols: " + e.getMessage());
        }

    }

    /**
     * Mètode que retorna la taula de simbols en format String
     *
     * @return retorna la taula de simbols pintada
     */
    public String mostrarTaulaSimbols() {

        if (taulaCompleta.isEmpty()) {
            return "Taula de Símbols buida.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== TAULA DE SÍMBOLS ===\n");

        // Agrupem per àmbit per visualitzar millor
        Map<String, List<Simbol>> agrupats = new LinkedHashMap<>();

        for (Simbol s : taulaCompleta) {
            String ambit = s.getAmbit();
            agrupats.putIfAbsent(ambit, new ArrayList<>());
            agrupats.get(ambit).add(s);
        }

        for (Map.Entry<String, List<Simbol>> entrada : agrupats.entrySet()) {
            sb.append("\nÀMBIT: ").append(entrada.getKey()).append("\n");
            sb.append("----------------------------------------------------------------------\n");
            sb.append(String.format("%-15s %-15s %-15s %-8s %-8s\n", "NOM", "CATEGORIA", "TIPUS", "OFFSET", "MIDA_FR"));

            for (Simbol s : entrada.getValue()) {
                sb.append(String.format("%-15s %-15s %-15s %-8d %-8d\n",
                        s.getNom(),
                        s.getCategoria(),
                        s.getTipus(),
                        s.getOffset(),
                        s.getMidaFrame() // Visualitzem la mida del frame si és funció
                ));
            }
        }
        return sb.toString();
    }

    /**
     * Genera el fitxer específic de VARIABLES (per al Backend / C3@)
     * Substitueix l'antiga 'TaulaVariables.guardar...'
     */
    public void exportarTaulaVariables(String rutaFitxer) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFitxer))) {
            bw.write("TAULA DE VARIABLES (Generada des de TS)\n");
            bw.write("---------------------------------------\n");
            bw.write(String.format("%-15s %-15s %-10s %-10s %-10s\n", "NOM", "AMBIT", "OFFSET", "MIDA", "TIPUS"));

            for (Simbol s : taulaCompleta) {
                if (s.getCategoria() == CategoriaSimbol.VARIABLE
                        || s.getCategoria() == CategoriaSimbol.PARAMETRE) {

                    bw.write(String.format("%-15s %-15s %-10d %-10d %-10s",
                            s.getNom(),
                            s.getAmbit(),
                            s.getOffset(),
                            s.getOcupacio(),
                            s.getTipus()));
                    bw.newLine();
                }
            }
            System.out.println("   > Taula de Variables guardada a: " + rutaFitxer);
        } catch (IOException e) {
            System.err.println("Error exportant Variables: " + e.getMessage());
        }
    }

    /**
     * Genera el fitxer específic de PROCEDIMENTS (per al Backend) Substitueix
     * l'antiga 'TaulaProcediments.guardar...'
     */
    public void exportarTaulaProcediments(String rutaFitxer) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFitxer))) {
            bw.write("TAULA DE PROCEDIMENTS (Generada des de TS)\n");
            bw.write("------------------------------------------\n");
            bw.write(String.format("%-15s %-15s %-10s %-15s\n", "ETIQUETA", "NOM", "FRAME(B)", "PARAMS"));

            for (Simbol s : taulaCompleta) {
                if (s.getCategoria() == CategoriaSimbol.FUNCIO
                        || s.getCategoria() == CategoriaSimbol.PROCEDIMENT) {

                    String params = s.getLlistaParametres().toString(); // O formatar-ho millor si vols

                    bw.write(String.format("%-15s %-15s %-10d %-15s",
                            s.getEtiqueta(),
                            s.getNom(),
                            s.getMidaFrame(),
                            params));
                    bw.newLine();
                }
            }
            System.out.println("   > Taula de Procediments guardada a: " + rutaFitxer);
        } catch (IOException e) {
            System.err.println("Error exportant Procediments: " + e.getMessage());
        }
    }

    // Mètode toString
    @Override
    public String toString() {
        if (taulaCompleta.isEmpty()) {
            return "Taula de Símbols buida.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== TAULA DE SIMBOLS ===\n");

        // Usamos un Map para agrupar visualmente por ámbitos
        // LinkedHashMap mantiene el orden de inserción (Global primero, luego funciones...)
        Map<String, List<Simbol>> agrupats = new LinkedHashMap<>();

        for (Simbol s : taulaCompleta) {
            String ambit = s.getAmbit();
            agrupats.putIfAbsent(ambit, new ArrayList<>());
            agrupats.get(ambit).add(s);
        }

        // Generamos el string iterando los grupos
        for (Map.Entry<String, List<Simbol>> entrada : agrupats.entrySet()) {
            sb.append("\nÀMBIT: ").append(entrada.getKey()).append("\n");
            sb.append("----------------------------------------------------\n");
            // Cabecera de columnas opcional
            sb.append(String.format("%-15s %-15s %-15s %-10s\n", "NOM", "CATEGORIA", "TIPUS", "OFFSET"));

            for (Simbol s : entrada.getValue()) {
                sb.append(String.format("%-15s %-15s %-15s %-10d\n",
                        s.getNom(),
                        s.getCategoria(),
                        s.getTipus(),
                        s.getOffset()));
            }
        }
        return sb.toString();
    }

    /**
     * Mètode que retorna tots els simbols de la taula completa
     * @return 
     */
    public ArrayList<Simbol> obtenirTotsElsSimbols() {
        return this.taulaCompleta;
    }
}
