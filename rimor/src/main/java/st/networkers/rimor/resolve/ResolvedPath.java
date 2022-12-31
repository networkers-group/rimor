package st.networkers.rimor.resolve;

import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.instruction.Instruction;

import java.util.List;
import java.util.Objects;

public final class ResolvedPath {

    private final MappedCommand mainCommand;
    private final Instruction instruction;
    private final List<String> leftoverPath;

    public ResolvedPath(MappedCommand mainCommand, Instruction instruction, List<String> leftoverPath) {
        this.mainCommand = mainCommand;
        this.instruction = instruction;
        this.leftoverPath = leftoverPath;
    }

    public MappedCommand getMainCommand() {
        return this.mainCommand;
    }

    public Instruction getInstruction() {
        return this.instruction;
    }

    public List<String> getLeftoverPath() {
        return this.leftoverPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResolvedPath)) return false;
        ResolvedPath that = (ResolvedPath) o;
        return Objects.equals(mainCommand, that.mainCommand) && Objects.equals(instruction, that.instruction) && Objects.equals(leftoverPath, that.leftoverPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainCommand, instruction, leftoverPath);
    }

    @Override
    public String toString() {
        return "ResolvedPath{" +
               "mainCommand=" + mainCommand +
               ", instruction=" + instruction +
               ", leftoverPath=" + leftoverPath +
               '}';
    }
}
