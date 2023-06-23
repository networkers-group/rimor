package st.networkers.rimor.aop;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.instruction.Instruction;

public interface CommandExecutor {

    /**
     * Executes the given {@link Instruction} with the given {@link ExecutionContext}.
     *
     * @param instruction instruction to execute.
     * @param context     context of the command execution.
     * @return the result of executing the instruction method.
     */
    Object execute(Instruction instruction, ExecutionContext context);
}
