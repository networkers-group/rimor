package st.networkers.rimor.command;

import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.Executable;
import st.networkers.rimor.inject.Annotated;
import st.networkers.rimor.inject.AnnotatedProperties;
import st.networkers.rimor.instruction.Instruction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolved command to use internally. To create commands, check {@link RimorCommand}.
 *
 * @see RimorCommand
 * @see AbstractRimorCommand
 */
public class MappedCommand implements Annotated, Executable {

    @Nullable private final MappedCommand parent;
    private final RimorCommand command;
    private final AnnotatedProperties annotatedProperties;

    private final String name;
    private final List<String> aliases;

    private Instruction mainInstruction;
    private final Map<String, Instruction> instructions = new HashMap<>();
    private final Map<String, MappedCommand> subcommands = new HashMap<>();

    public MappedCommand(@Nullable MappedCommand parent,
                         RimorCommand command,
                         AnnotatedProperties annotatedProperties,
                         String name,
                         List<String> aliases) {
        this.parent = parent;
        this.command = command;
        this.annotatedProperties = annotatedProperties;
        this.name = name.toLowerCase();
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public Optional<MappedCommand> getParent() {
        return Optional.ofNullable(parent);
    }

    public RimorCommand getCommand() {
        return command;
    }

    @Override
    public AnnotatedProperties getAnnotatedProperties() {
        return annotatedProperties;
    }

    public void setMainInstruction(Instruction mainInstruction) {
        this.mainInstruction = mainInstruction;
    }

    public void registerInstruction(Instruction instruction) {
        instruction.getAliases().forEach(alias -> this.instructions.put(alias, instruction));
    }

    public void registerSubcommand(MappedCommand subcommand) {
        this.subcommands.put(subcommand.getName(), subcommand);
        subcommand.getAliases().forEach(alias -> this.subcommands.put(alias, subcommand));
    }

    public String getName() {
        return this.name;
    }

    public Collection<String> getAliases() {
        return Collections.unmodifiableCollection(this.aliases);
    }

    public Optional<Instruction> getMainInstruction() {
        return Optional.ofNullable(this.mainInstruction);
    }

    public Optional<Instruction> getInstruction(String alias) {
        return Optional.ofNullable(this.instructions.get(alias.toLowerCase()));
    }

    public Map<String, Instruction> getInstructions() {
        return Collections.unmodifiableMap(this.instructions);
    }

    public Optional<MappedCommand> getSubcommand(String alias) {
        return Optional.ofNullable(this.subcommands.get(alias.toLowerCase()));
    }

    public Map<String, MappedCommand> getSubcommands() {
        return Collections.unmodifiableMap(this.subcommands);
    }

    public Set<String> getAllInstructionAliases() {
        return this.instructions.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MappedCommand)) return false;
        MappedCommand that = (MappedCommand) o;
        return Objects.equals(parent, that.parent) && Objects.equals(command, that.command) && Objects.equals(annotatedProperties, that.annotatedProperties) && Objects.equals(name, that.name) && Objects.equals(aliases, that.aliases) && Objects.equals(mainInstruction, that.mainInstruction) && Objects.equals(instructions, that.instructions) && Objects.equals(subcommands, that.subcommands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, command, annotatedProperties, name, aliases, mainInstruction, instructions, subcommands);
    }
}
