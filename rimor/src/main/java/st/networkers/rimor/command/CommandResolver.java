package st.networkers.rimor.command;

import st.networkers.rimor.annotated.AnnotatedProperties;
import st.networkers.rimor.instruction.InstructionResolver;
import st.networkers.rimor.instruction.ResolvedInstructions;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to resolve annotation-based command definitions.
 */
public class CommandResolver {

    private final InstructionResolver instructionResolver;

    public CommandResolver() {
        this(new InstructionResolver());
    }

    public CommandResolver(InstructionResolver instructionResolver) {
        this.instructionResolver = instructionResolver;
    }

    public MappedCommand resolve(Object commandInstance) {
        if (!commandInstance.getClass().isAnnotationPresent(CommandMapping.class))
            throw new IllegalArgumentException("there is no CommandMapping annotation in " + commandInstance.getClass().getName());

        AnnotatedProperties annotatedProperties = AnnotatedProperties.build(commandInstance.getClass());
        List<String> identifiers = this.resolveIdentifiers(commandInstance);

        ResolvedInstructions instructions = instructionResolver.resolveInstructions(commandInstance);
        Collection<MappedCommand> subcommands = this.resolveSubcommands(commandInstance);

        return new MappedCommandBuilder()
                .setCommandInstance(commandInstance)
                .setAnnotatedProperties(annotatedProperties)
                .setIdentifiers(identifiers)
                .setMainInstruction(instructions.getMainInstruction())
                .setInstructions(instructions.getInstructions())
                .setSubcommands(subcommands)
                .create();
    }

    private List<String> resolveIdentifiers(Object commandInstance) {
        CommandMapping commandMapping = commandInstance.getClass().getAnnotation(CommandMapping.class);

        List<String> identifiers = Arrays.stream(commandMapping.value())
                .filter(identifier -> !identifier.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (identifiers.isEmpty())
            throw new IllegalArgumentException("the specified identifiers for " + commandInstance.getClass().getSimpleName() + " are empty");

        return identifiers;
    }

    private Collection<MappedCommand> resolveSubcommands(Object commandInstance) {
        Collection<MappedCommand> subcommands = this.resolveDeclaredSubcommands(commandInstance);

        if (commandInstance instanceof RimorCommand)
            subcommands.addAll(this.resolveRegisteredSubcommands((RimorCommand) commandInstance));

        return subcommands;
    }

    private Collection<MappedCommand> resolveDeclaredSubcommands(Object commandInstance) {
        return Arrays.stream(commandInstance.getClass().getClasses())
                .filter(subcommandClass -> subcommandClass.isAnnotationPresent(CommandMapping.class))
                .filter(subcommandClass -> !(commandInstance instanceof RimorCommand)
                                           || !((RimorCommand) commandInstance).getSubcommands().contains(subcommandClass))
                .map(subcommandClass -> ReflectionUtils.instantiateInnerClass(commandInstance, subcommandClass))
                .map(this::resolve)
                .collect(Collectors.toList());
    }

    private Collection<MappedCommand> resolveRegisteredSubcommands(RimorCommand commandInstance) {
        return commandInstance.getSubcommands().stream()
                .map(this::resolve)
                .collect(Collectors.toList());
    }
}
