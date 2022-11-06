package st.networkers.rimor.internal.resolve;

import lombok.Getter;
import lombok.Setter;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class ResolvedInstructions {
    @Setter private ResolvedInstruction mainInstruction;
    private final Collection<ResolvedInstruction> instructions = new ArrayList<>();

    public void addInstruction(ResolvedInstruction instruction) {
        this.instructions.add(instruction);
    }
}
