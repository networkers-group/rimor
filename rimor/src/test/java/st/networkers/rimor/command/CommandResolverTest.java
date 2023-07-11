package st.networkers.rimor.command;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.instruction.InstructionMapping;
import st.networkers.rimor.instruction.MainInstructionMapping;
import st.networkers.rimor.reflect.CachedMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@MockitoSettings
class CommandResolverTest {

    @Spy CommandResolver commandResolver = new CommandResolver();

    @Command({"foo", "bar"})
    static class TwoIdentifiersCommand {
    }

    @Test
    void whenResolvingCommand_resultingCommandInstanceEqualsResolvedCommandInstance() {
        TwoIdentifiersCommand command = new TwoIdentifiersCommand();
        assertThat(commandResolver.resolve(command).getCommandInstance()).isEqualTo(command);
    }

    @Test
    void whenResolvingCommand_identifiersAreFooAndBarInOrder() {
        MappedCommand command = commandResolver.resolve(new TwoIdentifiersCommand());
        assertThat(command.getIdentifiers()).containsExactly("foo", "bar");
    }

    @Command("")
    static class NoIdentifierCommand {}

    @Test
    void whenResolvingNoIdentifierCommand_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> commandResolver.resolve(new NoIdentifierCommand())).isInstanceOf(IllegalArgumentException.class);
    }

    @Command("foo")
    static class CommandWithDefaultInstruction {
        @MainInstructionMapping
        public void defaultInstruction() {
        }
    }

    @Test
    void whenResolvingCommandWithDefaultInstruction_mainInstructionIsResolved() throws NoSuchMethodException {
        MappedCommand command = commandResolver.resolve(new CommandWithDefaultInstruction());
        assertThat(command.getMainInstruction())
                .map(Instruction::getMethod)
                .map(CachedMethod::getMethod)
                .contains(CommandWithDefaultInstruction.class.getMethod("defaultInstruction"));
    }

    @Command("foo")
    static class CommandWithInstruction {
        @InstructionMapping("bar")
        public void barInstruction() {
        }
    }

    @Test
    void whenResolvingCommandWithInstruction_bazInstructionIsResolved() throws NoSuchMethodException {
        MappedCommand command = commandResolver.resolve(new CommandWithInstruction());
        assertThat(command.getInstruction("bar"))
                .map(Instruction::getMethod)
                .map(CachedMethod::getMethod)
                .contains(CommandWithInstruction.class.getMethod("barInstruction"));
    }

    @Command("foo")
    static class CommandWithDeclaredStaticSubcommand {
        @Command("bar")
        public static class BarSubcommand {
            @MainInstructionMapping
            public void defaultInstruction() {
            }
        }
    }

    @Test
    void whenResolvingCommandWithDeclaredStaticSubcommand_barSubcommandIsResolvedAndRegistered() {
        MappedCommand command = commandResolver.resolve(new CommandWithDeclaredStaticSubcommand());

        ArgumentCaptor<CommandWithDeclaredStaticSubcommand.BarSubcommand> barSubcommandInstanceCaptor = ArgumentCaptor.forClass(CommandWithDeclaredStaticSubcommand.BarSubcommand.class);
        verify(commandResolver).resolve(barSubcommandInstanceCaptor.capture());

        assertThat(command.getSubcommand("bar"))
                .map(MappedCommand::getCommandInstance)
                .contains(barSubcommandInstanceCaptor.getValue());
    }

    @Command("foo")
    static class CommandWithDeclaredNonStaticSubcommand {
        @Command("bar")
        public class BarSubcommand {
        }
    }

    @Test
    void whenResolvingCommandWithDeclaredNonStaticSubcommand_barSubcommandIsResolvedAndRegistered() {
        MappedCommand command = commandResolver.resolve(new CommandWithDeclaredNonStaticSubcommand());

        ArgumentCaptor<CommandWithDeclaredNonStaticSubcommand.BarSubcommand> barSubcommandInstanceCaptor = ArgumentCaptor.forClass(CommandWithDeclaredNonStaticSubcommand.BarSubcommand.class);
        verify(commandResolver).resolve(barSubcommandInstanceCaptor.capture());

        assertThat(command.getSubcommand("bar"))
                .map(MappedCommand::getCommandInstance)
                .contains(barSubcommandInstanceCaptor.getValue());
    }

    @Command("foo")
    static class CommandWithRegisteredInnerSubcommand extends AbstractRimorCommand {
        public CommandWithRegisteredInnerSubcommand() {
            registerSubcommand(new BarSubcommand(0));
        }

        @Command("bar")
        static class BarSubcommand {
            private BarSubcommand(int i) {
            }
        }
    }

    @Test
    void whenResolvingCommandWithRegisteredSubcommand_barSubcommandIsResolvedAndRegistered() {
        MappedCommand command = commandResolver.resolve(new CommandWithRegisteredInnerSubcommand());

        ArgumentCaptor<CommandWithRegisteredInnerSubcommand.BarSubcommand> barSubcommandInstanceCaptor = ArgumentCaptor.forClass(CommandWithRegisteredInnerSubcommand.BarSubcommand.class);
        verify(commandResolver).resolve(barSubcommandInstanceCaptor.capture());

        assertThat(command.getSubcommand("bar"))
                .map(MappedCommand::getCommandInstance)
                .contains(barSubcommandInstanceCaptor.getValue());
    }
}
