package st.networkers.rimor.command;

import st.networkers.rimor.bean.BeanProcessor;
import st.networkers.rimor.instruction.InstructionResolver;
import st.networkers.rimor.instruction.InstructionResolver.InstructionResolutionResults;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BeanProcessor that resolves and registers {@link Command @Command}-annotated command definition classes.
 */
public class CommandProcessor implements BeanProcessor {

    private final CommandRegistry commandRegistry;
    private final InstructionResolver instructionResolver;

    public CommandProcessor(CommandRegistry commandRegistry, InstructionResolver instructionResolver) {
        this.commandRegistry = commandRegistry;
        this.instructionResolver = instructionResolver;
    }

    @Override
    public void process(Object bean) {
        if (!bean.getClass().isAnnotationPresent(Command.class) || isSubcommand(bean))
            return;

        MappedCommand command = this.resolve(bean);
        command.getIdentifiers().forEach(identifier -> commandRegistry.register(identifier, command));
    }

    private boolean isSubcommand(Object bean) {
        return bean.getClass().isMemberClass() && bean.getClass().getDeclaringClass().isAnnotationPresent(Command.class);
    }

    public MappedCommand resolve(Object bean) {
        InstructionResolutionResults instructionResolutionResults = instructionResolver.resolveInstructions(bean);
        Collection<MappedCommand> subcommands = this.resolveSubcommands(bean);
        List<String> identifiers = this.resolveIdentifiers(bean);

        // TODO process subcommand beans

        return MappedCommand.builder()
                .identifiers(identifiers)
                .mainInstruction(instructionResolutionResults.getMainInstruction())
                .instructions(instructionResolutionResults.mapInstructionsByIdentifier())
                .subcommands(subcommands)
                .create();
    }

    private List<String> resolveIdentifiers(Object commandInstance) {
        Command command = commandInstance.getClass().getAnnotation(Command.class);

        List<String> identifiers = Arrays.stream(command.value())
                .filter(identifier -> !identifier.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (identifiers.isEmpty())
            throw new IllegalArgumentException("the specified identifiers for " + commandInstance.getClass().getSimpleName() + " are empty");

        return identifiers;
    }

    private Collection<MappedCommand> resolveSubcommands(Object commandInstance) {
        Collection<MappedCommand> subcommands = this.resolveDeclaredSubcommands(commandInstance);

        if (commandInstance instanceof CommandDefinition)
            subcommands.addAll(this.resolveRegisteredSubcommands((CommandDefinition) commandInstance));

        return subcommands;
    }

    private Collection<MappedCommand> resolveDeclaredSubcommands(Object commandInstance) {
        return Arrays.stream(commandInstance.getClass().getClasses())
                .filter(subcommandClass -> subcommandClass.isAnnotationPresent(Command.class))
                .filter(subcommandClass -> !(commandInstance instanceof CommandDefinition)
                                           || !((CommandDefinition) commandInstance).getSubcommands().contains(subcommandClass))
                .map(subcommandClass -> ReflectionUtils.instantiateInnerClass(commandInstance, subcommandClass))
                .map(this::resolve)
                .collect(Collectors.toList());
    }

    private Collection<MappedCommand> resolveRegisteredSubcommands(CommandDefinition commandInstance) {
        return commandInstance.getSubcommands().stream()
                .map(this::resolve)
                .collect(Collectors.toList());
    }
}
