package st.networkers.rimor.execute;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.instruction.Instruction;

public class DefaultCommandExecutor implements CommandExecutor {

    private final RimorInjector injector;

    public DefaultCommandExecutor(RimorInjector injector) {
        this.injector = injector;
    }

    @Override
    public Object execute(Instruction instruction, ExecutionContext context) {
        return injector.invokeMethod(instruction.getMethod(), instruction.getCommandInstance(), context);
    }
}
