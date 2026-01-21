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

    // Control de l'àmbit actual durant la generació
    private String ambitActual = "global";
    private int bytesParametresAcumulats = 0;

    // Mapes per gestionar offsets de pila (Locals)
    private final Map<String, Integer> tempOffsets = new HashMap<>();
    private final Map<String, Integer> funcTotalSizes = new HashMap<>();

    // Set per gestionar temporals globals (Static Data)
    // Els temporals fora de funcions no van a la pila
    private final Set<String> temporalsGlobals = new HashSet<>();

    public GeneradorAssemblador(C3a c3a, TaulaSimbols ts) {
        this.c3a = c3a;
        this.ts = ts;
    }

    public void generaFitxer(String nomFitxer) {
        // 1. Pre-càlcul: Analitzar on viuen els temporals i calcular mides de frame
        precalcularOffsetsTemporals();

        // 2. Generació: Escriure el codi
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

    /**
     * Funció auxiliar per determinar si una etiqueta marca l'inici d'un nou
     * àmbit (funció).
     */
    private String determinarNouAmbit(String et) {
        if (et == null || et.isEmpty()) {
            return null;
        }
        if (et.equals("main")) {
            return "main";
        }

        // Netegem prefixos si n'hi ha (ex: f_suma -> suma)
        String nomSensePrefix = et.startsWith("f_") ? et.substring(2) : et;
        Simbol s = ts.cercarSimbol(nomSensePrefix);
        if (s == null) {
            s = ts.cercarSimbol(et);
        }

        if (s != null && (s.getCategoria() == CategoriaSimbol.FUNCIO || s.getCategoria() == CategoriaSimbol.PROCEDIMENT)) {
            return s.getNom();
        }
        return null;
    }

    /**
     * Recorre tot el C3A per saber quants temporals usa cada funció
     */
    private void precalcularOffsetsTemporals() {
        String ambitScan = "global";
        int localsSize = 0;
        Set<String> tempsInScope = new HashSet<>();

        for (C3a_Instr instr : c3a.getBlocs()) {

            // Si detectem etiqueta de funció, tanquem l'àmbit anterior i obrim el nou
            if (instr.getEtiqueta() != null && !instr.getEtiqueta().isEmpty()) {
                String nouAmbit = determinarNouAmbit(instr.getEtiqueta());
                
                if (nouAmbit != null) {
                    // Guardem info de l'àmbit que acabem de tancar (si no era global)
                    if (!ambitScan.equals("global")) {
                        registrarMidaScope(ambitScan, localsSize, tempsInScope);
                    }
                    
                    // Resetegem per al nou àmbit
                    tempsInScope.clear();
                    localsSize = 0;
                    ambitScan = nouAmbit;
                }
            }

            // Capturar mida de variables locals declarades (instrucció PMB)
            if (instr.getCodi() == Codi.PMB) {
                try {
                    localsSize = (instr.getArg2() != null && !instr.getArg2().equals("-"))
                            ? Integer.parseInt(instr.getArg2()) : 0;
                } catch (NumberFormatException e) {
                    localsSize = 0;
                }
            }

            // Recollir temporals usats
            // Si som al global, van al Set de globals. Si no, al Set local.
            if (ambitScan.equals("global")) {
                afegirSiEsTemporal(instr.getArg1(), temporalsGlobals);
                afegirSiEsTemporal(instr.getArg2(), temporalsGlobals);
                afegirSiEsTemporal(instr.getDesti(), temporalsGlobals);
            } else {
                afegirSiEsTemporal(instr.getArg1(), tempsInScope);
                afegirSiEsTemporal(instr.getArg2(), tempsInScope);
                afegirSiEsTemporal(instr.getDesti(), tempsInScope);
            }
        }
        
        // Registrar l'últim àmbit processat (si no és global)
        if (!ambitScan.equals("global")) {
            registrarMidaScope(ambitScan, localsSize, tempsInScope);
        }
    }

    private void afegirSiEsTemporal(String op, Set<String> set) {
        if (op != null && op.matches("t\\d+")) {
            set.add(op);
        }
    }

    /**
     * Assigna offsets negatius a la pila per als temporals locals.
     * MidaTotal = Variables Locals + (NumTemporals * 4 bytes).
     */
    private void registrarMidaScope(String ambit, int locals, Set<String> temps) {
        int i = 0;
        for (String t : temps) {
            // Offset comença després de les variables locals declarades
            // Ex: si locals=4 bytes, t0 serà a -8(A6) -> -(4 + 4 + 0)
            int off = -(locals + 4 + (i * 4));
            tempOffsets.put(t, off);
            i++;
        }
        // Guardem la mida total per usar-la al LINK
        int total = locals + (temps.size() * 4);
        
        // Assegurem que sigui parell (encara que x4 ja ho és) per al stack alignment
        if (total % 2 != 0) total++; 
        
        funcTotalSizes.put(ambit, total);
    }

    // -------------------------------------------------------------------------
    private void escriuCapcalera(BufferedWriter bw) throws IOException {
        bw.write("; --- CAPÇALERA ---");
        bw.newLine();
        bw.write("    ORG    $1000");
        bw.newLine();
        bw.write("START:");
        bw.newLine();
        bw.write("    LEA    STACK_TOP, A7");
        bw.newLine();
        
        // No saltar a main, sinó a l'inici del codi global
        bw.write("    JMP    __inici"); 
        
        bw.newLine();
        
        bw.write("    SIMHALT"); 
        bw.newLine();
        bw.newLine();
    }

    private void escriuSeccioDadesGlobals(BufferedWriter bw) throws IOException {
        bw.write("; --- DADES GLOBALS ---");
        bw.newLine();
        bw.write("STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila");
        bw.newLine();

        Set<String> declarats = new HashSet<>();
        
        // Declarar variables globals del programa (x, resultat, etc.)
        for (C3a_Instr instr : c3a.getBlocs()) {
            analitzaOperandPerGlobal(instr.getArg1(), declarats, bw);
            analitzaOperandPerGlobal(instr.getArg2(), declarats, bw);
            analitzaOperandPerGlobal(instr.getDesti(), declarats, bw);
        }

        // Declarar temporals globals (t0, t1...) com a variables estàtiques
        for (String tGlob : temporalsGlobals) {
            if (!declarats.contains(tGlob)) {
                bw.write(String.format("%-10s DS.L   1", tGlob));
                bw.newLine();
                declarats.add(tGlob);
            }
        }
        
        bw.newLine();
    }

    private void analitzaOperandPerGlobal(String op, Set<String> declarats, BufferedWriter bw) throws IOException {
        if (op == null || op.equals("-") || op.matches("-?\\d+")) {
            return;
        }
        if (declarats.contains(op)) {
            return;
        }
        // Temporals ja es gestionen a part
        if (op.matches("t\\d+")) {
            return;
        }

        Simbol s = cercarSimbolSegur(op);

        if (s != null && s.isEsGlobal() && (s.getCategoria() == CategoriaSimbol.VARIABLE || s.getCategoria() == CategoriaSimbol.CONSTANT)) {
            bw.write(String.format("%-10s DS.L   1", op));
            bw.newLine();
            declarats.add(op);
        }
    }

    private void escriuSeccioCodi(BufferedWriter bw) throws IOException {
        bw.write("; --- SECCIÓ DE CODI ---");
        bw.newLine();
        
        // Punt d'entrada per a les inicialitzacions globals
        bw.write("__inici:");
        bw.newLine();

        ambitActual = "global";
        bytesParametresAcumulats = 0;
        
        // CONTROL DE FLUX: Per evitar entrar a funcions abans del main
        boolean hemSaltatAlMain = false;

        for (C3a_Instr instr : c3a.getBlocs()) {

            // Detectar inici d'una etiqueta (funció o main)
            if (instr.getEtiqueta() != null && !instr.getEtiqueta().isEmpty()) {
                
                String nouAmbit = determinarNouAmbit(instr.getEtiqueta());
                
                // Si estem a l'àmbit global i trobem una nova funció/procediment
                if (ambitActual.equals("global") && nouAmbit != null) {
                    
                    // CAS A: És el main.
                    // No cal fer res especial, l'execució caurà dins del main naturalment.
                    if (nouAmbit.equals("main")) {
                        hemSaltatAlMain = true; // Ja hem arribat on volíem
                    } 
                    // CAS B: És una funció (ex: "sumar") i encara no hem saltat al main.
                    // Hem de generar un salt per esquivar aquesta funció.
                    else if (!hemSaltatAlMain) {
                        bw.write("    BRA    main   ; Saltem funcions inicials per anar al main");
                        bw.newLine();
                        hemSaltatAlMain = true;
                    }
                }

                gestionarEtiqueta(instr.getEtiqueta(), bw);
            }

            Codi op = instr.getCodi();
            String a1 = instr.getArg1();
            String a2 = instr.getArg2();
            String dest = instr.getDesti();

            bw.write("    ; " + instr.toString().trim());
            bw.newLine();

            switch (op) {
                case SKIP:
                    if (dest != null && !dest.equals("-")) {
                        // REPETIM LA COMPROVACIÓ PER A ETIQUETES DEFINIDES VIA SKIP
                        // (Per si el C3A usa SKIP per definir l'etiqueta de la funció)
                        String nouAmbit = determinarNouAmbit(dest);
                        if (ambitActual.equals("global") && nouAmbit != null) {
                            if (nouAmbit.equals("main")) {
                                hemSaltatAlMain = true;
                            } else if (!hemSaltatAlMain) {
                                bw.write("    BRA    main   ; Saltem funcions inicials per anar al main");
                                bw.newLine();
                                hemSaltatAlMain = true;
                            }
                        }
                        gestionarEtiqueta(dest, bw);
                    }
                    break;
                case COPY:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;
                case ADD:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    ADD.L  %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;
                case SUB:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    SUB.L  %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;

                // *** MULS i DIVS ***
                case PROD:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    MULS.W %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;
                case DIV:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    DIVS.W %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write("    EXT.L  D0"); // Neteja residu
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;

                case NEG:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write("    NEG.L  D0");
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;
                case AND:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    AND.L  %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;
                case OR:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    OR.L   %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;
                case NOT:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write("    NOT.L  D0");
                    bw.newLine();
                    bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;
                case GOTO:
                    bw.write("    BRA    " + dest);
                    bw.newLine();
                    break;
                case IF_EQ:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    CMP.L  %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write("    BEQ    " + dest);
                    bw.newLine();
                    break;
                case IF_NE:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    CMP.L  %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write("    BNE    " + dest);
                    bw.newLine();
                    break;
                case IF_LT:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    CMP.L  %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write("    BLT    " + dest);
                    bw.newLine();
                    break;
                case IF_GT:
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    bw.write(String.format("    CMP.L  %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write("    BGT    " + dest);
                    bw.newLine();
                    break;
                    
                case PMB:
                    // Utilitzar la mida pre-calculada (locals + temporals)
                    int totalFrame = 0;
                    if (funcTotalSizes.containsKey(ambitActual)) {
                        totalFrame = funcTotalSizes.get(ambitActual);
                    } else {
                        try {
                            totalFrame = (a2 != null) ? Integer.parseInt(a2) : 0;
                        } catch (Exception e) {}
                    }
                    bw.write("    LINK   A6, #-" + totalFrame);
                    bw.newLine();
                    break;
                    
                case RET:
                    if (a1 != null && !a1.equals("-")) {
                        bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                        bw.newLine();
                    }
                    bw.write("    UNLK   A6");
                    bw.newLine();
                    bw.write("    RTS");
                    bw.newLine();
                    break;
                case PARAM_S:
                    bw.write(String.format("    MOVE.L %s, -(A7)", traduirOperand(a1)));
                    bw.newLine();
                    bytesParametresAcumulats += 4;
                    break;
                case PARAM_C:
                    carregarAdrecaBase(bw, a2, "A0");
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();
                    Simbol sParamC = cercarSimbolSegur(a2);
                    boolean esCharParamC = (sParamC != null && sParamC.getTipus() == TipusSimbol.TAULA_CARACTER);

                    if (esCharParamC) {
                        bw.write("    CLR.L  D1");
                        bw.newLine();
                        bw.write("    MOVE.B 0(A0, D0.L), D1");
                        bw.newLine();
                    } else {
                        bw.write("    MOVE.L 0(A0, D0.L), D1");
                        bw.newLine();
                    }

                    bw.write("    MOVE.L D1, -(A7)");
                    bw.newLine();
                    bytesParametresAcumulats += 4;
                    break;
                case CALL:
                    bw.write("    JSR    " + a1);
                    bw.newLine();
                    if (bytesParametresAcumulats > 0) {
                        bw.write("    ADD.L  #" + bytesParametresAcumulats + ", A7");
                        bw.newLine();
                        bytesParametresAcumulats = 0;
                    }
                    if (dest != null && !dest.equals("-")) {
                        bw.write(String.format("    MOVE.L D0, %s", traduirOperand(dest)));
                        bw.newLine();
                    }
                    break;

                // -----------------------------------------------------------------
                // GESTIÓ D'ARRAYS (ADDRESS ERROR)
                // -----------------------------------------------------------------
                case IND_VAL:
                    bw.write(String.format("    ; ind_val %s[%s] -> %s", a2, a1, dest));
                    bw.newLine();
                    carregarAdrecaBase(bw, a2, "A0");
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a1)));
                    bw.newLine();

                    Simbol sVal = cercarSimbolSegur(a2);
                    boolean esCharVal = (sVal != null && sVal.getTipus() == TipusSimbol.TAULA_CARACTER);

                    if (esCharVal) {
                        bw.write("    CLR.L  D1");
                        bw.newLine();
                        bw.write("    MOVE.B 0(A0, D0.L), D1");
                        bw.newLine();
                    } else {
                        bw.write("    MOVE.L 0(A0, D0.L), D1");
                        bw.newLine();
                    }
                    bw.write(String.format("    MOVE.L D1, %s", traduirOperand(dest)));
                    bw.newLine();
                    break;

                case IND_ASS:
                    bw.write(String.format("    ; ind_ass %s[%s] = %s", dest, a2, a1));
                    bw.newLine();
                    carregarAdrecaBase(bw, dest, "A0");
                    bw.write(String.format("    MOVE.L %s, D0", traduirOperand(a2)));
                    bw.newLine();
                    bw.write(String.format("    MOVE.L %s, D1", traduirOperand(a1)));
                    bw.newLine();

                    Simbol sAss = cercarSimbolSegur(dest);
                    boolean esCharAss = (sAss != null && sAss.getTipus() == TipusSimbol.TAULA_CARACTER);

                    if (esCharAss) {
                        bw.write("    MOVE.B D1, 0(A0, D0.L)");
                        bw.newLine();
                    } else {
                        bw.write("    MOVE.L D1, 0(A0, D0.L)");
                        bw.newLine();
                    }
                    break;

                case HALT:
                    bw.write("    SIMHALT");
                    bw.newLine();
                    break;
                default:
                    bw.write("    ; TODO: " + op);
                    bw.newLine();
            }
            bw.newLine();
        }
    }
    
    private void gestionarEtiqueta(String et, BufferedWriter bw) throws IOException {
        bw.write(et + ":");
        bw.newLine();
        String nouAmbit = determinarNouAmbit(et);
        if (nouAmbit != null) {
            ambitActual = nouAmbit;
            bytesParametresAcumulats = 0;
        }
    }

    private void escriuFuncionsIO(BufferedWriter bw) throws IOException {
        bw.write("; --- FUNCIONS E/S ---");
        bw.newLine();
        
        bw.write("llegir:");
        bw.newLine();
        bw.write("    MOVE.L #4, D0   ; Tasca 4: Llegir Enter (espera Intro)");
        bw.newLine();
        bw.write("    TRAP   #15");
        bw.newLine();
        bw.write("    MOVE.L D1, D0   ; El resultat es guarda a D1, el movem a D0");
        bw.newLine();
        bw.write("    RTS");
        bw.newLine();
        bw.newLine();
        
        bw.write("imprimir:");
        bw.newLine();
        bw.write("    MOVE.L 4(A7), D1");
        bw.newLine();
        bw.write("    MOVE.L #3, D0");
        bw.newLine();
        bw.write("    TRAP   #15");
        bw.newLine();
        bw.write("    MOVE.L #13, D1");
        bw.newLine();
        bw.write("    MOVE.L #6, D0");
        bw.newLine();
        bw.write("    TRAP   #15");
        bw.newLine();
        bw.write("    MOVE.L #10, D1");
        bw.newLine();
        bw.write("    TRAP   #15");
        bw.newLine();
        bw.write("    RTS");
        bw.newLine();
        bw.newLine();
    }

    private void escriuPeu(BufferedWriter bw) throws IOException {
        bw.write("    END    START");
        bw.newLine();
    }

    // Mètode auxiliar per trobar símbols de manera segura en qualsevol àmbit
    private Simbol cercarSimbolSegur(String nom) {
        if (nom == null) return null;
        Simbol s = ts.cercarSimbolAmbit(ambitActual, nom);
        if (s == null) {
            s = ts.cercarSimbolAmbit("global", nom);
        }
        if (s == null) {
            s = ts.cercarSimbolAmbit("GLOBAL", nom);
        }
        return s;
    }

    private String traduirOperand(String nom) {
        if (nom == null || nom.equals("-")) {
            return "";
        }
        if (nom.matches("-?\\d+")) {
            return "#" + nom;
        }

        if (nom.matches("t\\d+")) {
            // Si és temporal global, torna el seu nom (etiqueta)
            if (temporalsGlobals.contains(nom)) {
                return nom;
            }
            // Si és local, torna l'offset de pila
            if (tempOffsets.containsKey(nom)) {
                return tempOffsets.get(nom) + "(A6)";
            } else {
                return nom; // Cas estrany, fallback
            }
        }

        Simbol s = cercarSimbolSegur(nom);

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
        Simbol s = cercarSimbolSegur(nomArray);

        if (s != null && !s.isEsGlobal()) {
            if (s.getCategoria() == CategoriaSimbol.PARAMETRE && s.isEsArray()) {
                int offsetTS = s.getOffset();
                int desp = 8 + offsetTS;
                bw.write(String.format("    MOVE.L %d(A6), %s", desp, regAdreca));
            } else {
                int midaTotal = s.getTipus().getMidaBytes() * s.getMidaArray();
                int desp = -(s.getOffset() + midaTotal);
                bw.write(String.format("    LEA    %d(A6), %s", desp, regAdreca));
            }
        } else {
            bw.write(String.format("    LEA    %s, %s", nomArray, regAdreca));
        }
        bw.newLine();
    }
}