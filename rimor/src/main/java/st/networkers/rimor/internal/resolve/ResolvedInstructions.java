package st.networkers.rimor.internal.resolve;

import lombok.Getter;
import lombok.Setter;
import st.networkers.rimor.internal.instruction.Instruction;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class ResolvedInstructions {
    @Setter private Instruction mainInstruction;
    private final Collection<Instruction> instructions = new ArrayList<>();

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
    }
}
