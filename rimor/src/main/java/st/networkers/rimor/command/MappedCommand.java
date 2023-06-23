package st.networkers.rimor.command;

import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.annotated.AnnotatedProperties;
import st.networkers.rimor.aop.AdviceRegistry;
import st.networkers.rimor.aop.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.Executable;
import st.networkers.rimor.execute.ExecutableScope;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.provide.ProviderRegistry;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolved command to use internally. To create commands, check {@link RimorCommand}.
 *
 * @see RimorCommand
 * @see AbstractRimorCommand
 */
public class MappedCommand implements Annotated, Executable {

    private final Object commandInstance;
    private final AnnotatedProperties annotatedProperties;
    private final ExecutableScope executableScope;

    private final List<String> identifiers;

    private final Instruction mainInstruction;
    private final Map<String, Instruction> instructions;
    private final Map<String, MappedCommand> subcommands;

    public MappedCommand(Object commandInstance,
                         AnnotatedProperties annotatedProperties,
                         ExecutableScope executableScope,
                         List<String> identifiers,
                         Instruction mainInstruction,
                         Map<String, Instruction> instructions,
                         Map<String, MappedCommand> subcommands) {
        this.commandInstance = commandInstance;
        this.annotatedProperties = annotatedProperties;
        this.executableScope = executableScope;
        this.identifiers = identifiers.stream().map(String::toLowerCase).collect(Collectors.toList());
        this.mainInstruction = mainInstruction;
        this.instructions = instructions;
        this.subcommands = subcommands;
    }

    public Object getCommandInstance() {
        return commandInstance;
    }

    @Override
    public AnnotatedProperties getAnnotatedProperties() {
        return annotatedProperties;
    }

    @Override
    public ExceptionHandlerRegistry getExceptionHandlerRegistry() {
        return executableScope.getExceptionHandlerRegistry();
    }

    @Override
    public AdviceRegistry getExecutionTaskRegistry() {
        return executableScope.getExecutionTaskRegistry();
    }

    @Override
    public ProviderRegistry getProviderRegistry() {
        return executableScope.getProviderRegistry();
    }

    public Collection<String> getIdentifiers() {
        return Collections.unmodifiableCollection(this.identifiers);
    }

    public Optional<Instruction> getMainInstruction() {
        return Optional.ofNullable(this.mainInstruction);
    }

    public Optional<Instruction> getInstruction(String identifier) {
        return Optional.ofNullable(this.instructions.get(identifier.toLowerCase()));
    }

    public Map<String, Instruction> getInstructions() {
        return Collections.unmodifiableMap(this.instructions);
    }

    public Optional<MappedCommand> getSubcommand(String identifier) {
        return Optional.ofNullable(this.subcommands.get(identifier.toLowerCase()));
    }

    public Map<String, MappedCommand> getSubcommands() {
        return Collections.unmodifiableMap(this.subcommands);
    }

    public Set<String> getAllInstructionIdentifiers() {
        return this.instructions.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MappedCommand)) return false;
        MappedCommand that = (MappedCommand) o;
        return Objects.equals(commandInstance, that.commandInstance) && Objects.equals(annotatedProperties, that.annotatedProperties) && Objects.equals(executableScope, that.executableScope) && Objects.equals(identifiers, that.identifiers) && Objects.equals(mainInstruction, that.mainInstruction) && Objects.equals(instructions, that.instructions) && Objects.equals(subcommands, that.subcommands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandInstance, annotatedProperties, executableScope, identifiers, mainInstruction, instructions, subcommands);
    }
}
