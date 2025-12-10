/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package backend.assemblador;

import java.io.*;
import java.util.*;
import frontend.taula_simbols.*;
import backend.codi_intermedi.*;

/**
 * Classe que genera el codi assemblador a partir del codi de 3 adreces
 * @author josep
 */
public class GeneradorAssemblador {
    
    private C3a c3a;
    private TaulaSimbols ts;
    private StringBuilder asmb;
    
    private HashMap<String, Integer> offsetTemporals;
    private int offsetActualTemporals;
    
    public GeneradorAssemblador (C3a c3a, TaulaSimbols ts) {
        this.c3a = c3a;
        this.ts = ts;
        this.asmb = new StringBuilder();
        this.offsetTemporals = new HashMap<>();
    }
    
    public void generarCodiAssemblador(String rutaSortida) {
        
        // 1. CAPÇALERA Easy68k
        asmb.append("\tORG\t$1000\n");          // començam a la pos 1000 de memoria
        asmb.append("START:\n");
        asmb.append("\tLEA\tstack_end, A7\n");  // inicialitzam stack pointer
        asmb.append("\tJSR\tmain\n");           // botam al main
        asmb.append("\tSIMHALT\n\n");           // aturam si torna
        
        // 2. Processar instruccions
        for (C3a_Instr instr : c3a.getBlocs()) {
            traduirIntruccio(instr);
        }
        
        // 3. Generar les globals
        generarGlobals();
        
        // 4. Zona de pila
        asmb.append("\n\tORG\t$8000\n");
        asmb.append("stack_start:  DS.B\t4000\n");
        asmb.append("stack_end:\n");
        asmb.append("\tEND\tSTART\n");
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(rutaSortida));
            bw.write(asmb.toString());
            System.out.println("   > Codi assembler generat correctament.");
            
        } catch (IOException e) {
            System.err.println("Error al guardar el fitxer d'assemblador: " + e.getMessage());
        }
    }

    private void traduirIntruccio(C3a_Instr i) {
        
        //Posam etiqueta si en te
        if (i.getEtiqueta() != null && !i.getEtiqueta().isEmpty()) {
            asmb.append(i.getEtiqueta()).append(":\n");
        }
        
        asmb.append("\t; ").append(i.toString()).append("\n"); // Comentari debug
        
        switch (i.getCodi()) {
            case PMB:
                //Preambul funcio, hem de reservar espais per les temporals
                int midaLocals = Integer.parseInt(i.getArg2());
                int midaTotal = midaLocals + 128;
                
                if (!i.getArg1().equals(i.getEtiqueta())) { 
                    asmb.append(i.getArg1()).append(":\n");
                }
                
                asmb.append("\tLINK A6, #-").append(midaTotal).append("\n");
                
                //reiniciam la gestio dels temporals per la funcio en questio
                offsetTemporals.clear();
                offsetActualTemporals = - midaLocals - 4;
                break;
                
            case RET:
                asmb.append("\t UNLK A6\n");
                asmb.append("\tRTS\n");
                
            case CALL:
                asmb.append("\tJSR ").append(i.getArg1()).append("\n");
                
                if (i.getDesti() != null) {
                    // si la funcio retorna valor (d0), el guardam al desti
                    String desti = getOperand(i.getDesti());
                    asmb.append("\tMOVE.L D0, ").append(desti).append("\n");
                }
                
                //per simplicitat el Frame Pointer gestiona tota la neteja de parametres
                break;
                
            case PARAM_S:
                String opParam = getOperand(i.getArg1());
                asmb.append("\tMOVE.L ").append(opParam).append(", -(A7)\n");
                break;
                
            case COPY:
                String src = getOperand(i.getArg1());
                String dst = getOperand(i.getDesti());
                asmb.append("\tMOVE.L ").append(src).append(", D0\n");
                asmb.append("\tMOVE.L D0, ").append(dst).append("\n");
                break;

            case ADD: operacioBinaria("ADD.L", i); break;
            case SUB: operacioBinaria("SUB.L", i); break;
            case PROD: operacioBinaria("MULS", i); break;
            case DIV: operacioBinaria("DIVS", i); break;
            
            case AND: operacioBinaria("AND.L", i); break;
            case OR:  operacioBinaria("OR.L", i); break;

            case GOTO:
                asmb.append("\tBRA ").append(i.getDesti()).append("\n");
                break;
            
            case IF_EQ: saltCondicional("BEQ", i); break;
            case IF_NE: saltCondicional("BNE", i); break;
            case IF_GT: saltCondicional("BGT", i); break;
            case IF_LT: saltCondicional("BLT", i); break;

            // --- ARRAYS ---
            case IND_VAL:
                // t = vector[index]
                // 1. Carregar adreça base array a A0
                carregarAdrecaBase(i.getArg1(), "A0");
                // 2. Carregar índex a D0
                asmb.append("\tMOVE.L ").append(getOperand(i.getArg2())).append(", D0\n");
                // 3. Multiplicar per 4 (mida int) -> LSL #2 es mes rapid que MULS #4
                asmb.append("\tLSL.L #2, D0\n");
                // 4. Llegir: 0(A0, D0) -> D1
                asmb.append("\tMOVE.L 0(A0, D0.L), D1\n");
                // 5. Guardar a destí
                asmb.append("\tMOVE.L D1, ").append(getOperand(i.getDesti())).append("\n");
                break;

            case IND_ASS:
                // vector[index] = valor
                // 1. Carregar adreça base array a A0
                carregarAdrecaBase(i.getDesti(), "A0");
                // 2. Carregar índex a D0
                asmb.append("\tMOVE.L ").append(getOperand(i.getArg2())).append(", D0\n");
                asmb.append("\tLSL.L #2, D0\n");
                // 3. Carregar valor a D1
                asmb.append("\tMOVE.L ").append(getOperand(i.getArg1())).append(", D1\n");
                // 4. Escriure: D1 -> 0(A0, D0)
                asmb.append("\tMOVE.L D1, 0(A0, D0.L)\n");
                break;

            // --- SISTEMA ---
            case HALT:
                asmb.append("\tSIMHALT\n");
                break;
                
            default:
                // Tractament especial per al "print" si es detecta com a CALL
                if (i.getCodi() == Codi.CALL && i.getArg1().equals("imprimir")) {
                    // Assumim que l'argument ja s'ha fet push abans (param_s)
                    // Easy68k trap #15 task 3 imprimeix nombre en D1
                    asmb.append("\tMOVE.L (A7)+, D1\n"); // Pop argument a D1
                    asmb.append("\tMOVE.L #3, D0\n");    // Tasca 3: Print Signed Decimal
                    asmb.append("\tTRAP #15\n");
                    // Nova línia (opcional)
                    asmb.append("\tMOVE.L #13, D0\n");   // CR
                    asmb.append("\tTRAP #15\n");
                    asmb.append("\tMOVE.L #10, D0\n");   // LF
                    asmb.append("\tTRAP #15\n");
                } 
                else if (i.getCodi() == Codi.CALL && (i.getArg1().equals("llegir"))) {
                    // Llegir nombre: Tasca 4
                    asmb.append("\tMOVE.L #4, D0\n");
                    asmb.append("\tTRAP #15\n");
                    // El resultat queda a D0
                    if (i.getDesti() != null) {
                        asmb.append("\tMOVE.L D0, ").append(getOperand(i.getDesti())).append("\n");
                    }
                }
                break;
        }
    }

    private void generarGlobals() {
        asmb.append("\n    ; --- GLOBALS ---\n");
        for (Simbol s : ts.obtenirTotsElsSimbols()) { 
            if (s.isEsGlobal() && s.getCategoria() != CategoriaSimbol.FUNCIO && s.getCategoria() != CategoriaSimbol.PROCEDIMENT) {
                // Declarar espai: var_x DS.B 4
                // Easy68k usa DS.B (bytes), DS.W (words), DS.L (longs)
                asmb.append("var_").append(s.getNom()).append(":\tDS.B ").append(s.getOcupacio()).append("\n");
            }
        }
    }

    /**
     * Converteix un operand C3A ("t1", "x", "10") a operand 68k ("-4(A6)", "var_x", "#10")
     */
    private String getOperand(String op) {
        if (op == null) return "#0";

        // 1. Literal numèric
        if (op.matches("-?\\d+")) {
            return "#" + op;
        }

        // 2. Variable declarada (Taula Símbols)
        Simbol s = ts.cercarSimbol(op);
        if (s != null) {
            if (s.isEsGlobal()) {
                return "var_" + op;
            } else {
                return s.getOffset() + "(A6)";
            }
        }

        // 3. Temporal (t0, t1...) - No està a la TS
        if (op.startsWith("t") && op.length() > 1) {
            if (!offsetTemporals.containsKey(op)) {
                // Assignar nou offset
                offsetTemporals.put(op, offsetActualTemporals);
                offsetActualTemporals -= 4; // Creixem cap avall
            }
            return offsetTemporals.get(op) + "(A6)";
        }

        // Per defecte (etiquetes, etc.)
        return op;
    }

    private void operacioBinaria(String instrAsmb, C3a_Instr i) {
        asmb.append("\tMOVE.L ").append(getOperand(i.getArg1())).append(", D0\n");
        asmb.append("\t").append(instrAsmb).append(" ").append(getOperand(i.getArg2())).append(", D0\n");
        asmb.append("\tMOVE.L D0, ").append(getOperand(i.getDesti())).append("\n");
    }

    private void saltCondicional(String branch, C3a_Instr i) {
        // CMP op2, op1  (atenció a l'ordre en 68k: CMP a, b compara b-a)
        // Però per C3A: if op1 == op2.
        asmb.append("\tMOVE.L ").append(getOperand(i.getArg1())).append(", D0\n");
        asmb.append("\tCMP.L  ").append(getOperand(i.getArg2())).append(", D0\n");
        asmb.append("\t").append(branch).append(" ").append(i.getDesti()).append("\n");
    }

    private void carregarAdrecaBase(String nomVar, String regAdress) {
        
        Simbol s = ts.cercarSimbol(nomVar);
        
        if (s != null) {
            if (s.isEsGlobal()) {
                // Global: LEA var_nom, A0
                asmb.append("\tLEA var_").append(nomVar).append(", ").append(regAdress).append("\n");
            } else {
                // Local: LEA offset(A6), A0
                asmb.append("\tLEA ").append(s.getOffset()).append("(A6), ").append(regAdress).append("\n");
            }
        } else {
            // Error o temporal (no hauria de ser array)
            asmb.append("\t; ERROR: Array ").append(nomVar).append(" no trobat\n");
        }
    }
}
