OBJECTIU
Desenvolupar un compilador per a un llenguatge imperatiu.

ASPECTES GENERALS
La pràctica podrà ser realitzada en grups de com a molt quatre persones. La conformació de cada grup s’haurà
de fer mitjançant l’apartat corresponent dins de la plana de l’assignatura a l’eina Aula Digital.
La pràctica consisteix en el disseny i implementació d'un compilador per a un llenguatge de programació. Les
tasques que haurà de realitzar el processador són:

  • Les pròpies de la component front-end:
    o Anàlisi lèxica
    o Anàlisi sintàctica
    o Anàlisi semàntica
  • Les pròpies de la component back-end:
    o Generació de codi intermedi
    o Optimització
    o Generació de codi assemblador
  • S’haurà de contemplar necessàriament la implementació d’una taula de símbols i un gestor d'errors.

FUNCIONALITAT DEL PROCESSADOR
El compilador que s’ha de desenvolupar ha de contemplar les següents funcionalitats:
• Haurà de ser capaç de processar el codi font subministrat en un arxiu de text, subministrat pel
programador. Cada execució s’ha de poder fer amb arxius diferents sense que això requereixi
modificar el codi del compilador ni tenir que modificar el nom o el contingut del codi font.

  • Haurà de generar una sèrie de fitxers com a resultat de la seva execució:
    o Pel que fa al front-end:
      ▪ Fitxer de tókens: tots els tókens generats segons la seqüència d’entrada.
      ▪ Taula de símbols: tota la informació de les dades introduïdes a la taula de símbols una
        vegada que s’hagi processat completament el codi font.
    o Pel que fa al back-end:
      ▪ Les taules de variables i de procediments, per tal de poder comprovar la correctesa
        del codi de tres adreces.
      ▪ Fitxer de codi intermedi. El codi intermedi corresponent al programa en codi font
        introduït.
      ▪ Fitxer amb codi assemblador, sense optimitzar. Per a cada instrucció de tres adreces
        es mostrarà un comentari amb la instrucció i, a continuació, la traducció
        corresponent.
      ▪ Fitxer amb codi assemblador, optimitzat. La idea és que l’executable obtingut amb el
        codi optimitzat i el codi sense optimitzar faci el mateix però es pugui veure la
        diferència en el rendiment.
    o Errors: si es detecten errors es generarà un document amb els errors detectats. Indicant per
      a cada error, la línia on s’ha detectat l’error, el tipus d’error (lèxic, sintàctic, semàntic) i un
      missatge explicatiu.
      
CARACTERISTIQUES DEL LLENGUATGE
La següent és la llista amb totes les característiques que ha de tenir el llenguatge. Hi ha algunes, les marcades
amb un asterisc (\*), que no són obligatòries. Altres, les marcades amb un nombre (2,4), tenen una certa
quantitat d’opcions, d’entre les que s’han d’incorporar almenys el nombre indicat.

  • Un cos general de programa on hi hagi d’haver els subprogrames, les declaracions i les instruccions
    del programa (de l’estil del main de java o c++ o un apartat d’instruccions tipus python)
  • Definició de subprogrames: funcions o procediments, amb arguments
  • Tipus:
    o Enter
    o Caràcter
    o Cadena de caràcters(*)
    o Lògic
    o Altres*
  • Tipus definits per l’usuari(1)
    o Tuples
    o Taules amb múltiples dimensions
  • Valors de qualsevulla dels tipus contemplats
    o Declaració i ús de variables
    o Constants
  • Operacions:
    o Assignació
    o Condicional
    o Selecció múltiple (tipus switch)(\*)
    o Bucles(2):
      ▪ while
      ▪ repeat until
      ▪ for
      ▪ altres
    o Crida a procediments i funcions amb paràmetres
    o Retorn de funcions si aquestes s’implementen
  • Expressions aritmètiques i lògiques:
    o Fent ús de literals del tipus adient
    o Fent ús de constants i variables
  • Operacions d’entrada i sortida
    o Entrada per teclat
    o Sortida per pantalla
    o Entrada i sortida des de fitxer(\*)
  • Operadors(6):
    o Aritmètics(2): suma, resta, producte, divisió, mòdul
    o Relacionals(2): igual, diferent, major, menor, major o igual, menor o igual
    o Lògics(2): i, o, no, o-excloent
    o Especials(\*):
  ▪ altres
