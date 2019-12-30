package n.to.t.assembler.instruction;

import static java.lang.Integer.toBinaryString;

public interface Instruction {

    static A a() {
        return new A();
    }

    static C c() {
        return new C();
    }

    int bits();

    default String toBitString() {
        return String.format("%16s", toBinaryString(bits())).replace(' ', '0');
    }
}
