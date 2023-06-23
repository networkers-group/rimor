package st.networkers.rimor.internal.execute;

import st.networkers.rimor.aop.AdviceRegistry;
import st.networkers.rimor.aop.CommandExecutor;
import st.networkers.rimor.aop.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.instruction.Instruction;

public class CommandExecutorImpl implements CommandExecutor {

    private final RimorInjector injector;
    private final ExceptionHandlerRegistry exceptionHandlerRegistry;
    private final AdviceRegistry adviceRegistry;

    public CommandExecutorImpl(RimorInjector injector, ExceptionHandlerRegistry exceptionHandlerRegistry,
                               AdviceRegistry adviceRegistry) {
        this.injector = injector;
        this.exceptionHandlerRegistry = exceptionHandlerRegistry;
        this.adviceRegistry = adviceRegistry;
    }

    @Override
    public Object execute(Instruction instruction, ExecutionContext context) {
        try {
            this.runPreExecutionTasks(instruction, context);
            Object result = injector.invokeMethod(instruction.getMethod(), instruction.getCommandInstance(), context);
            this.runPostExecutionTasks(instruction, context);

            return result;
        } catch (Throwable throwable) {
            this.handleThrowable(throwable, instruction, context);
            return null;
        }
    }

    private void runPreExecutionTasks(Instruction instruction, ExecutionContext context) {
        instruction.getExecutionTaskRegistry().runPreExecutionTasks(instruction, context);
        adviceRegistry.runPreExecutionTasks(instruction, context);
    }

    private void runPostExecutionTasks(Instruction instruction, ExecutionContext context) {
        instruction.getExecutionTaskRegistry().runPostExecutionTasks(instruction, context);
        adviceRegistry.runPostExecutionTasks(instruction, context);
    }

    private void handleThrowable(Throwable throwable, Instruction instruction, ExecutionContext context) {
        try {
            instruction.getExceptionHandlerRegistry().handleException(throwable, context);
        } catch (Throwable rethrown) {
            exceptionHandlerRegistry.handleException(rethrown, context);
        }
    }
}
