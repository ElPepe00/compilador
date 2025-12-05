/*
 * Autor: Josep Oliver Vallespir
 * DNI: 78222663P
 * Curs: 2025-2026
 * Assignatura: Compiladors I
 */
package frontend.ast;

import backend.codi_intermedi.C3a;
import backend.codi_intermedi.Codi;
import frontend.taula_simbols.*;
import frontend.sintactic.Operador;


/**
 *
 * @author josep
 */
public class Node_Express extends Node {

    private Operador op;
    
    // Per binaries
    private Node_Express e1;
    private Node_Express e2;
    
    // per unaries
    private Node_Express unari;
    
    // Fulles
    private Node_Ref ref;
    private Node_Num num;
    private Node_CharLit charLit;
    private Node_BoolLit boolLit;
    private Node_Crida_func crida;
    
    // CONSTRUCTORS
    public Node_Express(Operador op, Node_Express e1, Node_Express e2) {
        super("Express");
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }
    
    public Node_Express(Operador op, Node_Express e) {
        super("Express");
        this.op = op;
        this.unari = e;
    }
    
    public Node_Express(Operador op, Node_Ref n) {
        super("Express");
        this.op = op;
        this.ref = n;
    }
    
    public Node_Express(Operador op, Node_Num n) {
        super("Express");
        this.op = op;
        this.num = n;
    }
    
    public Node_Express(Operador op, Node_CharLit n) {
        super("Express");
        this.op = op;
        this.charLit = n;
    }
    
    public Node_Express(Operador op, Node_BoolLit n) {
        super("Express");
        this.op = op;
        this.boolLit = n;
    }
    
    public Node_Express(Operador op, Node_Crida_func n) {
        super("Express");
        this.op = op;
        this.crida = n;
    }

    // GETTERS
    public Operador getOp() {
        return op;
    }

    public Node_Express getE1() {
        return e1;
    }

    public Node_Express getE2() {
        return e2;
    }

    public Node_Express getUnari() {
        return unari;
    }

    public Node_Ref getRef() {
        return ref;
    }

    public Node_Num getNum() {
        return num;
    }

    public Node_CharLit getCharLit() {
        return charLit;
    }

    public Node_BoolLit getBoolLit() {
        return boolLit;
    }

    public Node_Crida_func getCrida() {
        return crida;
    }
    
