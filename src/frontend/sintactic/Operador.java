/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.sintactic;

/**
 * Operadors possibles a les expressions del llenguatge
 * @author josep
 */
public enum Operador {
    SUMA, RESTA, MULT, DIV,
    AND, OR,
    IGUAL, NOIGUAL, MENOR, MAJOR,
    NOT, UMINUS,
    REF, NUM, CHAR, BOOL, CRIDA, PAREN
}