package st.networkers.rimor.internal.resolve;

import lombok.Getter;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class ResolvedInstructions {
    private final Collection<ResolvedInstruction> instructions = new ArrayList<>();
    private final Collection<ResolvedInstruction> mainInstructions = new ArrayList<>();

    public void addMainInstruction(ResolvedInstruction instruction) {
        this.mainInstructions.add(instruction);
    }

    public void addInstruction(ResolvedInstruction instruction) {
        this.instructions.add(instruction);
    }
}
