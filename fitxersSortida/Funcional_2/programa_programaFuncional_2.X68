; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JMP    __inici
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
x          DS.L   1
y          DS.L   1
total      DS.L   1
t4         DS.L   1
t5         DS.L   1
t6         DS.L   1
t7         DS.L   1
t8         DS.L   1
t9         DS.L   1
t10        DS.L   1
t12        DS.L   1
t11        DS.L   1
t14        DS.L   1
t13        DS.L   1
t0         DS.L   1
t1         DS.L   1
t2         DS.L   1
t3         DS.L   1

; --- SECCIÓ DE CODI ---
__inici:
    ; skip    -           -           sumar
    BRA    main   ; Saltem funcions inicials per anar al main
sumar:

    ; pmb     sumar       12          -
    LINK   A6, #-12

    ; copy    a           -           t0
    MOVE.L 8(A6), D0
    MOVE.L D0, t0

    ; copy    b           -           t1
    MOVE.L 12(A6), D0
    MOVE.L D0, t1

    ; add     t0          t1          t2
    MOVE.L t0, D0
    ADD.L  t1, D0
    MOVE.L D0, t2

    ; copy    t2          -           res
    MOVE.L t2, D0
    MOVE.L D0, -12(A6)

    ; copy    res         -           t3
    MOVE.L -12(A6), D0
    MOVE.L D0, t3

    ; ret     t3          -           -
    MOVE.L t3, D0
    UNLK   A6
    RTS

    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    15          -           t4
    MOVE.L #15, D0
    MOVE.L D0, t4

    ; copy    t4          -           x
    MOVE.L t4, D0
    MOVE.L D0, x

    ; copy    x           -           t5
    MOVE.L x, D0
    MOVE.L D0, t5

    ; param_s t5          -           -
    MOVE.L t5, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; call    llegir      -           t6
    JSR    llegir
    MOVE.L D0, t6

    ; copy    t6          -           y
    MOVE.L t6, D0
    MOVE.L D0, y

    ; copy    x           -           t7
    MOVE.L x, D0
    MOVE.L D0, t7

    ; param_s t7          -           -
    MOVE.L t7, -(A7)

    ; copy    y           -           t8
    MOVE.L y, D0
    MOVE.L D0, t8

    ; param_s t8          -           -
    MOVE.L t8, -(A7)

    ; call    sumar       -           t9
    JSR    sumar
    ADD.L  #8, A7
    MOVE.L D0, t9

    ; copy    t9          -           total
    MOVE.L t9, D0
    MOVE.L D0, total

    ; copy    total       -           t10
    MOVE.L total, D0
    MOVE.L D0, t10

    ; copy    20          -           t11
    MOVE.L #20, D0
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

    ; copy    -1          -           t13
    MOVE.L #-1, D0
    MOVE.L D0, t13

    ; param_s t13         -           -
    MOVE.L t13, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; goto    -           -           e3
    BRA    e3

    ; skip    -           -           e2
e2:

    ; copy    0           -           t14
    MOVE.L #0, D0
    MOVE.L D0, t14

    ; param_s t14         -           -
    MOVE.L t14, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; skip    -           -           e3
e3:

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
