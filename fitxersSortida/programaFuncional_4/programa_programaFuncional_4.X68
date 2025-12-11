; --- CAPÇALERA ---
    ORG    $1000
START:
    LEA    STACK_TOP, A7
    JSR    main
    SIMHALT

; --- DADES GLOBALS I TEMPORALS ---
STACK_TOP: DS.L   1000   ; Reserva 4KB per la pila
t0         DS.L   1
matriu     DS.L   1
t1         DS.L   1
t2         DS.L   1
t3         DS.L   1
t4         DS.L   1
t5         DS.L   1
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
t16        DS.L   1
t17        DS.L   1
t18        DS.L   1
t19        DS.L   1
t20        DS.L   1
t21        DS.L   1
t22        DS.L   1
t23        DS.L   1
t24        DS.L   1
t25        DS.L   1
t26        DS.L   1
t27        DS.L   1
t28        DS.L   1
t29        DS.L   1
t30        DS.L   1
t31        DS.L   1
t32        DS.L   1
t33        DS.L   1
t34        DS.L   1
t35        DS.L   1
t36        DS.L   1

; --- SECCIÓ DE CODI ---
    ; skip    -           -           main
main:

    ; pmb     main        0           -
    LINK   A6, #-0

    ; copy    1           -           t0
    MOVE.L #1, D0
    MOVE.L D0, t0

    ; ind_ass t0          0           matriu
    LEA    matriu, A0    MOVE.L #0, D0
    MOVE.L t0, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    2           -           t1
    MOVE.L #2, D0
    MOVE.L D0, t1

    ; ind_ass t1          1           matriu
    LEA    matriu, A0    MOVE.L #1, D0
    MOVE.L t1, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    3           -           t2
    MOVE.L #3, D0
    MOVE.L D0, t2

    ; ind_ass t2          2           matriu
    LEA    matriu, A0    MOVE.L #2, D0
    MOVE.L t2, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    4           -           t3
    MOVE.L #4, D0
    MOVE.L D0, t3

    ; ind_ass t3          3           matriu
    LEA    matriu, A0    MOVE.L #3, D0
    MOVE.L t3, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    5           -           t4
    MOVE.L #5, D0
    MOVE.L D0, t4

    ; ind_ass t4          4           matriu
    LEA    matriu, A0    MOVE.L #4, D0
    MOVE.L t4, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    6           -           t5
    MOVE.L #6, D0
    MOVE.L D0, t5

    ; ind_ass t5          5           matriu
    LEA    matriu, A0    MOVE.L #5, D0
    MOVE.L t5, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    0           -           t6
    MOVE.L #0, D0
    MOVE.L D0, t6

    ; prod    t6          3           t7
    MOVE.L t6, D0
    MULS.L #3, D0
    MOVE.L D0, t7

    ; copy    0           -           t8
    MOVE.L #0, D0
    MOVE.L D0, t8

    ; add     t7          t8          t9
    MOVE.L t7, D0
    ADD.L  t8, D0
    MOVE.L D0, t9

    ; prod    t9          4           t10
    MOVE.L t9, D0
    MULS.L #4, D0
    MOVE.L D0, t10

    ; param_c t10         matriu      -
    LEA    matriu, A0    MOVE.L t10, D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    0           -           t11
    MOVE.L #0, D0
    MOVE.L D0, t11

    ; prod    t11         3           t12
    MOVE.L t11, D0
    MULS.L #3, D0
    MOVE.L D0, t12

    ; copy    2           -           t13
    MOVE.L #2, D0
    MOVE.L D0, t13

    ; add     t12         t13         t14
    MOVE.L t12, D0
    ADD.L  t13, D0
    MOVE.L D0, t14

    ; prod    t14         4           t15
    MOVE.L t14, D0
    MULS.L #4, D0
    MOVE.L D0, t15

    ; param_c t15         matriu      -
    LEA    matriu, A0    MOVE.L t15, D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    1           -           t16
    MOVE.L #1, D0
    MOVE.L D0, t16

    ; prod    t16         3           t17
    MOVE.L t16, D0
    MULS.L #3, D0
    MOVE.L D0, t17

    ; copy    0           -           t18
    MOVE.L #0, D0
    MOVE.L D0, t18

    ; add     t17         t18         t19
    MOVE.L t17, D0
    ADD.L  t18, D0
    MOVE.L D0, t19

    ; prod    t19         4           t20
    MOVE.L t19, D0
    MULS.L #4, D0
    MOVE.L D0, t20

    ; param_c t20         matriu      -
    LEA    matriu, A0    MOVE.L t20, D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    1           -           t21
    MOVE.L #1, D0
    MOVE.L D0, t21

    ; prod    t21         3           t22
    MOVE.L t21, D0
    MULS.L #3, D0
    MOVE.L D0, t22

    ; copy    2           -           t23
    MOVE.L #2, D0
    MOVE.L D0, t23

    ; add     t22         t23         t24
    MOVE.L t22, D0
    ADD.L  t23, D0
    MOVE.L D0, t24

    ; prod    t24         4           t25
    MOVE.L t24, D0
    MULS.L #4, D0
    MOVE.L D0, t25

    ; param_c t25         matriu      -
    LEA    matriu, A0    MOVE.L t25, D0
    MOVE.L 0(A0, D0.L), D1
    MOVE.L D1, -(A7)

    ; call    imprimir    -           -
    JSR    imprimir
    ADD.L  #4, A7

    ; copy    777         -           t26
    MOVE.L #777, D0
    MOVE.L D0, t26

    ; copy    1           -           t27
    MOVE.L #1, D0
    MOVE.L D0, t27

    ; prod    t27         3           t28
    MOVE.L t27, D0
    MULS.L #3, D0
    MOVE.L D0, t28

    ; copy    1           -           t29
    MOVE.L #1, D0
    MOVE.L D0, t29

    ; add     t28         t29         t30
    MOVE.L t28, D0
    ADD.L  t29, D0
    MOVE.L D0, t30

    ; prod    t30         4           t31
    MOVE.L t30, D0
    MULS.L #4, D0
    MOVE.L D0, t31

    ; ind_ass t26         t31         matriu
    LEA    matriu, A0    MOVE.L t31, D0
    MOVE.L t26, D1
    MOVE.L D1, 0(A0, D0.L)

    ; copy    1           -           t32
    MOVE.L #1, D0
    MOVE.L D0, t32

    ; prod    t32         3           t33
    MOVE.L t32, D0
    MULS.L #3, D0
    MOVE.L D0, t33

    ; copy    1           -           t34
    MOVE.L #1, D0
    MOVE.L D0, t34

    ; add     t33         t34         t35
    MOVE.L t33, D0
    ADD.L  t34, D0
    MOVE.L D0, t35

    ; prod    t35         4           t36
    MOVE.L t35, D0
    MULS.L #4, D0
    MOVE.L D0, t36

    ; param_c t36         matriu      -
    LEA    matriu, A0    MOVE.L t36, D0
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
