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
public class Node_Assignacio extends Node {

    private Node_LValue lvalue;
    private Node_Express expr;

    public Node_Assignacio(Node_LValue lvalue, Node_Express expr) {
        super("Assignacio");
        this.lvalue = lvalue;
        this.expr = expr;
    }

    @Override
    public void gestioSemantica(TaulaSimbols ts) {

        // 1. Obtenir tipus
        TipusSimbol t_left = lvalue.getTipusSimbolLValue(ts);
        TipusSimbol t_right = expr.getTipusSimbol(ts);

        // 2. Comprovacio de tipus
        if (t_left != t_right) {
            errorSemantic("Tipus incompatibles a l'assignacio: " + t_left + " = " + t_right);
            return;
        }

        // 3. Marcar la variable base com assignada
        Simbol s = lvalue.getRef().getSimbolAssoc();

        // 4. Comprovam que no sigui una constant ni null
        if (s != null) {

            if (s.getCategoria() == CategoriaSimbol.CONSTANT) {
                errorSemantic("No es pot assignar valor a la constant '" + s.getNom() + "'");
            }
            s.setAssignacio(true);
        }
    }

    @Override
    public String generaCodi3a(C3a codi3a) {

        // 1. Genera el codi de l'expressio (part dreta)
        String resExpr = expr.generaCodi3a(codi3a);

        // 2. Recuperam la informacio del destinatari (part esquerra)
        Node_Ref ref = lvalue.getRef();
        Simbol s = ref.getSimbolAssoc();

        if (s == null) {
            throw new RuntimeException("ERROR INTERN: Símbol no trobat a generació de codi (Assignació)");
        }

        String nomDesti = s.getNom();

        if (!ref.teIndex()) {
            codi3a.afegir(Codi.COPY, resExpr, null, nomDesti); // a = expr

        } else {
            String idx = ref.generaCodiIndexAplanat(codi3a);
            codi3a.afegir(Codi.IND_ASS, resExpr, idx, nomDesti); //a[i][j] = expr
        }

        return null;
    }

    @Override
    public String toString() {
        return "Assignacio2(" + lvalue + " = " + expr + ")";
    }
}
