package n.to.t.assembler.parse.statement;

import n.to.t.assembler.compile.SymbolTable;

public final class Address implements Statement {

    private final int lineNumber;
    private final String symbol;

    public Address(final int lineNumber, final String symbol) {
        this.lineNumber = lineNumber;
        this.symbol = symbol;
    }

    public int lineNumber() {
        return lineNumber;
    }

    public String symbol() {
        return symbol;
    }

    public int resolve(final SymbolTable table) {
        return symbol().chars().allMatch(Character::isDigit) ? Integer.parseInt(symbol) : table.addressOf(symbol);
    }
}