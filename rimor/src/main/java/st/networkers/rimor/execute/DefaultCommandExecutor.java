package st.networkers.rimor.execute;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.instruction.Instruction;

public class DefaultCommandExecutor implements CommandExecutor {
    @Override
    public Object execute(Instruction instruction, ExecutionContext executionContext) {
        return instruction.run(executionContext);
    }
}
