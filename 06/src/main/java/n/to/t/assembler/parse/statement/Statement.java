package n.to.t.assembler.parse.statement;

public interface Statement {

    default boolean isA(final Class<? extends Statement> type) {
        return type.isInstance(this);
    }

    default <T extends Statement> T asA(final Class<T> type) {
        return type.cast(this);
    }

    default boolean isLabel() {
        return isA(Label.class);
    }

    default Label asLabel() {
        return asA(Label.class);
    }

    default boolean isInstruction() {
        return isA(Address.class) || isA(Compute.class);
    }
}
