/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.taula_simbols;

/**
 * Enumerat del tipus base dels simbols que tenim al llenguatge
 * @author josep
 */
public enum TipusSimbol {

    INT, BOOL, CHAR, 
    TAULA_INT, TAULA_BOOL, TAULA_CARACTER,
    VOID, //per funcions sense return
    NULL,
    ERROR;


    public int getMidaBytes() {
        switch (this) {
            case INT:
            case BOOL:
                return 4;
                
            case CHAR:
                return 1;
            
            case TAULA_INT, TAULA_CARACTER, TAULA_BOOL:
            case VOID:
       
            default:
                return 0;
        }
    }
}
