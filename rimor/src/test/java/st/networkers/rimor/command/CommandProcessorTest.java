package st.networkers.rimor.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;
import st.networkers.rimor.bean.BeanProcessingException;
import st.networkers.rimor.bean.BeanProcessor;
import st.networkers.rimor.instruction.HandlerMethodInstruction;
import st.networkers.rimor.instruction.InstructionResolver;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings
@SuppressWarnings("InnerClassMayBeStatic")
class CommandProcessorTest {

    @Mock CommandRegistry commandRegistry;
    @Mock BeanProcessor beanProcessor;
    @Mock InstructionResolver instructionResolver;

    @InjectMocks @Spy CommandProcessor commandProcessor;

    @BeforeEach
    void setUp() {
        lenient().when(instructionResolver.resolveInstructions(any()))
                .thenReturn(new InstructionResolver.InstructionResolution());
    }

    @Command({"foo", "bar"})
    static class FooCommand {
    }

    @Test
    void whenResolvingCommand_identifiersAreResolvedFromAnnotationInOrder() {
        MappedCommand command = commandProcessor.resolve(new FooCommand());
        assertThat(command.getIdentifiers()).containsExactly("foo", "bar");
    }

    @Mock HandlerMethodInstruction mockedHandlerMethodInstruction;

    @Test
    void whenResolvingCommand_resolvedMainInstructionIsRegistered() {
        // make InstructionResolver return an InstructionResolution with a main instruction
        InstructionResolver.InstructionResolution instructionResolution = new InstructionResolver.InstructionResolution();
        instructionResolution.setMainInstruction(mockedHandlerMethodInstruction);
        when(instructionResolver.resolveInstructions(any()))
                .thenReturn(instructionResolution);

        MappedCommand command = commandProcessor.resolve(new FooCommand());

        // the main instruction is registered
        assertThat(command.getMainInstruction()).contains(mockedHandlerMethodInstruction);
    }

    @Test
    void whenResolvingCommand_resolvedInstructionIsRegistered() {
        // make InstructionResolver return an InstructionResolution with an instruction "foo"
        when(mockedHandlerMethodInstruction.getIdentifiers()).thenReturn(Collections.singleton("foo"));

        InstructionResolver.InstructionResolution instructionResolution = new InstructionResolver.InstructionResolution();
        instructionResolution.addInstruction(mockedHandlerMethodInstruction);

        when(instructionResolver.resolveInstructions(any()))
                .thenReturn(instructionResolution);

        MappedCommand command = commandProcessor.resolve(new FooCommand());

        // the instruction is registered
        assertThat(command.getInstructions())
                .hasSize(1)
                .containsEntry("foo", mockedHandlerMethodInstruction);
    }


    @Command("")
    static class NoIdentifierCommand {}

    @Test
    void whenResolvingCommandWithEmptyIdentifiers_throwsBeanProcessingException() {
        assertThatThrownBy(() -> commandProcessor.resolve(new NoIdentifierCommand()))
                .isInstanceOf(BeanProcessingException.class)
                .hasMessageContaining("the specified identifiers")
                .hasMessageContaining("are empty");
    }


    @Command("foo")
    static class CommandWithDeclaredStaticSubcommand {
        @Command("bar")
        public static class BarSubcommand {
        }
    }

    @Test
    void whenResolvingCommandWithDeclaredStaticSubcommand_subcommandBeanIsInstantiatedResolvedProcessedAndRegistered() {
        MappedCommand command = commandProcessor.resolve(new CommandWithDeclaredStaticSubcommand());

        // the subcommand is resolved just like any parent command
        verify(commandProcessor).resolve(any(CommandWithDeclaredStaticSubcommand.BarSubcommand.class));

        // the new bean (instantiated by Rimor) is processed
        verify(beanProcessor).process(any(CommandWithDeclaredStaticSubcommand.BarSubcommand.class));

        // the subcommand is registered in the parent command
        assertThat(command.getSubcommand("bar")).isPresent();
    }


    @Command("foo")
    static class CommandWithDeclaredNonStaticSubcommand {
        @Command("bar")
        public class BarSubcommand {
        }
    }

    @Test
    void whenResolvingCommandWithDeclaredNonStaticSubcommand_subcommandBeanIsInstantiatedResolvedProcessedAndRegistered() {
        MappedCommand command = commandProcessor.resolve(new CommandWithDeclaredNonStaticSubcommand());

        // subcommand is resolved just like any parent command
        verify(commandProcessor).resolve(any(CommandWithDeclaredNonStaticSubcommand.BarSubcommand.class));

        // the new bean (instantiated by Rimor) is processed
        verify(beanProcessor).process(any(CommandWithDeclaredNonStaticSubcommand.BarSubcommand.class));

        // subcommand is registered in the parent command
        assertThat(command.getSubcommand("bar")).isPresent();
    }


    @Command("foo")
    static class CommandWithRegisteredInnerSubcommandDefinition extends AbstractCommandDefinition {
        public CommandWithRegisteredInnerSubcommandDefinition(
                CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand subcommandInstance
        ) {
            registerSubcommand(subcommandInstance);
        }

        @Command("bar")
        public static class BarSubcommand {
        }
    }

    @Test
    void whenResolvingCommandWithRegisteredInnerSubcommand_subcommandMustNotBeInstantiatedByProcessor() {
        CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand subcommandInstance = new CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand();
        MappedCommand command = commandProcessor.resolve(new CommandWithRegisteredInnerSubcommandDefinition(subcommandInstance));

        // subcommand is only resolved once
        verify(commandProcessor, times(1))
                .resolve(any(CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand.class));

        // and the resolved instance is the given one, thus the subcommand is never instantiated by processor
        verify(commandProcessor).resolve(subcommandInstance);
    }

    @Test
    void whenResolvingCommandWithRegisteredSubcommand_subcommandIsResolvedAndRegistered() {
        CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand subcommandInstance = new CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand();
        MappedCommand command = commandProcessor.resolve(new CommandWithRegisteredInnerSubcommandDefinition(subcommandInstance));

        // subcommand is resolved
        verify(commandProcessor).resolve(any(CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand.class));

        // subcommand bean is processed
        verify(beanProcessor).process(any(CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand.class));

        // subcommand is registered
        assertThat(command.getSubcommand("bar")).isPresent();
    }
}
