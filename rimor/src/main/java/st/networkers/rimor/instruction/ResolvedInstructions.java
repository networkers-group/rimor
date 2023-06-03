package st.networkers.rimor.instruction;

import java.util.ArrayList;
import java.util.Collection;

public class ResolvedInstructions {

    private Instruction mainInstruction = null;
    private final Collection<Instruction> instructions = new ArrayList<>();

    public Instruction getMainInstruction() {
        return mainInstruction;
    }

    public void setMainInstruction(Instruction instruction) {
        if (this.mainInstruction != null)
            throw new IllegalArgumentException("trying to map multiple main instructions for " + instruction.getCommandInstance().getClass().getName());
        this.mainInstruction = instruction;
    }

    public Collection<Instruction> getInstructions() {
        return instructions;
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
    }
}
