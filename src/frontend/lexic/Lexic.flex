/*----------------------------------------------------------------------------*
 *  Lexic.flex - Analitzador Lèxic (JFlex)
 *----------------------------------------------------------------------------*/

 /*
  * Autor: Josep Oliver Vallespir
  * DNI: 78222663P
  * Curs: 2025-2026
  * Assignatura: Compiladors I
  */

package frontend.lexic;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java.util.ArrayList;
import frontend.gestor_errors.*;
import frontend.sintactic.sym;

%%

%cup
%public
%class Scanner
%line
%column

%eofval{
  return symbol(sym.EOF);
%eofval}

%{
	// Variable que emmagatzema la linia actual de l'anàlisi lèxic
	public int line = yyline;
	// Variable que emmagatzema la columna actual de l'anàlisi lèxic
	public int column = yycolumn;
	// Llista que emmagatzema tots els symbols reconeguts com objecte ComplexSymbol
	public ArrayList<ComplexSymbol> tokens = new ArrayList<>(); 

	// Mètode que serveix per emmagatzemar la posicio dins el codi del token
	// passant-li només el type
	private ComplexSymbol symbol(int type) {
		// Posicio inicial i final del token
		Location inici = new Location(yyline + 1, yycolumn + 1, (int)yychar);
		Location fi = new Location(yyline + 1, yycolumn + yylength(), (int)yychar + yylength());
		// Cream el complexSymbol
		ComplexSymbol cs = new ComplexSymbol(sym.terminalNames[type], type, inici, fi);
		// Guardam les coordenades del token
		cs.left = yyline + 1;
		cs.right = yycolumn;
		// Afegim el token a la llista de tokens reconeguts
		tokens.add(cs);
		return cs;
	}
	
	// Mètode que serveix per emmagatzemar la posicio dins el codi del token
	// passant-li el type i el valor de l'objecte
	private ComplexSymbol symbol(int type, Object value) {
		// Posicio inicial i final del token
		Location inici = new Location(yyline + 1, yycolumn + 1, (int)yychar);
		Location fi = new Location(yyline + 1, yycolumn + yylength(), (int)yychar + yylength());
		// Cream el complexSymbol, amb el value
		ComplexSymbol cs = new ComplexSymbol(sym.terminalNames[type], type, inici, fi, value);
		// Guardam les coordenades del token
		cs.left = yyline + 1;
		cs.right = yycolumn;
		// Afegim el token a la llista de tokens reconeguts
		tokens.add(cs);
		return cs;
	}

	// Mètode que emmagatzema els errors lèxics
	// Tipus: Lexic, Linia = yyline+1, Columna = yycolumn, Error: "text"
	private void errorLexic(){
		GestorError.afegirError(new MissatgeError(TipusError.LEXIC, 
													yyline + 1,
													yycolumn,
													"Error de token mentre es feia el parseig"));
	}
%}

