; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
a          DS.L   1
b          DS.L   1
c          DS.L   1
lec        DS.L   1
x          DS.L   1
y          DS.L   1
z          DS.L   1

; --- SECCIÓ DE CODI ---
    ; copy    3           -           t0
    MOVE.L #3, D0
    MOVE.L D0, -32(A6)

    ; copy    t0          -           a
    MOVE.L -32(A6), D0
    MOVE.L D0, a

    ; copy    -1          -           t1
    MOVE.L #-1, D0
    MOVE.L D0, -36(A6)

    ; copy    t1          -           b
    MOVE.L -36(A6), D0
    MOVE.L D0, b

    ; copy    109         -           t2
    MOVE.L #109, D0
    MOVE.L D0, -40(A6)

    ; copy    t2          -           c
    MOVE.L -40(A6), D0
    MOVE.L D0, c

    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    a           -           t3
    MOVE.L a, D0
    MOVE.L D0, -44(A6)

    ; param_s t3          -           -
    MOVE.L -44(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; call    llegir      -           t4
    JSR    llegir
    MOVE.L D0, -4(A6)

    ; copy    t4          -           lec
    MOVE.L -4(A6), D0
    MOVE.L D0, lec

    ; copy    42          -           t5
    MOVE.L #42, D0
    MOVE.L D0, -8(A6)

    ; copy    t5          -           x
    MOVE.L -8(A6), D0
    MOVE.L D0, x

    ; copy    0           -           t6
    MOVE.L #0, D0
    MOVE.L D0, -12(A6)

    ; copy    t6          -           y
    MOVE.L -12(A6), D0
    MOVE.L D0, y

    ; copy    10          -           t7
    MOVE.L #10, D0
    MOVE.L D0, -20(A6)

    ; copy    t7          -           z
    MOVE.L -20(A6), D0
    MOVE.L D0, z

    ; copy    x           -           t8
    MOVE.L x, D0
    MOVE.L D0, -24(A6)

    ; param_s t8          -           -
    MOVE.L -24(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    y           -           t9
    MOVE.L y, D0
    MOVE.L D0, -28(A6)

    ; param_s t9          -           -
    MOVE.L -28(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    z           -           t10
    MOVE.L z, D0
    MOVE.L D0, -16(A6)

    ; param_s t10         -           -
    MOVE.L -16(A6), -(A7)

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
