package st.networkers.rimor.interpret.execution;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.command.Command;
import st.networkers.rimor.internal.inject.Annotated;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;

public interface ExecutionEnclosingTask extends Annotated {

    void run(Command command, Injector injector, ExecutionContext context);

    void run(ResolvedInstruction instruction, Injector injector, ExecutionContext context);

}
