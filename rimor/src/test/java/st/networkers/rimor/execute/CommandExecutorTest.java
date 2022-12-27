package st.networkers.rimor.execute;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.BarAnnotation;
import st.networkers.rimor.PresentBarAnnotationExecutionTask;
import st.networkers.rimor.TestCommand;
import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.internal.execute.CommandExecutorImpl;
import st.networkers.rimor.internal.execute.exception.ExceptionHandlerRegistryImpl;
import st.networkers.rimor.internal.execute.task.ExecutionTaskRegistryImpl;
import st.networkers.rimor.internal.inject.InjectorImpl;
import st.networkers.rimor.internal.resolve.CommandResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class CommandExecutorTest {

    static TestCommand testCommand = new TestCommand();
    static MappedCommand command;

    static CommandExecutor commandExecutor;

    @BeforeAll
    static void setUp() {
        command = CommandResolver.resolve(testCommand);

        ExecutionTaskRegistry executionTaskRegistry = new ExecutionTaskRegistryImpl();
        executionTaskRegistry.registerPreExecutionTask(new PresentBarAnnotationExecutionTask());

        commandExecutor = new CommandExecutorImpl(new InjectorImpl(), new ExceptionHandlerRegistryImpl(), executionTaskRegistry);
    }

    @Test
    void givenBazInstructionWithIntegerAs1_whenExecuting_thenReturns1() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(Integer.class, 1)
        );

        assertEquals(1, commandExecutor.execute(command.getInstruction("bazAlias").get(), context));
    }

    @Test
    void givenBazInstructionWithBarAnnotationAnnotatedString_whenExecuting_thenThrowsIllegalStateException() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(Integer.class, 1),
                new ContextComponent<>(String.class, "").annotatedWith(BarAnnotation.class)
        );

        assertThrows(IllegalStateException.class, () -> commandExecutor.execute(command.getInstruction("bazAlias").get(), context));
    }

    @Test
    void givenSetInstructionFromBarSubcommandWithTrueBoolean_whenExecuting_thenReturnsTrue() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(Boolean.class, true)
        );

        Instruction bar = command.getSubcommand("bar").get().getInstruction("set").get();
        assertEquals(true, commandExecutor.execute(bar, context));
    }

    @Test
    void givenSetInstructionFromBarSubcommandWithBarAnnotationAnnotatedString_whenExecuting_thenThrowsIllegalStateException() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(Boolean.class, true),
                new ContextComponent<>(String.class, "").annotatedWith(BarAnnotation.class)
        );

        Instruction bar = command.getSubcommand("bar").get().getInstruction("set").get();
        assertThrows(IllegalStateException.class, () -> commandExecutor.execute(bar, context));
    }
}
