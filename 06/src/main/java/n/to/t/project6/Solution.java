package n.to.t.project6;

import n.to.t.assembler.Main;

public final class Solution {

    public static void main(final String... args) throws Exception {
        Main.main(
                "src/main/resources/Add.asm",
                "src/main/resources/Max.asm",
                "src/main/resources/Pong.asm",
                "src/main/resources/Rect.asm"
        );
    }
}
