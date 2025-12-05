/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frontend.taula_simbols;

/**
 *
 * @author josep
 */
public class TipusUtils {
    
    public static int midaBytesTipusBase(TipusSimbol t) {
        
        switch (t) {
            case INT:
            case BOOL:
                return 4;
            case CARACTER:
                return 1;
                
            default:
                throw new IllegalArgumentException("Tipus base no valid per mida: " + t);
        }
    }
    
    public static TipusSimbol tipusArrayDesdeNomBase(String base) {
        
        switch (base) {
            case "int":  return TipusSimbol.TAULA_INT;
            case "char": return TipusSimbol.TAULA_CARACTER;
            case "bool": return TipusSimbol.TAULA_BOOL;
            default:
                throw new IllegalArgumentException("Tipus base d'array desconegut: " + base);
        }
    }
    
}
