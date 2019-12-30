package n.to.t.assembler.instruction;

public final class C implements Instruction {

    private static final int DEST_MASK = 0b0000_0000_0011_1000;
    private static final int COMP_MASK = 0b0001_1111_1100_0000;
    private static final int JUMP_MASK = 0b0000_0000_0000_0111;

    private int bits;

    C() {
        // no-op
        this.bits = 0b1111_1111_1100_0000;
    }

    public C withDest(final int dest) {
        bits = (bits & ~DEST_MASK) | ((dest & 0b111) << 3);
        return this;
    }

    public C withComp(final int comp) {
        bits = (bits & ~COMP_MASK) | ((comp & 0b111_1111) << 6);
        return this;
    }

    public C withJump(final int jump) {
        bits = (bits & ~JUMP_MASK) | (jump & 0b111);
        return this;
    }

    @Override
    public int bits() {
        return bits;
    }
}