    public TipusSimbol getTipusSimbol(TaulaSimbols ts) {
        
        switch (op) {
            case SUMA:
            case RESTA:
            case MULT:
            case DIV:
                {
                    TipusSimbol t1 = e1.getTipusSimbol(ts);
                    TipusSimbol t2 = e2.getTipusSimbol(ts);
                    
                    if (t1 != TipusSimbol.INT || t2 != TipusSimbol.INT) {
                        throw new RuntimeException("Operador aritmetic " + op 
                        + "nomes admet INT, s'ha tronat " + t1 + " i " + t2);
                    }
                    
                    return TipusSimbol.INT;
                }
                
            case AND:
            case OR:
                {
                    TipusSimbol t1 = e1.getTipusSimbol(ts);
                    TipusSimbol t2 = e2.getTipusSimbol(ts);
                    
                    if (t1 != TipusSimbol.BOOL || t2 != TipusSimbol.BOOL) {
                        throw new RuntimeException("Operador aritmetic " + op 
                        + "nomes admet BOOL, s'ha tronat " + t1 + " i " + t2);
                    }
                    
                    return TipusSimbol.BOOL;
                }
                
            case IGUAL:
            case NOIGUAL:
                {
                    TipusSimbol t1 = e1.getTipusSimbol(ts);
                    TipusSimbol t2 = e2.getTipusSimbol(ts);
                    
                    if (t1 != t2) {
                        throw new RuntimeException("Comparacio " + op 
                        + " amb tipus diferents: " + t1 + " i " + t2);
                    }
                    
                    return TipusSimbol.BOOL;
                }
                
            case MENOR:
            case MAJOR:
                {
                    TipusSimbol t1 = e1.getTipusSimbol(ts);
                    TipusSimbol t2 = e2.getTipusSimbol(ts);
                    
                    // comprova que siguin el mateix tipus
                    if (t1 != t2) {
                        throw new RuntimeException("Comparacio " + op 
                        + " amb tipus diferents: " + t1 + " i " + t2);
                    }
                    
                    // només admet INT i CARACTER
                    if (t1 != TipusSimbol.INT && t1 != TipusSimbol.CARACTER) {
                        throw new RuntimeException("Comparacio " + op 
                        + " només admet INT o CARACTER: " + t1 + " i " + t2);
                    }
                    
                    return TipusSimbol.BOOL;
                }
                
            case NOT:
                {
                    TipusSimbol t = unari.getTipusSimbol(ts);
                    
                    if (t != TipusSimbol.BOOL) {
                        throw new RuntimeException("NOT nomes admet BOOl, s'ha trobat: " + t);
                    }
                    
                    return TipusSimbol.BOOL;
                }
                
            case UMINUS:
                {
                    TipusSimbol t = unari.getTipusSimbol(ts);
                    
                    if (t != TipusSimbol.INT) {
                        throw new RuntimeException("Signe negatiu nomes admet INT, s'ha trobat: " + t);
                    }
                    
                    return TipusSimbol.INT;
                }
                
            case PAREN: return unari.getTipusSimbol(ts);
            case REF: return ref.getTipusSimbol(ts);
            case NUM: return num.getTipusSimbol();
            case CHAR: return charLit.getTipusSimbol();
            case BOOL: return boolLit.getTipusSimbol();
            case CRIDA: return crida.getTipusSimbol(ts);
                
            default:
                throw new IllegalStateException("Operador d'expressio no gestionat: " + op);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        switch (op) {

        // ------ Binàries aritmètiques ------
        case SUMA: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            codi3a.afegir(Codi.ADD, t1, t2, tRes);
            return tRes;
        }
        case RESTA: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            codi3a.afegir(Codi.SUB, t1, t2, tRes);
            return tRes;
        }
        case MULT: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            codi3a.afegir(Codi.PROD, t1, t2, tRes);
            return tRes;
        }
        case DIV: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            codi3a.afegir(Codi.DIV, t1, t2, tRes);
            return tRes;
        }

        // ------ Binàries lògiques ------
        case AND: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            codi3a.afegir(Codi.AND, t1, t2, tRes);
            return tRes;
        }
        case OR: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            codi3a.afegir(Codi.OR, t1, t2, tRes);
            return tRes;
        }

        // ------ Comparacions ------
        case IGUAL: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            String lTrue = codi3a.novaEtiqueta();
            String lEnd = codi3a.novaEtiqueta();
            
            codi3a.afegir(Codi.IF_EQ, t1, t2, lTrue);
            codi3a.afegir(Codi.COPY, "0", null, tRes);
            codi3a.afegir(Codi.GOTO, null, null, lEnd);
            codi3a.afegirEtiqueta(lTrue);
            codi3a.afegir(Codi.COPY, "-1", null, tRes);
            codi3a.afegirEtiqueta(lEnd);
            return tRes;
        }
        case NOIGUAL: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            String lTrue = codi3a.novaEtiqueta();
            String lEnd = codi3a.novaEtiqueta();
            
            codi3a.afegir(Codi.IF_NE, t1, t2, lTrue);
            codi3a.afegir(Codi.COPY, "0", null, tRes);
            codi3a.afegir(Codi.GOTO, null, null, lEnd);
            codi3a.afegirEtiqueta(lTrue);
            codi3a.afegir(Codi.COPY, "-1", null, tRes);
            codi3a.afegirEtiqueta(lEnd);
            return tRes;
        }
        case MENOR: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            String lTrue = codi3a.novaEtiqueta();
            String lEnd = codi3a.novaEtiqueta();
            
            codi3a.afegir(Codi.IF_LT, t1, t2, lTrue);
            codi3a.afegir(Codi.COPY, "0", null, tRes);
            codi3a.afegir(Codi.GOTO, null, null, lEnd);
            codi3a.afegirEtiqueta(lTrue);
            codi3a.afegir(Codi.COPY, "-1", null, tRes);
            codi3a.afegirEtiqueta(lEnd);
            return tRes;
        }
        case MAJOR: {
            String t1 = e1.generaCodi3a(codi3a);
            String t2 = e2.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            String lTrue = codi3a.novaEtiqueta();
            String lEnd = codi3a.novaEtiqueta();
            
            codi3a.afegir(Codi.IF_GT, t1, t2, lTrue);
            codi3a.afegir(Codi.COPY, "0", null, tRes);
            codi3a.afegir(Codi.GOTO, null, null, lEnd);
            codi3a.afegirEtiqueta(lTrue);
            codi3a.afegir(Codi.COPY, "-1", null, tRes);
            codi3a.afegirEtiqueta(lEnd);
            return tRes;
        }

        // ------ Unaris ------
        case NOT: {
            String t = unari.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            codi3a.afegir(Codi.NOT, t, null, tRes);
            return tRes;
        }
        case UMINUS: {
            String t = unari.generaCodi3a(codi3a);
            String tRes = codi3a.novaTemp();
            codi3a.afegir(Codi.NEG, t, null, tRes);
            return tRes;
        }
        case PAREN:
            return unari.generaCodi3a(codi3a);

        // ------ Fulles ------
        case REF: return ref.generaCodi3a(codi3a);
        case NUM: return num.generaCodi3a(codi3a);
        case CHAR: return charLit.generaCodi3a(codi3a);
        case BOOL: return boolLit.generaCodi3a(codi3a);
        case CRIDA: return crida.generaCodi3a(codi3a);

        default:
            throw new IllegalStateException("Operador no gestionat a generaCodi: " + op);
    }
    }
    

    @Override
    public String toString() {
        switch (op) {   
            case NUM: return "Node_Express(NUM " + num + ")";
            case CHAR: return "Node_Express(CHAR " + charLit + ")";
            case BOOL: return "Node_Express(BOOL " + boolLit + ")";
            case REF: return "Node_Express(REF " + ref + ")";
            case CRIDA: return "Node_Express(CRIDA " + crida + ")";
            case NOT:
            case UMINUS:
            case PAREN: return "Node_Express(" + op + " " + unari + ")";
            default: return "Node_Express(" + op + " " + e1 + ", " + e2 + ")";
        }
    }
    
    

}
