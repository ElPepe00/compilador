/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.gestor_errors;

import java.util.ArrayList;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe que gestiona tots els errors del frontend del compilador
 * @author josep
 */
public class GestorError {

    private static ArrayList<MissatgeError> LlistaErrorLexic;
    private static ArrayList<MissatgeError> LlistaErrorSintactic;
    private static ArrayList<MissatgeError> LlistaErrorSemantic;

    // Constructor
    public GestorError() {
        LlistaErrorLexic = new ArrayList<>();
        LlistaErrorSintactic = new ArrayList<>();
        LlistaErrorSemantic = new ArrayList<>();
    }

    // Mètode que afegeix un error a la llista d'errors en funció del tipus
    // d'error
    public static void afegirError(MissatgeError missatge){
        if(missatge.getTipus().equals(TipusError.LEXIC)){
            LlistaErrorLexic.add(missatge);
        }
        if(missatge.getTipus().equals(TipusError.SINTACTIC)){
            LlistaErrorSintactic.add(missatge);
        }
        if(missatge.getTipus().equals(TipusError.SEMANTIC)){
            LlistaErrorSemantic.add(missatge);
        }
    }

    // Mètode que mira dins les llistes si hiha o no errors
    public static boolean hihaError(){
        if(LlistaErrorLexic.isEmpty()
            && LlistaErrorSintactic.isEmpty()
            && LlistaErrorSemantic.isEmpty()){
            return false;
        }
        return true;
    }

    // Mètode que retorna tots els errors emmagatzemats a les llistes d'errors
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(formatErrors("LEXIC", LlistaErrorLexic));
        result.append(formatErrors("SINTACTIC", LlistaErrorSintactic));
        if(LlistaErrorSintactic.isEmpty()){
            result.append(formatErrors("SEMANTIC", LlistaErrorSemantic));
        }
        return result.toString();
    }

    // Dona format a la llista d'errors per una fàcil visualitzacio
    public String formatErrors(String type, ArrayList<MissatgeError> errors) {
        StringBuilder sb = new StringBuilder();

        if (errors.isEmpty()) {
            sb.append("NO HI HA ERRORS ").append(type).append("\n");
        } else {
            // Ordenar los errores por línea de mayor a menor
            errors.sort(Comparator.comparingInt(MissatgeError::getLine));

            for (MissatgeError err : errors) {
                sb.append(err.toString()).append("\n");
            }
        }

        return sb.toString();
    }

    // Mètode que guarda a un fitxer els errors
    public void exportarErrors(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(formatErrors("LEXICS", LlistaErrorLexic));
            writer.write(formatErrors("SINTACTICS", LlistaErrorSintactic));
            writer.write(formatErrors("SEMANTICS", LlistaErrorSemantic));
            writer.flush();
        } catch (IOException e) {
        }
    }
}
