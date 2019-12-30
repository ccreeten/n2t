package n.to.t.assembler.instruction;

public final class A implements Instruction {

    private int bits;

    A() {
        // 'reset'
        this.bits = 0b0000_0000_0000_0000;
    }

    public A withAddress(final int address) {
        bits = address & 0x7FFF;
        return this;
    }

    @Override
    public int bits() {
        return bits;
    }
}
