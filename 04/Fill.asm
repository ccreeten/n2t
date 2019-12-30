// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

(MAIN)
    // check key press, fill with black if it is
    @KBD
    D = M
    @WHITE
    D ; JEQ

(BLACK)
    @colour
    M = -1
    @FILL
    0 ; JMP

(WHITE)
    @colour
    M = 0
    @FILL
    0 ; JMP

// fill all screen words with @colour
(FILL)
    @SCREEN
    D = A
    @wordPointer
    M = D

(LOOP)
    // check if we filled all of the screen
    @KBD
    D = A
    @wordPointer
    D = D - M
    @MAIN
    D ; JEQ

    // fill a single word
    @colour
    D = M
    @wordPointer
    A = M
    M = D

    // increment row pointer
    @wordPointer
    M = M + 1

    // next iteration
    @LOOP
    0 ; JMP
