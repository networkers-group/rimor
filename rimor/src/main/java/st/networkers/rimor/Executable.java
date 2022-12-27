package st.networkers.rimor;

import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.inject.Annotated;

/**
 * Represents everything executed in a command execution.
 *
 * @see MappedCommand
 * @see st.networkers.rimor.instruction.Instruction
 * @see st.networkers.rimor.execute.task.ExecutionTask
 */
public interface Executable extends Annotated {
}
