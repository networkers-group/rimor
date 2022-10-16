package st.networkers.rimor.internal.resolve;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.TestCommand;
import st.networkers.rimor.internal.command.RimorCommand;
import st.networkers.rimor.internal.instruction.CommandInstruction;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class CommandResolverTest {

    private static TestCommand testCommand;
    private static RimorCommand command;

    @BeforeAll
    static void setUp() {
        testCommand = new TestCommand();
        command = CommandResolver.resolve(testCommand);
    }

    @Test
    void whenCheckingParent_thenParentIsNull() {
        assertNull(command.getParent().orElse(null));
    }

    @Test
    void whenCheckingCommandInstance_thenCommandInstanceEqualsSetupInstance() {
        assertEquals(testCommand, command.getCommandInstance());
    }

    @Test
    void whenCheckingAliases_thenAliasesEqualsToAnnotated() {
        assertThat(command.getAliases()).hasSameElementsAs(Arrays.asList("test", "testcommand"));
    }

    @Test
    void whenGettingMainInstructions_thenSizeIs2() {
        assertEquals(2, command.getMainInstructions().size());
    }

    @Test
    void whenGettingFooInstructions_thenSizeIs1() {
        assertEquals(1, command.getInstructions("foo").size());
    }

    @Test
    void whenGettingFooInstruction_thenAliasesAreFooAndFooAlias() {
        CommandInstruction fooInstruction = command.getInstructions("foo").stream().findAny().get();

        assertThat(fooInstruction.getAliases()).hasSameElementsAs(Arrays.asList("foo", "fooalias"));
    }

    @Test
    // the baz instruction just has "bazalias" as an alias because it is annotated with @IgnoreMethodName, which is baz
    void whenGettingBazInstruction_thenAliasesAreOnlyBazAlias() {
        CommandInstruction bazInstruction = command.getInstructions("bazalias").stream().findAny().get();

        assertEquals(Collections.singletonList("bazalias"), bazInstruction.getAliases());
    }

    @Test
    void whenGettingSubcommands_thenSizeIs1() {
        assertEquals(1, command.getSubcommands().size());
    }

    // the Bar subcommand has no @Aliases annotation, so the alias is the class name, Bar.
    @Test
    void whenGettingBarSubcommand_thenAliasesAreOnlyBar() {
        assertEquals(Collections.singletonList("bar"), command.getSubcommand("bar").getAliases());
    }
}
