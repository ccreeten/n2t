package n.to.t.samples;

import n.to.t.assembler.compile.Compiler;
import n.to.t.assembler.emulate.Emulator;
import n.to.t.assembler.source.Source;

import java.nio.file.Paths;

public final class Samples {

    public static void main(final String... args) throws Exception {
        final var path = "src/main/resources/Pong.asm";

        final var compiler = new Compiler();
        final var source = Source.of(Paths.get(path));
        final var executable = compiler.compile(source);

        final var emulator = new Emulator();
        emulator.run(executable);
    }
}
