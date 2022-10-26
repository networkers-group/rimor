package st.networkers.rimor.interpret.execution;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.command.ResolvedCommand;
import st.networkers.rimor.internal.inject.AbstractAnnotated;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;

public abstract class AbstractExecutionEnclosingTask
        extends AbstractAnnotated<AbstractExecutionEnclosingTask>
        implements ExecutionEnclosingTask
{

    protected void run(Injector injector, ExecutionContext context) {
    }

    @Override
    public void run(ResolvedCommand command, Injector injector, ExecutionContext context) {
        this.run(injector, context);
    }

    @Override
    public void run(ResolvedInstruction instruction, Injector injector, ExecutionContext context) {
        this.run(injector, context);
    }
}
