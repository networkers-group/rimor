package st.networkers.rimor.internal.resolve;

import lombok.Getter;
import st.networkers.rimor.internal.instruction.CommandInstruction;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ResolvedInstructions {
    private final List<CommandInstruction> instructions = new ArrayList<>();
    private final List<CommandInstruction> mainInstructions = new ArrayList<>();

    public void addMainInstruction(CommandInstruction instruction) {
        this.mainInstructions.add(instruction);
    }

    public void addInstruction(CommandInstruction instruction) {
        this.instructions.add(instruction);
    }
}
