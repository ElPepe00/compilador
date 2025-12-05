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



/**
 * PROGRAMA PRINCIPAL DEL COMPILADOR PER EL LLENGUATGE "PEPLANG"
 * @author josep
 */
public class CompiladorMain {

    public static void main(String[] args) {
        
        int numPrograma = 1;
        
        String rutaProgramesProva = "programesProva/";
        String nomFitxer = "programaFuncional_" + numPrograma + ".txt";
        String rutaPrograma = rutaProgramesProva + nomFitxer;
        String rutaSortida = "fitxersSortida/" + "programaFuncional_" + numPrograma + "/";
 
        try {
            
            System.out.println(" --------------------------");
            System.out.println(" --- COMPILADOR PEPLANG ---");
            System.out.println(" --------------------------");
            System.out.println("   Programa seleccionat: " + nomFitxer);   
            
            // Inicialitzam el gestor d'errors
            GestorError gestorError = new GestorError();
            
            // *************
            // ANALISI LEXIC
            // *************
            System.out.println("\n --- ANALISI LEXIC:");
            Scanner scannerTokens = new Scanner(new FileReader(rutaPrograma));
            
            // generar fitxer de tokens
            guardarFitxerTokens(scannerTokens, (rutaSortida + "fitxerTokens_" + nomFitxer));
            scannerTokens.yyclose();
            
            // ***********************
            // ANALISI SINTACTIC + AST
            // ***********************
            System.out.println("\n --- ANALISI SINTACTIC + AST:");
            Scanner scannerParser = new Scanner(new FileReader(rutaPrograma));
            SymbolFactory sf = new ComplexSymbolFactory();
            Parser parser = new Parser(scannerParser, sf);
            
            // --------------------------------------------------- Revisar
            // Cream l'arrel de l'arbre sintactic
            Symbol resultat = parser.parse();
            
            Node_Peplang arrel = (Node_Peplang) resultat.value;

            System.out.println("   Arbre sintactic creat correctament.");
            
            // *************************
            // TAULA SIMBOLS + SEMATINCA
            // *************************
            System.out.println("\n --- ANALISI SEMANTICA:");
            
            TaulaSimbols ts = new TaulaSimbols();
            TaulaSimbols.entrarBloc();
            
            arrel.gestioSemantica(ts);

            System.out.println("   Nombre de simbols a la TS: " + TaulaSimbols.obtenirTotsElsSimbols().size());
            TaulaSimbols.guardarTaulaSimbols(rutaSortida + "taulaSimbols_" + nomFitxer);
            
            // **************
            // CODI INTERMEDI
            // **************
            System.out.println("\n --- CODI INTERMEDI (C3@):");
            
            C3a c3a = new C3a();
            arrel.generaCodi3a(c3a);
            
            c3a.guadarCodi3a(rutaSortida + "codi3a_" + nomFitxer);
            System.out.println("   Nombre d'instruccions de C3@: " + c3a.getNumBlocs());
            
            c3a.getTaulaVariables().guardarTaulaVariables(rutaSortida + "taulaVariables_" + nomFitxer);
            c3a.getTaulaProcediments().guardarTaulaProcediments(rutaSortida + "taulaProcediments_" +nomFitxer);

            
            
        } catch(Exception e) {
            throw new RuntimeException(e);
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
                String cadena = "Token: " + sym.terminalNames[cs.sym]
                                + " -- Linia: " + cs.left
                                + ", Columna: " + cs.right;
                
                bw.write(cadena);
                bw.newLine();
                contTokens++;
            }
            
            System.out.println("   Fitxer de tokens generat correctament.");
            System.out.println("   Numero de tokens generats: " + contTokens);
            bw.close();
            
        } catch (IOException e) {
            System.err.println("Error al guardar el fitxer de tokens: " + e);
        }
    }
}
