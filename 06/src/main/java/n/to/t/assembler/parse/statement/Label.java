package n.to.t.assembler.parse.statement;

public final class Label implements Statement  {

    private final int lineNumber;
    private final String symbol;
    private final int address;

    public Label(final int lineNumber, final String symbol, final int instructionAddress) {
        this.lineNumber = lineNumber;
        this.symbol = symbol;
        this.address = instructionAddress;
    }

    public int lineNumber() {
        return lineNumber;
    }

    public String symbol() {
        return symbol;
    }

    public int address() {
        return address;
    }
}



