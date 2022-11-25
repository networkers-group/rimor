package st.networkers.rimor.internal.resolve;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.TestCommand;
import st.networkers.rimor.internal.command.Command;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class CommandResolverTest {

    static TestCommand testCommand = new TestCommand();
    static Command command;

    @BeforeAll
    static void setUp() {
        command = CommandResolver.resolve(testCommand);
    }

    @Test
    void whenCheckingParent_thenParentIsNull() {
        assertThat(command.getParent()).isNotPresent();
    }

    @Test
    void whenCheckingCommandInstance_thenCommandInstanceEqualsSetupInstance() {
        assertEquals(testCommand, command.getDefinition());
    }

    @Test
    void whenCheckingAliases_thenAliasesEqualsToAnnotated() {
        assertThat(command.getAliases()).hasSameElementsAs(Arrays.asList("test", "testcommand"));
    }

    @Test
    void whenGettingMainInstruction_thenIsNotNull() {
        assertThat(command.getMainInstruction()).isPresent();
    }

    @Test
    void whenGettingFooInstruction_thenAliasesAreFooAndFooAlias() {
        assertThat(command.getInstruction("foo")).isPresent();
        assertThat(command.getInstruction("foo").get().getAliases()).hasSameElementsAs(Arrays.asList("foo", "fooalias"));
    }

    @Test
    // the baz instruction just has "bazalias" as an alias because ignoreMethodName is true, which is baz
    void whenGettingBazInstruction_thenAliasesAreOnlyBazAlias() {
        assertThat(command.getInstruction("baz")).isNotPresent();
        assertThat(command.getInstruction("bazalias")).isPresent();
        assertEquals(Collections.singletonList("bazalias"), command.getInstruction("bazalias").get().getAliases());
    }

    @Test
    void whenGettingSubcommands_thenThereIsOnlyBar() {
        assertEquals(1, command.getSubcommands().size());
        assertThat(command.getSubcommand("bar")).isPresent();
    }

    @Test
    void whenGettingBarSubcommandAndCheckingParent_thenParentEqualsResolvedCommand() {
        assertThat(command.getSubcommand("bar").get().getParent()).contains(command);
    }

    // the Bar subcommand has no aliases, so the alias is the class name, Bar.
    @Test
    void whenGettingBarSubcommand_thenAliasesAreOnlyBar() {
        assertThat(command.getSubcommand("bar").get().getAliases()).contains("bar");
    }
}
