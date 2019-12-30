package n.to.t.assembler.compile;

import n.to.t.assembler.instruction.Instruction;

import java.util.List;

public interface Executable {

    List<Instruction> instructions();
}
