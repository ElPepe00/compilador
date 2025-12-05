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
 *  - Gestiona àmbits amb una pila de taules (vars).
 *  - Manté un registre global de tots els simbols declarats (taulaFinal).
 *    Ens servirà per generar el fitxer de la taula de simbols.
 * 
 * Perquè funcioni s'ha de cridar entrarSubAmbit() una vegada al principi (Ambit global)
 * abans d'afegir simbols.
 */
public class TaulaSimbols {
    
    // Pila de taules per a cada ambit local (ambits actius)
    private static Stack<HashMap<String, Simbol>> pilaAmbits;
    
    // Pila per controlar l'offset actual de cada ambit
    // (el top de la pila és l'offset disponible actual)
    private static Stack<Integer> pilaOffsets;
    
    // Llista completa de tots els simbols introduits a la TS
    // (guarda tot l'historial dels Simbols)
    private static ArrayList<Simbol> taulaCompleta;
    
    
    // --- CONSTRUCTOR ---
    /**
     * Contructor de la taula de simbols
     */
    public TaulaSimbols() {
        pilaAmbits = new Stack<>();
        pilaOffsets = new Stack<>();
        taulaCompleta = new ArrayList<>();
    }
    
    
    // --- MÈTODES GESTIO AMBITS ---
    
    /**
     * Crea un subambit i afegeix una nova taula hash a la pila
     */
    public static void entrarBloc() {
        pilaAmbits.push(new HashMap<>());
        
        // Si es un bloc anonim seguim amb l'offset anterior, si es una funció
        // hem de resetear
        int offsetActual = pilaOffsets.isEmpty() ? 0 : pilaOffsets.peek();
        pilaOffsets.push(offsetActual);
    }
    
    /**
     * Elimina l'àmbit actual sortit del subambit
     */
    public static void sortirBloc() {
        if (!pilaAmbits.isEmpty()) {
            pilaAmbits.pop();
        }
        
        // Quan sortim del bloc recuperam el tamany total empleat (opcional)
        int midaFinalBloc = pilaOffsets.pop();
    }

    
    // --- METODES GESTIO SIMBOLS ---
    
    /**
     * Afegeix un simbol a l'ambit actual passant l'objecte Simbol
     * @param simbol objecte que es vol inserir a la taula
     */
    public static void afegirSimbol(Simbol simbol) {
        
        if(pilaAmbits.peek().containsKey(simbol.getNom())) {
            throw new RuntimeException("Error inserirSimbol(): El simbol " + simbol.getNom() + " ja existeix a n'aquest ambit.");
        }
        
        if (simbol.getCategoria() == CategoriaSimbol.VARIABLE 
            || simbol.getCategoria() == CategoriaSimbol.PARAMETRE) {
            
            // Obtenim l'offset actual i l'assignam
            int actualOffset = pilaOffsets.pop();
            simbol.setOffset(actualOffset);
            
            // Calculam el següent offset disponible i el guardam
            int nouOffset = actualOffset + simbol.getOcupacio();
            pilaOffsets.push(nouOffset);
            
            //Guardam informacio extra
            simbol.setAmbit(getNivellActual() == 0 ? "GLOBAL" : "LOCAL");
            simbol.setEsGlobal(getNivellActual() == 0);
        }
        
        // Guardam
        pilaAmbits.peek().put(simbol.getNom(), simbol);
        taulaCompleta.add(simbol);
    }
    
    /**
     * Cerca un simbol dins tots els ambits actius
     * De l'actual al global
     * @param nomSimbol nom del simbol a cercar
     * @return retorna el simbol si s'ha trobat sino null
     */
    public static Simbol cercarSimbol(String nom) {
        
        // Es recorre des del subàmbit més intern (top) fins al global (bottom)
        for (int i = pilaAmbits.size() - 1; i >= 0; i--) {
            
            if (pilaAmbits.get(i).containsKey(nom)) {
                return pilaAmbits.get(i).get(nom);
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
     * @return 
     */
    public static int getNivellActual() {
        return pilaAmbits.size() - 1;
    }
    
    /**
     * Retorna el tamany d'offset actual
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
    
    
    /**
     * Guarda el fitxer de la taula de simbols
     * @param nomFitxer nom del fitxer de sortida
     */
    public void guardarTaulaSimbols(String nomFitxer) {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomFitxer))) {
            bw.write(this.toString());
            System.out.println("   > Taula de Símbols guardada a: " + nomFitxer);
        } catch (IOException e) {
            System.err.println("Error guardant Taula de Símbols: " + e.getMessage());
        }
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
                if (s.getCategoria() == CategoriaSimbol.VARIABLE || 
                    s.getCategoria() == CategoriaSimbol.PARAMETRE) {
                    
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
     * Genera el fitxer específic de PROCEDIMENTS (per al Backend)
     * Substitueix l'antiga 'TaulaProcediments.guardar...'
     */
    public void exportarTaulaProcediments(String rutaFitxer) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFitxer))) {
            bw.write("TAULA DE PROCEDIMENTS (Generada des de TS)\n");
            bw.write("------------------------------------------\n");
            bw.write(String.format("%-15s %-15s %-10s %-15s\n", "ETIQUETA", "NOM", "FRAME(B)", "PARAMS"));

            for (Simbol s : taulaCompleta) {
                if (s.getCategoria() == CategoriaSimbol.FUNCIO || 
                    s.getCategoria() == CategoriaSimbol.PROCEDIMENT) {
                    
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
        if (taulaCompleta.isEmpty()) return "Taula de Símbols buida.";

        StringBuilder sb = new StringBuilder();
        sb.append("=== TAULA DE SÍMBOLS ===\n");

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
}
