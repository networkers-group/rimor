package st.networkers.rimor.execute.task;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.command.MappedCommand;
import st.networkers.rimor.internal.inject.AbstractAnnotated;
import st.networkers.rimor.internal.instruction.Instruction;

public abstract class AbstractExecutionEnclosingTask
        extends AbstractAnnotated<AbstractExecutionEnclosingTask>
        implements ExecutionEnclosingTask
{

    protected void run(Injector injector, ExecutionContext context) {
    }

    @Override
    public void run(MappedCommand command, Injector injector, ExecutionContext context) {
        this.run(injector, context);
    }

    @Override
    public void run(Instruction instruction, Injector injector, ExecutionContext context) {
        this.run(injector, context);
    }
}
