/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.optimitzacio;

import backend.codi_intermedi.C3a;
import backend.codi_intermedi.C3a_Instr;
import backend.codi_intermedi.Codi;
import java.util.ArrayList;

/**
 * Aplica optimitzacions de mireta locals sobre el Codi de 3 Adreces.
 * Modifica directament la llista d'instruccions del C3a.
 */
public class Optimitzador {

    private final C3a c3a;      // Codi de 3 adreces
    private boolean hiHaCanvis; // Control per a passades iteratives

    // CONSTRUCTOR
    public Optimitzador(C3a c3a) {
        this.c3a = c3a;
    }

    /**
     * Mètode principal que executa les optimitzacions fins que no es poden
     * aplicar més canvis
     */
    public void optimitzar() {
        System.out.println("   > Iniciant optimitzacio ...");
        int instruccionsEliminades = 0;
        
        do {
            hiHaCanvis = false;
            instruccionsEliminades += aplicarPassada();
        } while (hiHaCanvis);

        System.out.println("   > Optimitzacio finalitzada. Instruccions eliminades: " + instruccionsEliminades);
    }

    /**
     * Recorre la llista d'instruccions i cerca patrons de 2 línies.
     * Retorna el nombre d'instruccions eliminades
     */
    private int aplicarPassada() {
        ArrayList<C3a_Instr> instrs = c3a.getBlocs();
        int eliminades = 0;

        // Iterem fins a size-2 perque miram parelles d'instruccions (i, i+1)
        for (int i = 0; i < instrs.size() - 1; i++) {
            C3a_Instr actual = instrs.get(i);
            C3a_Instr seguent = instrs.get(i + 1);

            // -----------------------------------------------------------------
            // OPTIMITZACIÓ 1: Eliminació de Còpies Redundants (CONST/VAR -> TEMP -> VAR)
            // Patró:
            //    i:   COPY A  -  tX
            //    i+1: COPY tX -  B
            // Resultat:
            //    i:   COPY A  -  B
            //    (i+1 eliminada)
            // -----------------------------------------------------------------
            if (actual.getCodi() == Codi.COPY && seguent.getCodi() == Codi.COPY) {
                String temp = actual.getDesti();
                String srcSeguent = seguent.getArg1();

                // Verificam que el desti de la PRIMERA es l'origen de la SEGONA
                // i és un temporal
                if (esTemporal(temp) && temp.equals(srcSeguent)) {
                    
                    // No podem eliminar la segona instrucció si té una etiqueta
                    if (senseEtiqueta(seguent)) {
                        // El desti final passa a la instruccio actual
                        actual.setDesti(seguent.getDesti());
                        
                        // Eliminam la instruccio següent
                        instrs.remove(i + 1);
                        eliminades++;
                        hiHaCanvis = true;
                        
                        i--; 
                        continue;
                    }
                }
            }

            // -----------------------------------------------------------------
            // OPTIMITZACIÓ 2: Propagació de Còpies (VAR -> TEMP -> ÚS)
            // Patró:
            //    i:   COPY A  -  tX
            //    i+1: OP   tX ...  (o PARAM tX)
            // Resultat:
            //    i+1: OP   A ...
            //    (i eliminada si tX mor aquí)
            // -----------------------------------------------------------------
            if (actual.getCodi() == Codi.COPY && esTemporal(actual.getDesti())) {
                String valOriginal = actual.getArg1();
                String temp = actual.getDesti();

                // Comprovem si la següent instrucció utilitza 'temp' com a primer argument
                if (seguent.getArg1() != null && seguent.getArg1().equals(temp)) {
                    
                    // Comprovam que no sigui etiqueta
                    if (senseEtiqueta(actual)) {
                        
                        // Posam a la instruccio següent el valor orgininal
                        seguent.setArg1(valOriginal);
                        
                        // Eliminam la instruccio
                        instrs.remove(i);
                        eliminades++;
                        hiHaCanvis = true;
                        
                        i--;
                        continue;
                    }
                }
            }
        }
        return eliminades;
    }

    // Funcions auxiliars
    // Comprovam si es un temporal o no
    private boolean esTemporal(String s) {
        return s != null && s.startsWith("t") && s.length() > 1 && Character.isDigit(s.charAt(1));
    }

    // Comprova si hiha etiqueta o no
    private boolean senseEtiqueta(C3a_Instr instr) {
        return instr.getEtiqueta() == null || instr.getEtiqueta().isEmpty();
    }
}