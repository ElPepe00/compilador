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
 *
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
        // Condició ha de ser BOOL (Node_Cond ja ho comprova)
        cond.gestioSemantica(ts);

        // Bloc THEN amb nou àmbit
        TaulaSimbols.entrarBloc();
        if (cosThen != null) {
            cosThen.gestioSemantica(ts);
        }
        TaulaSimbols.sortirBloc();

        // PART FINAL (sense else o amb else)
        if (fi != null) {
            fi.gestioSemantica(ts);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {
        String condTemp = cond.generaCodi3a(codi3a);
        
        String etElse = codi3a.novaEtiqueta();
        String etFi = codi3a.novaEtiqueta();
        
        codi3a.afegir(Codi.IF_EQ, condTemp, "0", etElse); // if cont == 0 goto etElse

        //THEN
        if (cosThen != null) {
            cosThen.generaCodi3a(codi3a);
        }
        
        //ELSE si existeix
        if (fi != null) {
            codi3a.afegir(Codi.GOTO, null, null, etFi);
            codi3a.afegirEtiqueta(etElse);
            
            fi.generaCodi3a(codi3a); //genera el bloc else
            
            codi3a.afegirEtiqueta(etFi);
        } else {
            codi3a.afegirEtiqueta(etElse);
        }
     
        return null;
    }
    

    @Override
    public String toString() {
        return "If_prog(cond=" + cond + ", then=" + cosThen + ", fi=" + fi + ")";
    }
}
