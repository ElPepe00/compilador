/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.codi_intermedi;

/**
 * Bloc específic d'una instrucció de Codi de 3 adreces
 * @author josep
 */
public class C3a_Instr {
    
    private Codi codi;       // Operació del C3A (ADD, SUB, COPY, GOTO, ...)
    private String arg1;     // Primer operand
    private String arg2;     // Segon operand
    private String desti;    // Destí o etiqueta associada a la instrucció
    private String etiqueta; // Etiqueta que precedeix la instrucció (si n'hi ha)
    
    /**
     * Crea una nova instrucció de C3A amb els camps indicats.
     */
    public C3a_Instr(String et, Codi codi, String arg1, String arg2, String desti) {
        this.codi = codi;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.desti = desti;
        
        this.etiqueta = et;
    }

    // Getters i setters
    public Codi getCodi() {
        return codi;
    }

    public void setCodi(Codi codi) {
        this.codi = codi;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getDesti() {
        return desti;
    }

    public void setDesti(String desti) {
        this.desti = desti;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    

    /**
     * Construeix una representació en text alineada de la instrucció,
     * adequada per a depuració o exportació del C3A.
     */
    @Override
    public String toString() {

        String labelStr;
        if (etiqueta != null && !etiqueta.isEmpty()) {
            labelStr = String.format("%-8s", etiqueta + ":");
        } else {
            labelStr = String.format("%-8s", "");
        }

        String opStr = (codi != null)
                ? String.format("%-8s", codi.name().toLowerCase())
                : String.format("%-8s", "");

        String a1 = (arg1 != null) ? arg1 : "-";
        String a2 = (arg2 != null) ? arg2 : "-";
        String d  = (desti  != null) ? desti  : "-";

        String a1Str = String.format("%-12s", a1);
        String a2Str = String.format("%-12s", a2);
        String rStr  = String.format("%-12s", d);

        return labelStr + opStr + a1Str + a2Str + rStr;
    }
}
