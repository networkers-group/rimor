package st.networkers.rimor.execute;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.task.PreExecutionTask;
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

    /**
     * Executes the given {@link Instruction} with the given {@link ExecutionContext}.
     *
     * @param instruction           instruction to execute.
     * @param context               context of the command execution.
     * @param skipPreExecutionTasks whether to skip the {@link PreExecutionTask} of the instruction and its parent command
     *                              and recursive parents.
     * @return the result of executing the instruction method.
     */
    Object execute(Instruction instruction, ExecutionContext context, boolean skipPreExecutionTasks);
}