/*---------------------------------------------*/
/*    			DECLARACIONS 			       */
/*---------------------------------------------*/
IDENTIFICADOR   = [A-Za-z_][A-Za-z0-9_]*
NUMERO          = 0|[1-9][0-9]*
CARACTER    	= "'" ( "\\" . | [^\\'\r\n] ) "'"

ESPAI           = [ \t\r\n]+
COMENT_LINIA    = "//" [^\r\n]*

/*---------------------------------------------*/
/*             SECCIÓ DE NORMES                */
/*---------------------------------------------*/
%%
/* Inici i fi de programa */
"programa"		{ return symbol(sym.PROGRAMA); }
"programa_fi"   { return symbol(sym.PROGRAMA_FI); }

/* Subprograma */
"funcio"      	{ return symbol(sym.FUNCIO); }
"funcio_fi"     { return symbol(sym.FUNCIO_FI); }
"return"        { return symbol(sym.RETURN); }

/* Condicional i bucles */
"if"          	{ return symbol(sym.IF); }
"if_fi"         { return symbol(sym.IF_FI); }
"else"         	{ return symbol(sym.ELSE); }
"else_fi"       { return symbol(sym.ELSE_FI); }
"while"         { return symbol(sym.WHILE); }
"while_fi"      { return symbol(sym.WHILE_FI); }
"do"			{ return symbol(sym.DO); }
"dowhile"		{ return symbol(sym.DOWHILE); }

/* Entrada i sortida */
"llegir"        { return symbol(sym.LLEGIR); }
"imprimir"    	{ return symbol(sym.IMPRIMIR); }

/* Tipus de dada*/
"constant"		{ return symbol(sym.CONSTANT); }
"int"          	{ return symbol(sym.INT); }
"char"			{ return symbol(sym.CHAR); }
"bool"         	{ return symbol(sym.BOOL); }
"taula"			{ return symbol(sym.TAULA); }

/* Operadors relacionals */
"==" 			{ return symbol(sym.IGUAL); }
"!="			{ return symbol(sym.NOIGUAL);}
"<"  			{ return symbol(sym.MENOR); }
">"  			{ return symbol(sym.MAJOR); }
//"!=" 			{ return symbol(sym.NE); }
//"<=" 			{ return symbol(sym.LE); }
//">=" 			{ return symbol(sym.GE); }

/* Operadors lògics */
"||" 			{ return symbol(sym.OR); }
"&&" 			{ return symbol(sym.AND); }
"not"  			{ return symbol(sym.NOT); }

/* Operadors aritmetics */
"+"  			{ return symbol(sym.SUMA); }
"-" 			{ return symbol(sym.RESTA); }
"*"  			{ return symbol(sym.MULT); }
"/" 			{ return symbol(sym.DIV); }

/* Operador assignacio */
"="  			{ return symbol(sym.ASSIGN); }

/* Delimitadors */
"," 			{ return symbol(sym.COMA); }
"(" 			{ return symbol(sym.LPAREN); }
")" 			{ return symbol(sym.RPAREN); }
"[" 			{ return symbol(sym.LBRACKET); }
"]" 			{ return symbol(sym.RBRACKET); }

/* Començament i Fi de linea */
":"				{ return symbol(sym.BEGIN); }
";" 			{ return symbol(sym.FILINEA); }

"true"        	{ return symbol(sym.BTRUE,  Boolean.TRUE); }
"false"       	{ return symbol(sym.BFALSE, Boolean.FALSE); }

/*---------------------------------------------*/
/* Espais i comentaris */
{ESPAI}      		{ /* ignora */ }
{COMENT_LINIA} 		{ /* ignora */ }

/* Identificadors i literals */
{IDENTIFICADOR}    	{ return symbol(sym.ID, this.yytext()); }
{NUMERO}           	{ return symbol(sym.NUMERO, this.yytext()); }
{CARACTER}         	{
						String s = yytext(); // obtenim el text del token reconegut
						char c;
						// comprovam que sigui un caràcter especial tipus "\n, \t, \r, \\, \'"
						if (s.charAt(1) == '\\') {
							switch (s.charAt(2)) { // miram el tercer element de l'string
								case 'n': c = '\n'; break;
								case 't': c = '\t'; break;
								case 'r': c = '\r'; break;
								case '\\': c = '\\'; break;
								case '\'': c = '\''; break;
								default:  c = s.charAt(2);
							}
						} else { 
							c = s.charAt(1); //si no hiha barra, és un caràcter normal
						}
						return symbol(sym.CARACTER, Character.valueOf(c));
					}

/* Error Lèxic */
[^]                	{ errorLexic(); }



/* PROGRAMA D'EXEMPLE:

constant int max = 5;

funcio mostrar_valor(char c):
	imprimir(c);
funcio_fi

funcio int sumar(int a, int b):
	int c = 0;
	c = a + b;
	return c;
funcio_fi

programa:

	int j = 0;
	int suma = 0;
	
	while (j < max):
		imprimir(j);
		j = j+1;
	while_fi
	
	int p = llegir();
	int res = sumar(p, j);
	
	if (p == res):
		imprimir(true);
	else:
		imprimir(false);
	else_fi

programa_fi
*/

/*
constant int max = 10;

funcio int sumar(int a, int b):
	return a+b;
funcio_fi

programa:

	int j = llegir();
	int t = max;
	int c;
	
	c = sumar(j, t);
	
	if (c > 50):
		imprimir(c);
	if_fi

programa_fi
*/

// sumar(taula a[], int b);
// taula pere : int, int :