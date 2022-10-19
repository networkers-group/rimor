package st.networkers.rimor;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.instruction.CommandInstruction;

public interface CommandExecutor {

    /**
     * Executes the given {@link CommandInstruction} with the given {@link ExecutionContext}.
     *
     * @param instruction instruction to execute.
     * @param context context of the command execution.
     * @return the result of executing the instruction method.
     */
    Object execute(CommandInstruction instruction, ExecutionContext context);
}
