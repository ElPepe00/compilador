; --- CABECERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
lletres    DS.L   1
i          DS.L   1

; --- SECCIÓ DE CODI ---
    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    65          -           t0
    MOVE.L #65, D0
    MOVE.L D0, -32(A6)

    ; ind_ass t0          0           lletres
    ; ind_ass lletres[0] = t0
    LEA    lletres, A0
    MOVE.L #0, D0
    MOVE.L -32(A6), D1
    MOVE.B D1, 0(A0, D0.L)

    ; copy    66          -           t1
    MOVE.L #66, D0
    MOVE.L D0, -36(A6)

    ; ind_ass t1          1           lletres
    ; ind_ass lletres[1] = t1
    LEA    lletres, A0
    MOVE.L #1, D0
    MOVE.L -36(A6), D1
    MOVE.B D1, 0(A0, D0.L)

    ; copy    67          -           t2
    MOVE.L #67, D0
    MOVE.L D0, -40(A6)

    ; ind_ass t2          2           lletres
    ; ind_ass lletres[2] = t2
    LEA    lletres, A0
    MOVE.L #2, D0
    MOVE.L -40(A6), D1
    MOVE.B D1, 0(A0, D0.L)

    ; copy    0           -           t3
    MOVE.L #0, D0
    MOVE.L D0, -44(A6)

    ; copy    t3          -           i
    MOVE.L -44(A6), D0
    MOVE.L D0, i

    ; skip    -           -           e0
e0:

    ; copy    i           -           t4
    MOVE.L i, D0
    MOVE.L D0, -4(A6)

    ; copy    3           -           t5
    MOVE.L #3, D0
    MOVE.L D0, -8(A6)

    ; if_lt   t4          t5          e2
    MOVE.L -4(A6), D0
    CMP.L  -8(A6), D0
    BLT    e2

    ; copy    0           -           t6
    MOVE.L #0, D0
    MOVE.L D0, -12(A6)

    ; goto    -           -           e3
    BRA    e3

    ; skip    -           -           e2
e2:

    ; copy    -1          -           t6
    MOVE.L #-1, D0
    MOVE.L D0, -12(A6)

    ; skip    -           -           e3
e3:

    ; if_eq   t6          0           e1
    MOVE.L -12(A6), D0
    CMP.L  #0, D0
    BEQ    e1

    ; copy    i           -           t7
    MOVE.L i, D0
    MOVE.L D0, -20(A6)

    ; param_c t7          lletres     -
    LEA    lletres, A0
    MOVE.L -20(A6), D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    i           -           t8
    MOVE.L i, D0
    MOVE.L D0, -24(A6)

    ; copy    1           -           t9
    MOVE.L #1, D0
    MOVE.L D0, -28(A6)

    ; add     t8          t9          t10
    MOVE.L -24(A6), D0
    ADD.L  -28(A6), D0
    MOVE.L D0, -16(A6)

    ; copy    t10         -           i
    MOVE.L -16(A6), D0
    MOVE.L D0, i

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
