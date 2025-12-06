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

/**
 * Estructura de control IF
 * @author josep
 */
public class Node_If_prog extends Node {

    private Node_Cond cond;
    private Node_Elements cosThen;
    private Node_If_fin fi;

    public Node_If_prog(Node_Cond cond, Node_Elements cosThen, Node_If_fin fi) {
        super("If_prog");
        this.cond = cond;
        this.cosThen = cosThen;
        this.fi = fi;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {
        // 1. Validar la condicio, ha de se booleana
        cond.gestioSemantica(ts);

        // 2. Ambit per el bloc THEN
        ts.entrarBloc();
        if (cosThen != null) {
            cosThen.gestioSemantica(ts);
        }
        ts.sortirBloc();

        // 3. Gestionar la part ELSE
        if (fi != null) {
            fi.gestioSemantica(ts);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        
        // Generar codi de la condicio
        String condTemp = cond.generaCodi3a(codi3a);
        
        String etElse = codi3a.novaEtiqueta();
        String etFi = codi3a.novaEtiqueta();
        
        // Si condicio es 0(false), bota a ELSE(o al final si no hiha else)
        codi3a.afegir(Codi.IF_EQ, condTemp, "0", etElse); // if cont == 0 goto etElse

        // Bloc THEN
        if (cosThen != null) {
            cosThen.generaCodi3a(codi3a);
        }
        
        //Bloc ELSE (si existeix)
        if (fi != null && fi.teElse()) {
            // si hem fet el THEN, botam al final per no fer ELSE
            codi3a.afegir(Codi.GOTO, null, null, etFi);
            
            // etiqueta inici ELSE
            codi3a.afegirEtiqueta(etElse);
            
            fi.generaCodi3a(codi3a); //genera el bloc else
            
            // etiqueta final
            codi3a.afegirEtiqueta(etFi);
        } else {
            // Si no hiha else, aquesta sera la etiqueta de final
            codi3a.afegirEtiqueta(etElse);
        }
     
        return null;
    }
    

    @Override
    public String toString() {
        return "If_prog(cond=" + cond + ", then=" + cosThen + ", fi=" + fi + ")";
    }
}
