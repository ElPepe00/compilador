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

public class GeneradorAssemblador {

    private final C3a c3a;
    private final TaulaSimbols ts;
    private String ambitActual = "global"; 
    private int bytesParametresAcumulats = 0;

    public GeneradorAssemblador(C3a c3a, TaulaSimbols ts) {
        this.c3a = c3a;
        this.ts = ts;
    }

    public void generaFitxer(String nomFitxer) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomFitxer))) {
            escriuCapcalera(bw);
            escriuSeccioDadesGlobals(bw);
            escriuSeccioCodi(bw);
            escriuFuncionsIO(bw);
            escriuPeu(bw);
            System.out.println("   > Codi assemblador Easy68K generat a: " + nomFitxer);
        } catch (IOException e) {
            System.err.println("Error generant assemblador: " + e.getMessage());
        }
    }

    private void escriuCapcalera(BufferedWriter bw) throws IOException {
        bw.write("; --- CAPÇALERA ---"); bw.newLine();
        bw.write("    ORG    $1000"); bw.newLine();
        bw.write("START:"); bw.newLine();
        bw.write("    LEA    STACK_TOP, A7"); bw.newLine();
        bw.write("    JSR    main"); bw.newLine();
        bw.write("    SIMHALT"); bw.newLine();
        bw.newLine();
    }

    private void escriuSeccioDadesGlobals(BufferedWriter bw) throws IOException {
        bw.write("; --- DADES GLOBALS I TEMPORALS ---"); bw.newLine();
        
        // Definim la pila aquí per assegurar que l'etiqueta existeix
        bw.write("STACK_TOP: DS.L   1000   ; Reserva 4KB per la pila"); bw.newLine();
        
        Set<String> declarats = new HashSet<>();
        for (C3a_Instr instr : c3a.getBlocs()) {
            analitzaOperandPerGlobal(instr.getArg1(), declarats, bw);
            analitzaOperandPerGlobal(instr.getArg2(), declarats, bw);
            analitzaOperandPerGlobal(instr.getDesti(), declarats, bw);
        }
        bw.newLine();
    }

    private void analitzaOperandPerGlobal(String op, Set<String> declarats, BufferedWriter bw) throws IOException {
        if (op == null || op.equals("-") || op.matches("-?\\d+")) return;
        if (declarats.contains(op)) return;

        if (op.matches("t\\d+")) { // Temporals
            bw.write(String.format("%-10s DS.L   1", op)); bw.newLine();
            declarats.add(op);
            return;
        }

        Simbol s = ts.cercarSimbolAmbit("global", op);
        if (s != null && (s.getCategoria() == CategoriaSimbol.VARIABLE || s.getCategoria() == CategoriaSimbol.CONSTANT)) {
             bw.write(String.format("%-10s DS.L   1", op)); bw.newLine();
             declarats.add(op);
        }
    }

    private void escriuSeccioCodi(BufferedWriter bw) throws IOException {
        bw.write("; --- SECCIÓ DE CODI ---"); bw.newLine();

        for (C3a_Instr instr : c3a.getBlocs()) {
            if (instr.getEtiqueta() != null && !instr.getEtiqueta().isEmpty()) {
                gestionarEtiqueta(instr.getEtiqueta(), bw);
            }

            Codi op = instr.getCodi();
            String a1 = instr.getArg1();
            String a2 = instr.getArg2();
            String dest = instr.getDesti();

            bw.write("    ; " + instr.toString().trim()); bw.newLine();

            switch (op) {
                case SKIP:
                    if (dest != null && !dest.equals("-")) gestionarEtiqueta(dest, bw);
                    break;
                case COPY:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case ADD:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    ADD.L  %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case SUB:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    SUB.L  %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case PROD:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    MULS.L %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case DIV:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    DIVS.L %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case NEG:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write("    NEG.L  D0"); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case AND:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    AND.L  %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case OR:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    OR.L   %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case NOT:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write("    NOT.L  D0"); bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case GOTO:
                    bw.write("    BRA    " + dest); bw.newLine();
                    break;
                case IF_EQ:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    CMP.L  %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write("    BEQ    " + dest); bw.newLine();
                    break;
                case IF_NE:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    CMP.L  %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write("    BNE    " + dest); bw.newLine();
                    break;
                case IF_LT:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    CMP.L  %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write("    BLT    " + dest); bw.newLine();
                    break;
                case IF_GT:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write(String.format("    CMP.L  %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write("    BGT    " + dest); bw.newLine();
                    break;
                case PMB:
                    String midaFrame = (a2 != null) ? a2 : "0";
                    bw.write("    LINK   A6, #-" + midaFrame); bw.newLine();
                    break;
                case RET:
                    if (a1 != null && !a1.equals("-")) {
                        bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    }
                    bw.write("    UNLK   A6"); bw.newLine();
                    bw.write("    RTS"); bw.newLine();
                    break;
                case PARAM_S:
                    bw.write(String.format("    MOVE.L %s, -(A7)", traduirOperand(a1))); bw.newLine();
                    bytesParametresAcumulats += 4;
                    break;
                case PARAM_C:
                    carregarAdrecaBase(bw, a2, "A0");
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1))); bw.newLine();
                    bw.write("    MOVE.L 0(A0, D0.L), D1"); bw.newLine();
                    bw.write("    MOVE.L D1, -(A7)"); bw.newLine();
                    bytesParametresAcumulats += 4;
                    break;
                case CALL:
                    bw.write("    JSR    " + a1); bw.newLine();
                    if (bytesParametresAcumulats > 0) {
                        bw.write("    ADD.L  #" + bytesParametresAcumulats + ", A7"); bw.newLine();
                        bytesParametresAcumulats = 0;
                    }
                    if (dest != null && !dest.equals("-")) {
                        bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest))); bw.newLine();
                    }
                    break;
                case IND_VAL:
                    carregarAdrecaBase(bw, a1, "A0");
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write("    MOVE.L 0(A0, D0.L), D1"); bw.newLine();
                    bw.write(String.format("    MOVE.L D1, %s", traduirOperand(dest))); bw.newLine();
                    break;
                case IND_ASS:
                    carregarAdrecaBase(bw, dest, "A0");
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a2))); bw.newLine();
                    bw.write(String.format("    MOVE.L %s, D1", traduirOperand(a1))); bw.newLine();
                    bw.write("    MOVE.L D1, 0(A0, D0.L)"); bw.newLine();
                    break;
                case HALT:
                    bw.write("    SIMHALT"); bw.newLine();
                    break;
                default:
                    bw.write("    ; TODO: " + op); bw.newLine();
            }
            bw.newLine();
        }
    }

    private void gestionarEtiqueta(String et, BufferedWriter bw) throws IOException {
        bw.write(et + ":"); bw.newLine();
        if (et.equals("main")) {
            ambitActual = "main";
            bytesParametresAcumulats = 0;
        } else if (et.startsWith("f_")) {
            String possibleNom = et.substring(2);
            Simbol s = ts.cercarSimbol(possibleNom);
            if (s == null) s = ts.cercarSimbol(et); 
            if (s != null && (s.getCategoria() == CategoriaSimbol.FUNCIO || s.getCategoria() == CategoriaSimbol.PROCEDIMENT)) {
                ambitActual = s.getNom();
                bytesParametresAcumulats = 0;
            }
        }
    }

    private void escriuFuncionsIO(BufferedWriter bw) throws IOException {
        bw.write("; --- FUNCIONS E/S ---"); bw.newLine();
        bw.write("llegir:"); bw.newLine();
        bw.write("    MOVE.L #4, D0"); bw.newLine();
        bw.write("    TRAP   #15"); bw.newLine();
        bw.write("    MOVE.L D1, D0"); bw.newLine();
        bw.write("    RTS"); bw.newLine();
        bw.newLine();
        bw.write("imprimir:"); bw.newLine();
        bw.write("    MOVE.L 4(A7), D1"); bw.newLine();
        bw.write("    MOVE.L #3, D0"); bw.newLine();
        bw.write("    TRAP   #15"); bw.newLine();
        bw.write("    MOVE.L #13, D1"); bw.newLine();
        bw.write("    MOVE.L #6, D0"); bw.newLine();
        bw.write("    TRAP   #15"); bw.newLine();
        bw.write("    MOVE.L #10, D1"); bw.newLine();
        bw.write("    TRAP   #15"); bw.newLine();
        bw.write("    RTS"); bw.newLine();
        bw.newLine();
    }

    private void escriuPeu(BufferedWriter bw) throws IOException {
        bw.write("    END    START"); bw.newLine();
    }

    private String traduirOperand(String nom) {
        if (nom == null || nom.equals("-")) return "";
        if (nom.matches("-?\\d+")) return "#" + nom; 

        Simbol s = ts.cercarSimbolAmbit(ambitActual, nom);
        if (s == null) s = ts.cercarSimbolAmbit("global", nom);

        if (s != null) {
            if (s.isEsGlobal()) {
                return nom; 
            } else {
                int offsetTS = s.getOffset();
                int desp;
                if (s.getCategoria() == CategoriaSimbol.PARAMETRE) {
                    desp = 8 + offsetTS;
                } else {
                    desp = -(offsetTS + 4);
                }
                return desp + "(A6)";
            }
        }
        return nom;
    }
    
    private void carregarAdrecaBase(BufferedWriter bw, String nomArray, String regAdreca) throws IOException {
        Simbol s = ts.cercarSimbolAmbit(ambitActual, nomArray);
        if (s == null) s = ts.cercarSimbolAmbit("global", nomArray);
        
        if (s != null && !s.isEsGlobal()) {
            int desp = -(s.getOffset() + s.getTipus().getMidaBytes() * s.getMidaArray());
            bw.write(String.format("    LEA    %s(A6), %s", desp, regAdreca));
        } else {
            bw.write(String.format("    LEA    %s, %s", nomArray, regAdreca));
        }
    }
}