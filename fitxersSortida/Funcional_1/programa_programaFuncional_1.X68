; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JMP    __inici
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
SUMAND     DS.L   1
x          DS.L   1
resultat   DS.L   1
t4         DS.L   1
t5         DS.L   1
t6         DS.L   1
t7         DS.L   1
t0         DS.L   1
t1         DS.L   1
t2         DS.L   1
t3         DS.L   1

; --- SECCIÓ DE CODI ---
__inici:
    ; copy    10          -           t0
    MOVE.L #10, D0
    MOVE.L D0, t0

    ; copy    t0          -           SUMAND
    MOVE.L t0, D0
    MOVE.L D0, SUMAND

    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    SUMAND      -           t1
    MOVE.L SUMAND, D0
    MOVE.L D0, t1

    ; param_s t1          -           -
    MOVE.L t1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; call    llegir      -           t2
    JSR    llegir
    MOVE.L D0, t2

    ; copy    t2          -           x
    MOVE.L t2, D0
    MOVE.L D0, x

    ; copy    0           -           t3
    MOVE.L #0, D0
    MOVE.L D0, t3

    ; copy    t3          -           resultat
    MOVE.L t3, D0
    MOVE.L D0, resultat

    ; copy    x           -           t4
    MOVE.L x, D0
    MOVE.L D0, t4

    ; copy    SUMAND      -           t5
    MOVE.L SUMAND, D0
    MOVE.L D0, t5

    ; add     t4          t5          t6
    MOVE.L t4, D0
    ADD.L  t5, D0
    MOVE.L D0, t6

    ; copy    t6          -           resultat
    MOVE.L t6, D0
    MOVE.L D0, resultat

    ; copy    resultat    -           t7
    MOVE.L resultat, D0
    MOVE.L D0, t7

    ; param_s t7          -           -
    MOVE.L t7, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; halt    -           -           -
    SIMHALT

; --- FUNCIONS E/S ---
llegir:
    MOVE.L #4, D0   ; Tasca 4: Llegir Enter (espera Intro)
    TRAP   #15
    MOVE.L D1, D0   ; El resultat es guarda a D1, el movem a D0
    RTS

imprimir:
    MOVE.L 4(A7), D1
    MOVE.L #3, D0
    TRAP   #15
    MOVE.L #13, D1
    MOVE.L #6, D0
    TRAP   #15
    MOVE.L #10, D1
    TRAP   #15
    RTS

    END    START
