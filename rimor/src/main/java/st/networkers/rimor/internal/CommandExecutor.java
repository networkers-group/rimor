package st.networkers.rimor.internal;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.instruction.CommandInstruction;
import st.networkers.rimor.internal.inject.Injector;

public class CommandExecutor {

    private final Injector injector;

    public CommandExecutor(Injector injector) {
        this.injector = injector;
    }

    public Object execute(CommandInstruction instruction, ExecutionContext context) {
        return injector.invokeMethod(instruction.getMethod(), instruction.getCommandInstance(), context);
    }
}
