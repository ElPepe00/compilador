; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS I TEMPORALS ---
STACK_TOP: DS.L   1000   ; Reserva 4KB per la pila
t0         DS.L   1
vector     DS.L   1
t1         DS.L   1
t2         DS.L   1
t3         DS.L   1
t4         DS.L   1
t5         DS.L   1
val        DS.L   1
t6         DS.L   1
t7         DS.L   1
t8         DS.L   1
t9         DS.L   1
t10        DS.L   1
t11        DS.L   1
t12        DS.L   1
t13        DS.L   1
t14        DS.L   1
t15        DS.L   1

; --- SECCIÓ DE CODI ---
    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    10          -           t0
    MOVE.L #10, D0
    MOVE.L D0, t0

    ; ind_ass t0          0           vector
    LEA    vector, A0    MOVE.L #0, D0
    MOVE.L t0, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    20          -           t1
    MOVE.L #20, D0
    MOVE.L D0, t1

    ; ind_ass t1          1           vector
    LEA    vector, A0    MOVE.L #1, D0
    MOVE.L t1, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    30          -           t2
    MOVE.L #30, D0
    MOVE.L D0, t2

    ; ind_ass t2          2           vector
    LEA    vector, A0    MOVE.L #2, D0
    MOVE.L t2, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    0           -           t3
    MOVE.L #0, D0
    MOVE.L D0, t3

    ; prod    t3          4           t4
    MOVE.L t3, D0
    MULS.L #4, D0
    MOVE.L D0, t4

    ; ind_val vector      t4          t5
    LEA    vector, A0    MOVE.L t4, D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, t5

    ; copy    t5          -           val
    MOVE.L t5, D0
    MOVE.L D0, val

    ; copy    val         -           t6
    MOVE.L val, D0
    MOVE.L D0, t6

    ; param_s t6          -           -
    MOVE.L t6, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    1           -           t7
    MOVE.L #1, D0
    MOVE.L D0, t7

    ; prod    t7          4           t8
    MOVE.L t7, D0
    MULS.L #4, D0
    MOVE.L D0, t8

    ; param_c t8          vector      -
    LEA    vector, A0    MOVE.L t8, D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    2           -           t9
    MOVE.L #2, D0
    MOVE.L D0, t9

    ; prod    t9          4           t10
    MOVE.L t9, D0
    MULS.L #4, D0
    MOVE.L D0, t10

    ; param_c t10         vector      -
    LEA    vector, A0    MOVE.L t10, D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    99          -           t11
    MOVE.L #99, D0
    MOVE.L D0, t11

    ; copy    2           -           t12
    MOVE.L #2, D0
    MOVE.L D0, t12

    ; prod    t12         4           t13
    MOVE.L t12, D0
    MULS.L #4, D0
    MOVE.L D0, t13

    ; ind_ass t11         t13         vector
    LEA    vector, A0    MOVE.L t13, D0
    MOVE.L t11, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    2           -           t14
    MOVE.L #2, D0
    MOVE.L D0, t14

    ; prod    t14         4           t15
    MOVE.L t14, D0
    MULS.L #4, D0
    MOVE.L D0, t15

    ; param_c t15         vector      -
    LEA    vector, A0    MOVE.L t15, D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

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
