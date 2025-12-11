; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
MAX        DS.L   1
i          DS.L   1
b          DS.L   1

; --- SECCIÓ DE CODI ---
    ; copy    10          -           t0
    MOVE.L #10, D0
    MOVE.L D0, -28(A6)

    ; copy    t0          -           MAX
    MOVE.L -28(A6), D0
    MOVE.L D0, MAX

    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    0           -           t1
    MOVE.L #0, D0
    MOVE.L D0, -32(A6)

    ; copy    t1          -           i
    MOVE.L -32(A6), D0
    MOVE.L D0, i

    ; copy    0           -           t2
    MOVE.L #0, D0
    MOVE.L D0, -36(A6)

    ; copy    t2          -           b
    MOVE.L -36(A6), D0
    MOVE.L D0, b

    ; skip    -           -           e0
e0:

    ; copy    i           -           t3
    MOVE.L i, D0
    MOVE.L D0, -40(A6)

    ; copy    MAX         -           t4
    MOVE.L MAX, D0
    MOVE.L D0, -4(A6)

    ; if_lt   t3          t4          e2
    MOVE.L -40(A6), D0
    CMP.L  -4(A6), D0
    BLT    e2

    ; copy    0           -           t5
    MOVE.L #0, D0
    MOVE.L D0, -8(A6)

    ; goto    -           -           e3
    BRA    e3

    ; skip    -           -           e2
e2:

    ; copy    -1          -           t5
    MOVE.L #-1, D0
    MOVE.L D0, -8(A6)

    ; skip    -           -           e3
e3:

    ; if_eq   t5          0           e1
    MOVE.L -8(A6), D0
    CMP.L  #0, D0
    BEQ    e1

    ; copy    i           -           t6
    MOVE.L i, D0
    MOVE.L D0, -12(A6)

    ; copy    1           -           t7
    MOVE.L #1, D0
    MOVE.L D0, -16(A6)

    ; add     t6          t7          t8
    MOVE.L -12(A6), D0
    ADD.L  -16(A6), D0
    MOVE.L D0, -20(A6)

    ; copy    t8          -           i
    MOVE.L -20(A6), D0
    MOVE.L D0, i

    ; copy    i           -           t9
    MOVE.L i, D0
    MOVE.L D0, -24(A6)

    ; param_s t9          -           -
    MOVE.L -24(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; goto    -           -           e0
    BRA    e0

    ; skip    -           -           e1
e1:

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
