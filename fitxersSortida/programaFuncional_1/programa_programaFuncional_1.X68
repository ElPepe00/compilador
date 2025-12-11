; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
max        DS.L   1
j          DS.L   1
t          DS.L   1
c          DS.L   1

; --- SECCIÓ DE CODI ---
    ; copy    10          -           t0
    MOVE.L #10, D0
    MOVE.L D0, -44(A6)

    ; copy    t0          -           max
    MOVE.L -44(A6), D0
    MOVE.L D0, max

    ; skip    -           -           sumar
sumar:

    ; pmb     sumar       12          -
    LINK   A6, #-12

    ; copy    a           -           t1
    MOVE.L 8(A6), D0
    MOVE.L D0, -48(A6)

    ; copy    b           -           t2
    MOVE.L 12(A6), D0
    MOVE.L D0, -52(A6)

    ; add     t1          t2          t3
    MOVE.L -48(A6), D0
    ADD.L  -52(A6), D0
    MOVE.L D0, -56(A6)

    ; copy    t3          -           res
    MOVE.L -56(A6), D0
    MOVE.L D0, -12(A6)

    ; copy    res         -           t4
    MOVE.L -12(A6), D0
    MOVE.L D0, -4(A6)

    ; ret     t4          -           -
    MOVE.L -4(A6), D0
    UNLK   A6
    RTS

    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; call    llegir      -           t5
    JSR    llegir
    MOVE.L D0, -8(A6)

    ; copy    t5          -           j
    MOVE.L -8(A6), D0
    MOVE.L D0, j

    ; copy    max         -           t6
    MOVE.L max, D0
    MOVE.L D0, -12(A6)

    ; copy    t6          -           t
    MOVE.L -12(A6), D0
    MOVE.L D0, t

    ; copy    j           -           t7
    MOVE.L j, D0
    MOVE.L D0, -16(A6)

    ; param_s t7          -           -
    MOVE.L -16(A6), -(A7)

    ; copy    t           -           t8
    MOVE.L t, D0
    MOVE.L D0, -20(A6)

    ; param_s t8          -           -
    MOVE.L -20(A6), -(A7)

    ; call    sumar       -           t9
    JSR    sumar
    ADD.L  #8, A7
    MOVE.L D0, -24(A6)

    ; copy    t9          -           c
    MOVE.L -24(A6), D0
    MOVE.L D0, c

    ; copy    c           -           t10
    MOVE.L c, D0
    MOVE.L D0, -28(A6)

    ; copy    50          -           t11
    MOVE.L #50, D0
    MOVE.L D0, -36(A6)

    ; if_gt   t10         t11         e0
    MOVE.L -28(A6), D0
    CMP.L  -36(A6), D0
    BGT    e0

    ; copy    0           -           t12
    MOVE.L #0, D0
    MOVE.L D0, -32(A6)

    ; goto    -           -           e1
    BRA    e1

    ; skip    -           -           e0
e0:

    ; copy    -1          -           t12
    MOVE.L #-1, D0
    MOVE.L D0, -32(A6)

    ; skip    -           -           e1
e1:

    ; if_eq   t12         0           e2
    MOVE.L -32(A6), D0
    CMP.L  #0, D0
    BEQ    e2

    ; copy    c           -           t13
    MOVE.L c, D0
    MOVE.L D0, -40(A6)

    ; param_s t13         -           -
    MOVE.L -40(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; skip    -           -           e2
e2:

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
