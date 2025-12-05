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
    private static Stack<Hashtable<String, Simbol>> vars;
    
    // Pila de taules ampliada/completa (guarda tot l'historial dels Simbols)
    private static Stack<Hashtable<String, Simbol>> taulaFinal;
    
    
    // --- CONSTRUCTOR ---
    /**
     * Contructor de la taula de simbols
     */
    public TaulaSimbols() {
        vars = new Stack<>();
        taulaFinal = new Stack<>();
    }
    
    
    // --- MÈTODES GESTIO AMBITS ---
    
    /**
     * Crea un subambit i afegeix una nova taula hash a la pila
     */
    public static void entrarSubAmbit() {
        vars.push(new Hashtable<>());
        taulaFinal.push(new Hashtable<>());
    }
    
    /**
     * Elimina l'àmbit actual sortit del subambit
     * La taulaFinal no es surt del subambit
     */
    public static void sortirSubAmbit() {
        if (!vars.isEmpty()) {
            vars.pop();
        }
    }
    
    /**
     * Retorna el nivell actual d'ambit (0 és el primer creat)
     * @return 
     */
    public static int getNivellActual() {
        return vars.size() - 1;
    }
    
    
    // --- METODES GESTIO SIMBOLS ---
    
    /**
     * Afegeix un simbol a l'ambit actual passant l'objecte Simbol
     * @param simbol objecte que es vol inserir a la taula
     */
    public static void inserirSimbol(Simbol simbol) {
        
        if(simbolJaExisteixSubAmbitActual(simbol.getNom())) {
            throw new RuntimeException("Error inserirSimbol(): El simbol " + simbol.getNom() + " ja existeix.");
        }
        
        // Posam el simbol a la pila d'ambits
        vars.peek().put(simbol.getNom(), simbol);
        // Posam el simbol a la taula
        taulaFinal.peek().put(simbol.getNom(), simbol);
    }
    
    /**
     * Afegeix un simbol a l'ambit actual passant tots els parametres d'un simbol
     * @param nomS Nom del simbol (id)
     * @param tipusS Tipus del simbol (INT, BOOL, CARACTER, TAULA)
     * @param catS Categoria del simbol (VARIABLE, CONSTANT, FUNCIO...)
     * @param valor Valor associat per constants, altres 0
     * @param ocup Bytes que ocupa el simbol
     */
    public static void inserirSimbol(String nomS, TipusSimbol tipusS, CategoriaSimbol catS, int valor, int ocup) {
        
        Simbol simbol = new Simbol(nomS, tipusS, catS, valor, ocup);
        
        if(simbolJaExisteixSubAmbitActual(simbol.getNom())) {
            throw new RuntimeException("Error inserirSimbol(): El simbol " + simbol.getNom() + " ja existeix.");
        }
        
        // Posam el simbol a la pila d'ambits
        vars.peek().put(nomS, simbol);
        // Posam el simbol a la taula
        taulaFinal.peek().put(nomS, simbol);
    }
    
    /**
     * Cerca un simbol dins tots els ambits actius
     * De l'actual al global
     * @param nomSimbol nom del simbol a cercar
     * @return retorna el simbol si s'ha trobat sino null
     */
    public static Simbol cercaSimbol(String nomSimbol) {
        
        // Es recorre des del subàmbit més intern (top) fins al global (bottom)
        for (int i = vars.size() - 1; i >= 0; i--) {
            
            Hashtable<String, Simbol> ambit = vars.get(i);
            
            if (ambit.containsKey(nomSimbol)) {
                return ambit.get(nomSimbol);
            }
        }
        return null;
    }
    
    /**
     * Obte un simbol únicament de l'àmbit actual
     * @param nomSimbol Nom del simbol
     * @return retorna el simbol si es trobat
     */
    public static Simbol obtenirSimbolSubAmbitActual(String nomSimbol) {
        
        if (vars.isEmpty()) {
            return null;
        }
        
        return vars.peek().get(nomSimbol);
    }
    
    /**
     * Comprova si a l'ambit actual ja hiha un simbol amb aquest nom
     * @param nomSimbol nom del simbol a comprovar
     * @return retorna true si hiha un simbol amb el mateix nom
     */
    public static boolean simbolJaExisteixSubAmbitActual(String nomSimbol) {
        
        if (vars.isEmpty()) {
            return false;
        }
        
        Hashtable<String, Simbol> ambitActual = vars.peek();
        
        return ambitActual.containsKey(nomSimbol);
    }
    
    
    // --- FUNCIONS DE SUPORT ---

    /**
     * Retorna tots els símbols de tots els àmbits (històric complet),
     * útil per construir la "taula de variables" o la "taula de procediments".
     */
    public static List<Simbol> obtenirTotsElsSimbols() {
        List<Simbol> llista = new ArrayList<>();

        for (Hashtable<String, Simbol> scope : taulaFinal) {
            llista.addAll(scope.values());
        }
        return llista;
    }

    /**
     * Retorna tots els símbols d'una categoria concreta
     * (per exemple, totes les VARIABLES, tots els PROC/FUNCIO, etc.).
     */
    public static List<Simbol> obtenirSimbolsPerCategoria(CategoriaSimbol categoria) {
        List<Simbol> llista = new ArrayList<>();

        for (Hashtable<String, Simbol> scope : taulaFinal) {
            for (Simbol s : scope.values()) {
                if (s.getCategoria() == categoria) {
                    llista.add(s);
                }
            }
        }
        return llista;
    }
    
    // --- EXPORTACIÓ I VISUALITZACIÓ DE TAULA DE SIMBOLS ---
    
    /**
     * Guarda el fitxer de la taula de simbols
     * @param nomFitxer nom del fitxer de sortida
     */
    public static void guardarTaulaSimbols(String nomFitxer) {
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(nomFitxer));
            
            for (int i = 0; i < taulaFinal.size(); i++) {
                
                Hashtable<String, Simbol> ambitActual = taulaFinal.get(i);
                
                if (i == 0) {
                    bw.write("Ambit " + i + ": Global\n");
                } else {
                    bw.write("Ambit " + i + ":\n");
                }

                for (Simbol s : ambitActual.values()) {
                    bw.write("  " + s.toString() + "\n");
                }
            }
            
            System.out.println("   Taula de Simbols guardada correctament.");
            bw.close();
            
        } catch (IOException e) {
            System.err.println("Error al guardar el fitxer de la taula de simbols: " + e);
        }
    }

    // Mètode toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int nivellAmbit = vars.size();
        
        for (int i = vars.size() - 1; i >= 0; i--) {
            
            Hashtable<String, Simbol> ambitActual = vars.get(i);
            sb.append("Ambit ").append(nivellAmbit - i - 1).append(":\n");
            
            for (Simbol s : ambitActual.values()) {
                sb.append("  ").append(s.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
