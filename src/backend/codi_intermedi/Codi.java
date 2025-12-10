/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.codi_intermedi;

/**
 *
 * @author josep
 */
public enum Codi {
    
    COPY,
    ADD,
    SUB,
    PROD,
    DIV,
    NEG,
    
    AND,
    OR,
    NOT,
    
    IND_VAL,    // ind_val base index dest -> dest = base[index]
    IND_ASS,    // ind_ass value index base -> base[index] = value
    
    SKIP,       // etiqueta
    GOTO,       // goto etiqueta
    IF_EQ,
    IF_NE,
    IF_LT,
    IF_GT,
    
    PMB,
    CALL,
    RET,
    
    PARAM_S,    // param_s -> a
    PARAM_C,    // param_c -> idx base
    
    HALT        // aturada de programa
}
