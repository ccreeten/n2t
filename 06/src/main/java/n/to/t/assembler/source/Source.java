package n.to.t.assembler.source;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Source {

    private final Path path;

    private Source(final Path path) {
        this.path = path;
    }

    public static Source of(final Path path) {
        return new Source(path);
    }

    public Iterable<String> lines() {
        try {
            return Files.lines(path)::iterator;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
