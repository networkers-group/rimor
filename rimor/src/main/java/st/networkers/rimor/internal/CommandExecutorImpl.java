package st.networkers.rimor.internal;

import st.networkers.rimor.CommandExecutor;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.instruction.Instruction;

public class CommandExecutorImpl implements CommandExecutor {

    private final Injector injector;

    public CommandExecutorImpl(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Object execute(Instruction instruction, ExecutionContext context) {
        return injector.invokeMethod(instruction.getMethod(), instruction.getCommandInstance(), context);
    }
}
