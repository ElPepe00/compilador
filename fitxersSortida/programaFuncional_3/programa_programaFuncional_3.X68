; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
vector     DS.L   1
val        DS.L   1

; --- SECCIÓ DE CODI ---
    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    10          -           t0
    MOVE.L #10, D0
    MOVE.L D0, -52(A6)

    ; ind_ass t0          0           vector
    LEA    vector, A0
    MOVE.L #0, D0
    MOVE.L -52(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    20          -           t1
    MOVE.L #20, D0
    MOVE.L D0, -56(A6)

    ; ind_ass t1          4           vector
    LEA    vector, A0
    MOVE.L #4, D0
    MOVE.L -56(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    30          -           t2
    MOVE.L #30, D0
    MOVE.L D0, -60(A6)

    ; ind_ass t2          8           vector
    LEA    vector, A0
    MOVE.L #8, D0
    MOVE.L -60(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    0           -           t3
    MOVE.L #0, D0
    MOVE.L D0, -64(A6)

    ; prod    t3          4           t4
    MOVE.L -64(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -4(A6)

    ; ind_val vector      t4          t5
    LEA    vector, A0
    MOVE.L -4(A6), D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -8(A6)

    ; copy    t5          -           val
    MOVE.L -8(A6), D0
    MOVE.L D0, val

    ; copy    val         -           t6
    MOVE.L val, D0
    MOVE.L D0, -12(A6)

    ; param_s t6          -           -
    MOVE.L -12(A6), -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    1           -           t7
    MOVE.L #1, D0
    MOVE.L D0, -16(A6)

    ; prod    t7          4           t8
    MOVE.L -16(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -20(A6)

    ; param_c t8          vector      -
    LEA    vector, A0
    MOVE.L -20(A6), D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    2           -           t9
    MOVE.L #2, D0
    MOVE.L D0, -24(A6)

    ; prod    t9          4           t10
    MOVE.L -24(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -28(A6)

    ; param_c t10         vector      -
    LEA    vector, A0
    MOVE.L -28(A6), D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    99          -           t11
    MOVE.L #99, D0
    MOVE.L D0, -36(A6)

    ; copy    2           -           t12
    MOVE.L #2, D0
    MOVE.L D0, -32(A6)

    ; prod    t12         4           t13
    MOVE.L -32(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -44(A6)

    ; ind_ass t11         t13         vector
    LEA    vector, A0
    MOVE.L -44(A6), D0
    MOVE.L -36(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    2           -           t14
    MOVE.L #2, D0
    MOVE.L D0, -40(A6)

    ; prod    t14         4           t15
    MOVE.L -40(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -48(A6)

    ; param_c t15         vector      -
    LEA    vector, A0
    MOVE.L -48(A6), D0
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
