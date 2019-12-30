package n.to.t.assembler;

import n.to.t.assembler.compile.Compiler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;

public final class Main {

    public static void main(final String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("At least one file argument expected, usage:\n    assembler file [file...]");
            return;
        }
        final var compiler = new Compiler();
        for (final var arg : args) {
            final var source = Paths.get(arg);
            compiler.compile(source, targetFileFor(source));
        }
    }

    private static Path targetFileFor(final Path source) throws IOException {
        final var targetFile = source.resolveSibling(source.getFileName().toString().replace(".asm", ".hack"));
        return !exists(targetFile) ? createFile(targetFile) : targetFile;
    }
}
