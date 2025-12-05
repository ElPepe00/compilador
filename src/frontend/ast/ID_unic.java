/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

/**
 * Classe que simula un contador global dels identificadors dels nodes de l'AST 
 * @author josep
 */
public class ID_unic {
    
    private static int contador = 0;
    
    // Mètode que retorna un valor més a l'anterior
    public static int next() {
        return contador++;
    }
}
