package st.networkers.rimor;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;

public interface CommandExecutor {

    /**
     * Executes the given {@link ResolvedInstruction} with the given {@link ExecutionContext}.
     *
     * @param instruction instruction to execute.
     * @param context context of the command execution.
     * @return the result of executing the instruction method.
     */
    Object execute(ResolvedInstruction instruction, ExecutionContext context);
}
