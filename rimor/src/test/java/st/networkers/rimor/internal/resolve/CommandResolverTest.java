package st.networkers.rimor.internal.resolve;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.TestCommand;
import st.networkers.rimor.internal.command.Command;
import st.networkers.rimor.internal.instruction.Instruction;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommandResolverTest {

    private static TestCommand testCommand;
    private static Command command;

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
    void whenGettingMainInstruction_thenIsNotNull() {
        assertNotNull(command.getMainInstruction());
    }

    @Test
    void whenGettingFooInstruction_thenIsNotNull() {
        assertNotNull(command.getInstruction("foo"));
    }

    @Test
    void whenGettingFooInstruction_thenAliasesAreFooAndFooAlias() {
        Instruction fooInstruction = command.getInstruction("foo");

        assertThat(fooInstruction.getAliases()).hasSameElementsAs(Arrays.asList("foo", "fooalias"));
    }

    @Test
    // the baz instruction just has "bazalias" as an alias because it is annotated with @IgnoreMethodName, which is baz
    void whenGettingBazInstruction_thenAliasesAreOnlyBazAlias() {
        Instruction bazInstruction = command.getInstruction("bazalias");

        assertEquals(Collections.singletonList("bazalias"), bazInstruction.getAliases());
    }

    @Test
    void whenGettingSubcommands_thenSizeIs1() {
        assertEquals(1, command.getSubcommands().size());
    }

    @Test
    void whenGettingBarSubcommandAndCheckingParent_thenParentEqualsResolvedCommand() {
        assertThat(command.getSubcommand("bar").getParent()).contains(command);
    }

    // the Bar subcommand has no @Aliases annotation, so the alias is the class name, Bar.
    @Test
    void whenGettingBarSubcommand_thenAliasesAreOnlyBar() {
        assertThat(command.getSubcommand("bar").getAliases()).contains("bar");
    }
}
