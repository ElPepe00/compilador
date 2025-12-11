; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS ---
STACK_TOP: DS.L   2000   ; Reserva 8KB per la pila
matriu     DS.L   1

; --- SECCIÓ DE CODI ---
    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    1           -           t0
    MOVE.L #1, D0
    MOVE.L D0, -136(A6)

    ; ind_ass t0          0           matriu
    LEA    matriu, A0
    MOVE.L #0, D0
    MOVE.L -136(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    2           -           t1
    MOVE.L #2, D0
    MOVE.L D0, -140(A6)

    ; ind_ass t1          4           matriu
    LEA    matriu, A0
    MOVE.L #4, D0
    MOVE.L -140(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    3           -           t2
    MOVE.L #3, D0
    MOVE.L D0, -144(A6)

    ; ind_ass t2          8           matriu
    LEA    matriu, A0
    MOVE.L #8, D0
    MOVE.L -144(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    4           -           t3
    MOVE.L #4, D0
    MOVE.L D0, -148(A6)

    ; ind_ass t3          12          matriu
    LEA    matriu, A0
    MOVE.L #12, D0
    MOVE.L -148(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    5           -           t4
    MOVE.L #5, D0
    MOVE.L D0, -4(A6)

    ; ind_ass t4          16          matriu
    LEA    matriu, A0
    MOVE.L #16, D0
    MOVE.L -4(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    6           -           t5
    MOVE.L #6, D0
    MOVE.L D0, -8(A6)

    ; ind_ass t5          20          matriu
    LEA    matriu, A0
    MOVE.L #20, D0
    MOVE.L -8(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    0           -           t6
    MOVE.L #0, D0
    MOVE.L D0, -12(A6)

    ; prod    t6          3           t7
    MOVE.L -12(A6), D0
    MULS.W #3, D0
    MOVE.L D0, -16(A6)

    ; copy    0           -           t8
    MOVE.L #0, D0
    MOVE.L D0, -20(A6)

    ; add     t7          t8          t9
    MOVE.L -16(A6), D0
    ADD.L  -20(A6), D0
    MOVE.L D0, -24(A6)

    ; prod    t9          4           t10
    MOVE.L -24(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -32(A6)

    ; param_c t10         matriu      -
    LEA    matriu, A0
    MOVE.L -32(A6), D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    0           -           t11
    MOVE.L #0, D0
    MOVE.L D0, -52(A6)

    ; prod    t11         3           t12
    MOVE.L -52(A6), D0
    MULS.W #3, D0
    MOVE.L D0, -44(A6)

    ; copy    2           -           t13
    MOVE.L #2, D0
    MOVE.L D0, -68(A6)

    ; add     t12         t13         t14
    MOVE.L -44(A6), D0
    ADD.L  -68(A6), D0
    MOVE.L D0, -60(A6)

    ; prod    t14         4           t15
    MOVE.L -60(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -80(A6)

    ; param_c t15         matriu      -
    LEA    matriu, A0
    MOVE.L -80(A6), D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    1           -           t16
    MOVE.L #1, D0
    MOVE.L D0, -76(A6)

    ; prod    t16         3           t17
    MOVE.L -76(A6), D0
    MULS.W #3, D0
    MOVE.L D0, -88(A6)

    ; copy    0           -           t18
    MOVE.L #0, D0
    MOVE.L D0, -84(A6)

    ; add     t17         t18         t19
    MOVE.L -88(A6), D0
    ADD.L  -84(A6), D0
    MOVE.L D0, -92(A6)

    ; prod    t19         4           t20
    MOVE.L -92(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -100(A6)

    ; param_c t20         matriu      -
    LEA    matriu, A0
    MOVE.L -100(A6), D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    1           -           t21
    MOVE.L #1, D0
    MOVE.L D0, -96(A6)

    ; prod    t21         3           t22
    MOVE.L -96(A6), D0
    MULS.W #3, D0
    MOVE.L D0, -108(A6)

    ; copy    2           -           t23
    MOVE.L #2, D0
    MOVE.L D0, -104(A6)

    ; add     t22         t23         t24
    MOVE.L -108(A6), D0
    ADD.L  -104(A6), D0
    MOVE.L D0, -116(A6)

    ; prod    t24         4           t25
    MOVE.L -116(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -112(A6)

    ; param_c t25         matriu      -
    LEA    matriu, A0
    MOVE.L -112(A6), D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    777         -           t26
    MOVE.L #777, D0
    MOVE.L D0, -124(A6)

    ; copy    1           -           t27
    MOVE.L #1, D0
    MOVE.L D0, -120(A6)

    ; prod    t27         3           t28
    MOVE.L -120(A6), D0
    MULS.W #3, D0
    MOVE.L D0, -132(A6)

    ; copy    1           -           t29
    MOVE.L #1, D0
    MOVE.L D0, -128(A6)

    ; add     t28         t29         t30
    MOVE.L -132(A6), D0
    ADD.L  -128(A6), D0
    MOVE.L D0, -28(A6)

    ; prod    t30         4           t31
    MOVE.L -28(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -40(A6)

    ; ind_ass t26         t31         matriu
    LEA    matriu, A0
    MOVE.L -40(A6), D0
    MOVE.L -124(A6), D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    1           -           t32
    MOVE.L #1, D0
    MOVE.L D0, -36(A6)

    ; prod    t32         3           t33
    MOVE.L -36(A6), D0
    MULS.W #3, D0
    MOVE.L D0, -56(A6)

    ; copy    1           -           t34
    MOVE.L #1, D0
    MOVE.L D0, -48(A6)

    ; add     t33         t34         t35
    MOVE.L -56(A6), D0
    ADD.L  -48(A6), D0
    MOVE.L D0, -72(A6)

    ; prod    t35         4           t36
    MOVE.L -72(A6), D0
    MULS.W #4, D0
    MOVE.L D0, -64(A6)

    ; param_c t36         matriu      -
    LEA    matriu, A0
    MOVE.L -64(A6), D0
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
