package n.to.t.assembler.compile;

import java.util.HashMap;
import java.util.Map;

public final class SymbolTable {

    private final Map<String, Integer> table = new HashMap<>();
    private int variableAddress;

    private SymbolTable(final int baseVariableAddress) {
        this.variableAddress = baseVariableAddress;
    }

    public static SymbolTable withInitialVariableAddress(final int address) {
        return new SymbolTable(address);
    }

    public SymbolTable addPredefined(final String symbol, final int address) {
        table.put(symbol, address);
        return this;
    }

    public int addLabel(final String symbol, final int address) {
        return table.computeIfAbsent(symbol, __ -> address);
    }

    public int addressOf(final String symbol) {
        return table.computeIfAbsent(symbol, __ -> variableAddress++);
    }
}
