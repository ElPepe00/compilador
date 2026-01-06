; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
SUMAND     DS.L   1
x          DS.L   1
resultat   DS.L   1

; --- SECCIÓ DE CODI ---
    ; copy    10          -           t0
    MOVE.L #10, D0
    MOVE.L D0, -16(A6)

    ; copy    t0          -           SUMAND
    MOVE.L -16(A6), D0
    MOVE.L D0, SUMAND

    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; call    llegir      -           t1
    JSR    llegir
    MOVE.L D0, -20(A6)

    ; copy    t1          -           x
    MOVE.L -20(A6), D0
    MOVE.L D0, x

    ; copy    0           -           t2
    MOVE.L #0, D0
    MOVE.L D0, -24(A6)

    ; copy    t2          -           resultat
    MOVE.L -24(A6), D0
    MOVE.L D0, resultat

    ; copy    x           -           t3
    MOVE.L x, D0
    MOVE.L D0, -28(A6)

    ; copy    SUMAND      -           t4
    MOVE.L SUMAND, D0
    MOVE.L D0, -4(A6)

    ; add     t3          t4          t5
    MOVE.L -28(A6), D0
    ADD.L  -4(A6), D0
    MOVE.L D0, -8(A6)

    ; copy    t5          -           resultat
    MOVE.L -8(A6), D0
    MOVE.L D0, resultat

    ; copy    resultat    -           t6
    MOVE.L resultat, D0
    MOVE.L D0, -12(A6)

    ; param_s t6          -           -
    MOVE.L -12(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; halt    -           -           -
    SIMHALT

; --- FUNCIONS E/S ---
llegir:
    MOVE.L #4, D0
    TRAP   #15
    MOVE.L D1, D0
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
