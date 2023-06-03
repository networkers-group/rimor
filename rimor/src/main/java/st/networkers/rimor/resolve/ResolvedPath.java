package st.networkers.rimor.resolve;

import st.networkers.rimor.instruction.Instruction;

import java.util.List;
import java.util.Objects;

public final class ResolvedPath {

    private final Instruction instruction;
    private final List<String> leftoverPath;

    public ResolvedPath(Instruction instruction, List<String> leftoverPath) {
        this.instruction = instruction;
        this.leftoverPath = leftoverPath;
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
        return Objects.equals(instruction, that.instruction) && Objects.equals(leftoverPath, that.leftoverPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instruction, leftoverPath);
    }

    @Override
    public String toString() {
        return "ResolvedPath{" +
               "instruction=" + instruction +
               ", leftoverPath=" + leftoverPath +
               '}';
    }
}
