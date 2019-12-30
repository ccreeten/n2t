// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

    // set result to 0
    @R2
    M = 0

(LOOP)
    // check if R1 is down to 0
    @R1
    D = M
    @END
    D ; JEQ

    // add R0 to R2
    @R0
    D = M
    @R2
    M = D + M

    // decrement R1
    @R1
    M = M - 1

    // next iteration
    @LOOP
    0 ; JMP

(END)
    @END
    0 ; JMP
