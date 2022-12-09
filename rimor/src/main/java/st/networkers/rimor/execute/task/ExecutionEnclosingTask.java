package st.networkers.rimor.execute.task;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.command.MappedCommand;
import st.networkers.rimor.internal.inject.Annotated;
import st.networkers.rimor.internal.instruction.Instruction;

public interface ExecutionEnclosingTask extends Annotated {

    void run(MappedCommand command, Injector injector, ExecutionContext context);

    void run(Instruction instruction, Injector injector, ExecutionContext context);

}
