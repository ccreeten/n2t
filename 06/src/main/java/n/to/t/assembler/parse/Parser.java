package n.to.t.assembler.parse;

import n.to.t.assembler.parse.statement.*;
import n.to.t.assembler.source.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;

public final class Parser {

    private static final Pattern COMMENT = compile("(?<uncommented>.*?)//.*");
    private static final Pattern LABEL = compile("\\((?<symbol>.*?)\\)");
    private static final Pattern ADDRESS = compile("@(?<symbol>.*)");
    private static final Pattern COMPUTE = compile("((?<dest>.*)=)?(?<comp>.*?)(;(?<jump>.*))?");
    private static final Pattern WHITE_SPACE = compile("\\s");

    public List<Label> parseLabels(final Source source) {
        return parse(source).stream()
                .filter(Statement::isLabel)
                .map(Statement::asLabel)
                .collect(toList());
    }

    public List<Statement> parse(final Source source) {
        final var statements = new ArrayList<Statement>();

        var lineNumber = 0;
        var instructionAddress = 0;

        for (final var line : source.lines()) {
            final var statement = parse(++lineNumber, instructionAddress, line);
            if (statement.isInstruction()) {
                instructionAddress++;
            }
            statements.add(statement);
        }
        return statements;
    }

    private Statement parse(final int lineNumber, final int instructionAddress, final String line) {
        final var statement = WHITE_SPACE.matcher(line).replaceAll("");
        if (statement.isBlank()) {
            return new None();
        }

        final var comment = COMMENT.matcher(statement);
        if (comment.matches()) {
            return parse(lineNumber, instructionAddress, comment.group("uncommented"));
        }
        final var label = LABEL.matcher(statement);
        if (label.matches()) {
            return new Label(lineNumber, label.group("symbol"), instructionAddress);
        }
        final var address = ADDRESS.matcher(statement);
        if (address.matches()) {
            return new Address(lineNumber, address.group("symbol"));
        }
        final var compute = COMPUTE.matcher(statement);
        if (compute.matches()) {
            return new Compute(lineNumber, compute.group("dest"), compute.group("comp"), compute.group("jump"));
        }
        throw new UnsupportedOperationException("Could not parse line: " + statement);
    }
}
