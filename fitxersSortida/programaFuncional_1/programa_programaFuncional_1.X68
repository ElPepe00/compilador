; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS I TEMPORALS ---
STACK_TOP: DS.L   1000   ; Reserva 4KB per la pila
t0         DS.L   1
max        DS.L   1
t1         DS.L   1
t2         DS.L   1
t3         DS.L   1
t4         DS.L   1
t5         DS.L   1
j          DS.L   1
t6         DS.L   1
t          DS.L   1
t7         DS.L   1
t8         DS.L   1
t9         DS.L   1
c          DS.L   1
t10        DS.L   1
t11        DS.L   1
t12        DS.L   1
t13        DS.L   1

; --- SECCIÓ DE CODI ---
    ; copy    10          -           t0
    MOVE.L #10, D0
    MOVE.L D0, t0

    ; copy    t0          -           max
    MOVE.L t0, D0
    MOVE.L D0, max

    ; skip    -           -           sumar
sumar:

    ; pmb     sumar       12          -
    LINK   A6, #-12

    ; copy    a           -           t1
    MOVE.L a, D0
    MOVE.L D0, t1

    ; copy    b           -           t2
    MOVE.L b, D0
    MOVE.L D0, t2

    ; add     t1          t2          t3
    MOVE.L t1, D0
    ADD.L  t2, D0
    MOVE.L D0, t3

    ; copy    t3          -           res
    MOVE.L t3, D0
    MOVE.L D0, res

    ; copy    res         -           t4
    MOVE.L res, D0
    MOVE.L D0, t4

    ; ret     t4          -           -
    MOVE.L t4, D0
    UNLK   A6
    RTS

    ; ret     -           -           -
    UNLK   A6
    RTS

    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; call    llegir      -           t5
    JSR    llegir
    MOVE.L D0, t5

    ; copy    t5          -           j
    MOVE.L t5, D0
    MOVE.L D0, j

    ; copy    max         -           t6
    MOVE.L max, D0
    MOVE.L D0, t6

    ; copy    t6          -           t
    MOVE.L t6, D0
    MOVE.L D0, t

    ; copy    j           -           t7
    MOVE.L j, D0
    MOVE.L D0, t7

    ; param_s t7          -           -
    MOVE.L t7, -(A7)

    ; copy    t           -           t8
    MOVE.L t, D0
    MOVE.L D0, t8

    ; param_s t8          -           -
    MOVE.L t8, -(A7)

    ; call    sumar       -           t9
    JSR    sumar
    ADD.L  #8, A7
    MOVE.L D0, t9

    ; copy    t9          -           c
    MOVE.L t9, D0
    MOVE.L D0, c

    ; copy    c           -           t10
    MOVE.L c, D0
    MOVE.L D0, t10

    ; copy    50          -           t11
    MOVE.L #50, D0
    MOVE.L D0, t11

    ; if_gt   t10         t11         e0
    MOVE.L t10, D0
    CMP.L  t11, D0
    BGT    e0

    ; copy    0           -           t12
    MOVE.L #0, D0
    MOVE.L D0, t12

    ; goto    -           -           e1
    BRA    e1

    ; skip    -           -           e0
e0:

    ; copy    -1          -           t12
    MOVE.L #-1, D0
    MOVE.L D0, t12

    ; skip    -           -           e1
e1:

    ; if_eq   t12         0           e2
    MOVE.L t12, D0
    CMP.L  #0, D0
    BEQ    e2

    ; copy    c           -           t13
    MOVE.L c, D0
    MOVE.L D0, t13

    ; param_s t13         -           -
    MOVE.L t13, -(A7)

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
