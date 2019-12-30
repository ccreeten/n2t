package n.to.t.assembler.parse.statement;

import java.util.Optional;

public final class Compute implements Statement {

    private final int lineNumber;
    private final String dest;
    private final String comp;
    private final String jump;

    public Compute(final int lineNumber, final String dest, final String comp, final String jump) {
        this.lineNumber = lineNumber;
        this.dest = dest;
        this.comp = comp;
        this.jump = jump;
    }

    public int lineNumber() {
        return lineNumber;
    }

    public Optional<String> dest() {
        return Optional.ofNullable(dest);
    }

    public String comp() {
        return comp;
    }

    public Optional<String> jump() {
        return Optional.ofNullable(jump);
    }
}