; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
x          DS.L   1
y          DS.L   1
total      DS.L   1

; --- SECCIÓ DE CODI ---
    ; skip    -           -           sumar
sumar:

    ; pmb     sumar       12          -
    LINK   A6, #-12

    ; copy    a           -           t0
    MOVE.L 8(A6), D0
    MOVE.L D0, -44(A6)

    ; copy    b           -           t1
    MOVE.L 12(A6), D0
    MOVE.L D0, -48(A6)

    ; add     t0          t1          t2
    MOVE.L -44(A6), D0
    ADD.L  -48(A6), D0
    MOVE.L D0, -52(A6)

    ; copy    t2          -           res
    MOVE.L -52(A6), D0
    MOVE.L D0, -12(A6)

    ; copy    res         -           t3
    MOVE.L -12(A6), D0
    MOVE.L D0, -56(A6)

    ; ret     t3          -           -
    MOVE.L -56(A6), D0
    UNLK   A6
    RTS

    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    15          -           t4
    MOVE.L #15, D0
    MOVE.L D0, -4(A6)

    ; copy    t4          -           x
    MOVE.L -4(A6), D0
    MOVE.L D0, x

    ; copy    10          -           t5
    MOVE.L #10, D0
    MOVE.L D0, -8(A6)

    ; copy    t5          -           y
    MOVE.L -8(A6), D0
    MOVE.L D0, y

    ; copy    x           -           t6
    MOVE.L x, D0
    MOVE.L D0, -12(A6)

    ; param_s t6          -           -
    MOVE.L -12(A6), -(A7)

    ; copy    y           -           t7
    MOVE.L y, D0
    MOVE.L D0, -16(A6)

    ; param_s t7          -           -
    MOVE.L -16(A6), -(A7)

    ; call    sumar       -           t8
    JSR    sumar
    ADD.L  #8, A7
    MOVE.L D0, -20(A6)

    ; copy    t8          -           total
    MOVE.L -20(A6), D0
    MOVE.L D0, total

    ; copy    total       -           t9
    MOVE.L total, D0
    MOVE.L D0, -24(A6)

    ; copy    20          -           t10
    MOVE.L #20, D0
    MOVE.L D0, -28(A6)

    ; if_gt   t9          t10         e0
    MOVE.L -24(A6), D0
    CMP.L  -28(A6), D0
    BGT    e0

    ; copy    0           -           t11
    MOVE.L #0, D0
    MOVE.L D0, -36(A6)

    ; goto    -           -           e1
    BRA    e1

    ; skip    -           -           e0
e0:

    ; copy    -1          -           t11
    MOVE.L #-1, D0
    MOVE.L D0, -36(A6)

    ; skip    -           -           e1
e1:

    ; if_eq   t11         0           e2
    MOVE.L -36(A6), D0
    CMP.L  #0, D0
    BEQ    e2

    ; copy    -1          -           t12
    MOVE.L #-1, D0
    MOVE.L D0, -32(A6)

    ; param_s t12         -           -
    MOVE.L -32(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; goto    -           -           e3
    BRA    e3

    ; skip    -           -           e2
e2:

    ; copy    0           -           t13
    MOVE.L #0, D0
    MOVE.L D0, -40(A6)

    ; param_s t13         -           -
    MOVE.L -40(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; skip    -           -           e3
e3:

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
