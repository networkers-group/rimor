package st.networkers.rimor.resolve;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.TestCommand;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.task.ExecutionEnclosingTaskRegistry;
import st.networkers.rimor.execute.task.ExecutionEnclosingTaskRegistryImpl;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.command.MappedCommand;
import st.networkers.rimor.internal.inject.InjectorImpl;
import st.networkers.rimor.internal.instruction.Instruction;
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

    static ExecutionEnclosingTaskRegistry executionEnclosingTaskRegistry = new ExecutionEnclosingTaskRegistryImpl();
    static Injector injector = new InjectorImpl(new ProviderRegistryImpl());

    static PathResolver resolver = new PathResolverImpl(executionEnclosingTaskRegistry, injector);

    static TestCommand testCommand = new TestCommand();
    static MappedCommand command;

    @BeforeAll
    static void setUp() {
        command = CommandResolver.resolve(testCommand);
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
        Results results = resolver.resolvePath(command, Arrays.asList("bar", "set", "true"), ExecutionContext.build());

        Instruction setInstruction = command.getSubcommand("bar").get().getInstruction("set").get();
        assertEquals(setInstruction, results.getInstruction());

        assertThat(results.getLeftoverPath()).containsExactly("true");
    }
}
