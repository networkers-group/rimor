package st.networkers.rimor.resolve;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.BarAnnotation;
import st.networkers.rimor.PresentBarAnnotationExecutionTask;
import st.networkers.rimor.TestCommand;
import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.internal.execute.exception.ExceptionHandlerRegistryImpl;
import st.networkers.rimor.internal.execute.task.ExecutionTaskRegistryImpl;
import st.networkers.rimor.internal.inject.InjectorImpl;
import st.networkers.rimor.internal.provide.ProviderRegistryImpl;
import st.networkers.rimor.internal.resolve.CommandResolver;
import st.networkers.rimor.internal.resolve.PathResolverImpl;
import st.networkers.rimor.resolve.PathResolver.Results;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class PathResolverTest {

    static Injector injector = new InjectorImpl(new ProviderRegistryImpl());

    static TestCommand testCommand = new TestCommand();
    static MappedCommand command;

    static PathResolver resolver;

    @BeforeAll
    static void setUp() {
        command = CommandResolver.resolve(testCommand);

        ExecutionTaskRegistry executionTaskRegistry = new ExecutionTaskRegistryImpl();
        executionTaskRegistry.registerPreExecutionTask(new PresentBarAnnotationExecutionTask());

        resolver = new PathResolverImpl(new ExceptionHandlerRegistryImpl(), executionTaskRegistry);
    }

    @Test
    void givenPathWithNoMapping_whenResolving_thenResultIsMainInstruction() {
        Results results = resolver.resolvePath(command, Arrays.asList("test", "param0", "param1"), ExecutionContext.build());

        assertEquals(command.getMainInstruction().get(), results.getInstruction());
        assertThat(results.getLeftoverPath()).containsExactly("test", "param0", "param1");
    }

    @Test
    void givenFooPath_whenResolving_thenResultIsFooInstruction() {
        Results results = resolver.resolvePath(command, Arrays.asList("foo", "param0", "param1"), ExecutionContext.build());

        assertEquals(command.getInstruction("foo").get(), results.getInstruction());
        assertThat(results.getLeftoverPath()).containsExactly("param0", "param1");
    }

    @Test
    void givenBarPath_whenResolving_thenThrowsInstructionNotFoundException() {
        // throws exception because bar subcommand has no main instruction
        InstructionNotFoundException exception = assertThrows(
                InstructionNotFoundException.class,
                () -> resolver.resolvePath(command, Arrays.asList("bar", "param0", "param1"), ExecutionContext.build())
        );

        assertEquals(command, exception.getUberCommand());
        assertEquals(command.getSubcommand("bar").get(), exception.getSubcommand());
        assertThat(exception.getRemainingPath()).containsExactly("param0", "param1");
    }

    @Test
    void givenBarSetPath_whenResolving_thenResultIsSetInstructionInBarSubcommand() {
        Instruction setInstruction = command.getSubcommand("bar").get().getInstruction("set").get();
        Results results = resolver.resolvePath(command, Arrays.asList("bar", "set", "true"), ExecutionContext.build());

        assertEquals(setInstruction, results.getInstruction());
        assertThat(results.getLeftoverPath()).containsExactly("true");
    }

    @Test
    void givenBarSetPathWithBarAnnotationAnnotatedString_whenResolving_thenThrowsIllegalStateException() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(String.class, "").annotatedWith(BarAnnotation.class)
        );

        assertThrows(IllegalStateException.class, () -> resolver.resolvePath(command, Arrays.asList("bar", "set", "true"), context));
    }
}
