package st.networkers.rimor.command;

import st.networkers.rimor.bean.BeanProcessor;
import st.networkers.rimor.instruction.InstructionResolver;
import st.networkers.rimor.instruction.InstructionResolver.InstructionResolution;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BeanProcessor that resolves and registers {@link Command @Command}-annotated command definition classes.
 */
public class CommandProcessor implements BeanProcessor {

    private final BeanProcessor beanProcessor;
    private final CommandRegistry commandRegistry;
    private final InstructionResolver instructionResolver;

    private final Map<Object, MappedCommand> resolvedCommands = new HashMap<>();

    public CommandProcessor(BeanProcessor beanProcessor, CommandRegistry commandRegistry, InstructionResolver instructionResolver) {
        this.beanProcessor = beanProcessor;
        this.commandRegistry = commandRegistry;
        this.instructionResolver = instructionResolver;
    }

    @Override
    public void process(Object bean) {
        if (!bean.getClass().isAnnotationPresent(Command.class) || isSubcommand(bean))
            return;

        MappedCommand command = this.resolve(bean, false);
        command.getIdentifiers().forEach(identifier -> commandRegistry.register(identifier, command));
    }

    private boolean isSubcommand(Object bean) {
        return bean.getClass().isMemberClass() && bean.getClass().getDeclaringClass().isAnnotationPresent(Command.class);
    }

    public MappedCommand resolve(Object bean, boolean processBean) {
        if (this.resolvedCommands.containsKey(bean)) {
            return this.resolvedCommands.get(bean);
        }

        List<String> identifiers = this.resolveIdentifiers(bean);
        InstructionResolution instructionResolution = instructionResolver.resolveInstructions(bean);
        Collection<MappedCommand> subcommands = this.resolveSubcommands(bean);

        MappedCommand command = MappedCommand.builder()
                .identifiers(identifiers)
                .mainInstruction(instructionResolution.getMainInstruction())
                .instructions(instructionResolution.mapInstructionsByIdentifier())
                .subcommands(subcommands)
                .create();

        this.resolvedCommands.put(bean, command);

        if (processBean) {
            beanProcessor.process(bean);
        }

        return command;
    }

    private List<String> resolveIdentifiers(Object bean) {
        Command command = bean.getClass().getAnnotation(Command.class);

        List<String> identifiers = Arrays.stream(command.value())
                .filter(identifier -> !identifier.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (identifiers.isEmpty())
            throw new IllegalArgumentException("the specified identifiers for " + bean.getClass().getSimpleName() + " are empty");

        return identifiers;
    }

    private Collection<MappedCommand> resolveSubcommands(Object bean) {
        List<MappedCommand> subcommands = new ArrayList<>();

        if (bean instanceof CommandDefinition)
            subcommands.addAll(this.resolveRegisteredSubcommands((CommandDefinition) bean));

        subcommands.addAll(this.resolveDeclaredSubcommands(bean));

        return subcommands;
    }

    private Collection<MappedCommand> resolveRegisteredSubcommands(CommandDefinition bean) {
        return bean.getSubcommands().stream()
                .map(subcommandBean -> this.resolve(subcommandBean, true))
                .collect(Collectors.toList());
    }

    // package-private for testing purposes
    Collection<MappedCommand> resolveDeclaredSubcommands(Object bean) {
        Collection<Class<?>> classesToIgnore = new ArrayList<>();

        if (bean instanceof CommandDefinition) {
            // manually registered subcommands should not be resolved here
            ((CommandDefinition) bean).getSubcommands().stream()
                    .map(Object::getClass)
                    .forEach(classesToIgnore::add);
        }

        return Arrays.stream(bean.getClass().getClasses())
                .filter(subcommandClass -> subcommandClass.isAnnotationPresent(Command.class))
                .filter(subcommandClass -> !classesToIgnore.contains(subcommandClass))
                .map(subcommandClass -> ReflectionUtils.instantiateInnerClass(bean, subcommandClass))
                .map(subcommandBean -> this.resolve(subcommandBean, true))
                .collect(Collectors.toList());
    }
}
