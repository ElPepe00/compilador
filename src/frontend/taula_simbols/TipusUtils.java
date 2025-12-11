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

    public static TipusSimbol getTipusBaseDesdeNomBase(String base) {
        
        switch (base) {
            case "INT":  return TipusSimbol.INT;
            case "CARACTER": return TipusSimbol.CHAR;
            case "BOOL": return TipusSimbol.BOOL;
            default:
                throw new IllegalArgumentException("Tipus base desconegut: " + base);
        }
    }
    
    public static TipusSimbol getTipusArrayDesdeNomBase(String base) {
        
        switch (base) {
            case "INT":  return TipusSimbol.TAULA_INT;
            case "CARACTER": return TipusSimbol.TAULA_CARACTER;
            case "BOOL": return TipusSimbol.TAULA_BOOL;
            default:
                throw new IllegalArgumentException("Tipus base d'array desconegut: " + base);
        }
    }
    
    public static TipusSimbol getTipusBaseDeTipusArray(TipusSimbol tArray) {
        if (tArray == TipusSimbol.TAULA_INT) return TipusSimbol.INT;
        if (tArray == TipusSimbol.TAULA_BOOL) return TipusSimbol.BOOL;
        if (tArray == TipusSimbol.TAULA_CARACTER) return TipusSimbol.CHAR;
        
        return tArray; 
    }
    
}
