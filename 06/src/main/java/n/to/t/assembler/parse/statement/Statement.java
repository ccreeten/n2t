package n.to.t.assembler.parse.statement;

public interface Statement {

    default boolean is(final Class<? extends Statement> type) {
        return type.isInstance(this);
    }

    default <T extends Statement> T as(final Class<T> type) {
        return type.cast(this);
    }

    default boolean isLabel() {
        return is(Label.class);
    }

    default Label asLabel() {
        return as(Label.class);
    }

    default boolean isInstruction() {
        return is(Address.class) || is(Compute.class);
    }
}
