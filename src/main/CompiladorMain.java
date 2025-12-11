/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package main;

import java.io.*;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

import frontend.lexic.*;
import frontend.sintactic.*;
import frontend.taula_simbols.*;
import frontend.ast.*;
import frontend.gestor_errors.*;

import backend.codi_intermedi.*;
import backend.assemblador.*;



/**
 * PROGRAMA PRINCIPAL DEL COMPILADOR PER EL LLENGUATGE "PEPLANG"
 * @author josep
 */
public class CompiladorMain {

    public static void main(String[] args) {
        
        long tempsInici = System.currentTimeMillis();
        
        // Programes Funcionals del 1 - 3
        // Programes Erronis del 4 - 6
        int numPrograma = 5;                                                    // ----- SELECCIONAR PROGRAMA
        
        String rutaProgramesProva = "programesProva/";
        String nomFitxer = "programaFuncional_" + numPrograma + ".txt";
        String rutaPrograma = rutaProgramesProva + nomFitxer;
        String rutaSortida = "fitxersSortida/" + "programaFuncional_" + numPrograma + "/";

        
        /*
        if (args.length == 0) {
            System.err.println("Error: Falta el nom del fitxer d'entrada.");
            System.err.println("   > Us: java -jar compilador.jar <ruta/nom_fitxer_entrada.txt>");
            return;
        }

        String nomFitxerEntrada = args[0]; // Capturamos args[0]

        String rutaProgramesProva = "programesProva/";
        String nomFitxer = nomFitxerEntrada;
        String rutaPrograma = rutaProgramesProva + nomFitxer;
        String nomBase = nomFitxer.substring(0, nomFitxer.lastIndexOf('.'));
        String rutaSortida = "fitxersSortida/" + nomBase + "/";
        */
        
        File f = new File(rutaSortida);
        
        if (!f.exists()) {
            f.mkdirs();
        }
 
        try {
            
            System.out.println(" --------------------------");
            System.out.println(" --- COMPILADOR PEPLANG ---");
            System.out.println(" --------------------------");
            System.out.println("   Programa seleccionat: " + nomFitxer);   
            
            // Inicialitzam el gestor d'errors
            GestorError gestorError = new GestorError();
            
            // ***************
            //  ANALISI LEXIC
            // ***************
            System.out.println("\n [1] --- ANALISI LEXIC:");
            Scanner scannerTokens = new Scanner(new FileReader(rutaPrograma));
            String rutaTokens = rutaSortida + "fitxerTokens_" + nomFitxer;
            // generar fitxer de tokens
            guardarFitxerTokens(scannerTokens, rutaTokens);
            scannerTokens.yyclose();
            
            if (GestorError.hihaError()) {
                System.err.println("   [ERROR] S'han trobat errors lèxics. Aturant compilacio.");
                gestorError.exportarErrors(rutaSortida + "errors_" + nomFitxer);
                return;
            }
            
            // *************************
            //  ANALISI SINTACTIC + AST
            // *************************
            System.out.println("\n [2] --- ANALISI SINTACTIC + AST:");
            Scanner scannerParser = new Scanner(new FileReader(rutaPrograma));
            SymbolFactory sf = new ComplexSymbolFactory();
            Parser parser = new Parser(scannerParser, sf);
            
            // Cream l'arrel de l'arbre sintactic
            Symbol resultat = parser.parse();
            Node_Peplang arrel = (Node_Peplang) resultat.value;
            
            if (GestorError.hihaError() || arrel == null) {
                System.err.println("   [ERROR] S'han trobat errors sintactics. Aturant compilacio.");
                gestorError.exportarErrors(rutaSortida + "errors_" + nomFitxer);
                return;
            }

            System.out.println("   > Arbre sintactic explicit construit correctament.");
            
            // ***************************
            //  TAULA SIMBOLS + SEMATINCA
            // ***************************
            System.out.println("\n [3] --- ANALISI SEMANTICA:");
            
            TaulaSimbols ts = new TaulaSimbols();
            ts.entrarBloc(); // cream ambit 0, GLOBAL
            
            // Aquesta crida comprova tota la semantica de l'arbre/programa
            arrel.gestioSemantica(ts);
            
            if (GestorError.hihaError()) {
                System.err.println("   [ERROR] S'han trobat errors semantics. Aturant compilacio.");
                gestorError.exportarErrors(rutaSortida + "errors_" + nomFitxer);
                // guardar la TS parcial per depurar
                ts.guardarTaulaSimbols(rutaSortida + "taulaSimbols_PARCIAL_" + nomFitxer);
                return;
            }

            // Guardam taula de simbol
            ts.guardarTaulaSimbols(rutaSortida + "taulaSimbols_" + nomFitxer);
            // Guardam taula de variables i procediments
            ts.exportarTaulaVariables(rutaSortida + "taulaVariables_" + nomFitxer);
            ts.exportarTaulaProcediments(rutaSortida + "taulaProcediments_" + nomFitxer);
            
            // ****************
            //  CODI INTERMEDI
            // ****************
            System.out.println("\n [4] --- CODI INTERMEDI (C3@):");
            
            C3a c3a = new C3a();
            // Ja tenim l'arbre construit, nomes generam el codi, recorrentlo
            arrel.generaCodi3a(c3a);
            
            // Guardam el fitxer d'instruccions de 3 adreces
            String rutaC3a = rutaSortida + "codi3a_" + nomFitxer;
            c3a.guadarCodi3a(rutaC3a);
            System.out.println("   > Nombre d'instruccions de C3@: " + c3a.getNumInstr());
           

            // ******************
            //  CODI ASSEMBLADOR
            // ******************
            System.out.println("\n [5] --- GENERACIO ASSEMBLADOR (Easy68k):");
            // Cream el generador i amb la TS i el C3@ generam el codi per 68k
            GeneradorAssemblador genAsm = new GeneradorAssemblador(c3a, ts);
            
            // Guardam el fitxer a la ruta especificada
            String rutaAsm = rutaSortida + "programa_" + nomFitxer.replace(".txt", ".X68");
            genAsm.generaFitxer(rutaAsm);
            
            long tempsFinal = System.currentTimeMillis() - tempsInici;
            
            // ** FI COMPILACIÓ (Exitosa)
            System.out.println("\n\n ---------------------------------------");
            System.out.println("   > COMPILACIO EXITOSA! (Temps: " + tempsFinal + " ms)\n");
            
        } catch(Exception e) {
            System.err.println("\n [EXCEPTION] Error fatal durant la compilacio:");
            e.getMessage();
        }
    }
    
    
    
    // Mètode que genera el fitxer de tokens
    public static void guardarFitxerTokens(Scanner scanner, String rutaFitxer) {
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFitxer));
            Symbol token;
            int contTokens = 0;
            
            while ((token = scanner.next_token()).sym != sym.EOF) {
                ComplexSymbol cs = (ComplexSymbol) token;
                String nomToken = sym.terminalNames[cs.sym];
                String valor = (cs.value != null) ? cs.value.toString() : "";
                
                String linia = String.format("Token: %-15s | Valor: %-10s | Linia: %d, Col: %d", 
                                             nomToken, valor, cs.left, cs.right);
                
                bw.write(linia);
                bw.newLine();
                contTokens++;
            }
            
            System.out.println("   > Fitxer de Tokens guardat a: " + rutaFitxer);
            System.out.println("   > Numero de tokens generats: " + contTokens);
            bw.close();
            
        } catch (IOException e) {
            System.err.println("Error al guardar el fitxer de tokens: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error durant l'escaneig: " + e.getMessage());
        }
    }
}
