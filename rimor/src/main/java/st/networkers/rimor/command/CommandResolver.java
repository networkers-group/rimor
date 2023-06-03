package st.networkers.rimor.command;

import st.networkers.rimor.executable.ExecutableProperties;
import st.networkers.rimor.inject.AnnotatedProperties;
import st.networkers.rimor.instruction.InstructionResolver;
import st.networkers.rimor.instruction.ResolvedInstructions;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CommandResolver {

    private final InstructionResolver instructionResolver;

    public CommandResolver() {
        this(new InstructionResolver());
    }

    public CommandResolver(InstructionResolver instructionResolver) {
        this.instructionResolver = instructionResolver;
    }

    public MappedCommand resolve(Object commandInstance) {
        return resolve(new ExecutableProperties(), commandInstance);
    }

    public MappedCommand resolve(ExecutableProperties parentProperties, Object commandInstance) {
        if (!commandInstance.getClass().isAnnotationPresent(CommandMapping.class))
            throw new IllegalArgumentException("there is no CommandMapping annotation in " + commandInstance.getClass().getName());

        AnnotatedProperties annotatedProperties = AnnotatedProperties.build(commandInstance.getClass());
        ExecutableProperties executableProperties = parentProperties.clone();
        List<String> identifiers = this.resolveIdentifiers(commandInstance);

        // TODO exception handlers, execution tasks and providers

        ResolvedInstructions instructions = instructionResolver.resolveInstructions(commandInstance, executableProperties);
        Collection<MappedCommand> subcommands = this.resolveSubcommands(commandInstance, executableProperties);

        return new MappedCommandBuilder()
                .setCommandInstance(commandInstance)
                .setAnnotatedProperties(annotatedProperties)
                .setExecutableProperties(executableProperties)
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

    private Collection<MappedCommand> resolveSubcommands(Object commandInstance, ExecutableProperties properties) {
        Collection<MappedCommand> subcommands = this.resolveDeclaredSubcommands(commandInstance, properties);

        if (commandInstance instanceof RimorCommand)
            subcommands.addAll(this.resolveRegisteredSubcommands((RimorCommand) commandInstance, properties));

        return subcommands;
    }

    private Collection<MappedCommand> resolveDeclaredSubcommands(Object commandInstance, ExecutableProperties properties) {
        return Arrays.stream(commandInstance.getClass().getClasses())
                .filter(subcommandClass -> subcommandClass.isAnnotationPresent(CommandMapping.class))
                .filter(subcommandClass -> !(commandInstance instanceof RimorCommand)
                                           || !((RimorCommand) commandInstance).getSubcommands().contains(subcommandClass))
                .map(subcommandClass -> ReflectionUtils.instantiateInnerClass(commandInstance, subcommandClass))
                .map(subcommandInstance -> this.resolve(properties, subcommandInstance))
                .collect(Collectors.toList());
    }

    private Collection<MappedCommand> resolveRegisteredSubcommands(RimorCommand commandInstance, ExecutableProperties properties) {
        return commandInstance.getSubcommands().stream()
                .map(subcommandInstance -> this.resolve(properties, subcommandInstance))
                .collect(Collectors.toList());
    }
}
