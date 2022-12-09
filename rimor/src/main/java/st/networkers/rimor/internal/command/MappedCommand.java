package st.networkers.rimor.internal.command;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.Executable;
import st.networkers.rimor.command.AbstractRimorCommand;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.internal.inject.AbstractAnnotated;
import st.networkers.rimor.internal.instruction.Instruction;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolved command to use internally. To create commands, check {@link RimorCommand}.
 *
 * @see RimorCommand
 * @see AbstractRimorCommand
 */
public class MappedCommand extends AbstractAnnotated<MappedCommand> implements Executable {

    @Nullable private final MappedCommand parent;
    @Getter private final RimorCommand command;
    private final Collection<String> aliases;

    @Setter private Instruction mainInstruction;
    private final Map<String, Instruction> instructions = new HashMap<>();
    private final Map<String, MappedCommand> subcommands = new HashMap<>();

    public MappedCommand(@Nullable MappedCommand parent, RimorCommand command, Collection<String> aliases,
                         Map<Class<? extends Annotation>, Annotation> annotations) {
        super(annotations);
        this.parent = parent;
        this.command = command;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public Optional<MappedCommand> getParent() {
        return Optional.ofNullable(parent);
    }

    public void registerInstruction(Instruction instruction) {
        for (String alias : instruction.getAliases())
            this.instructions.put(alias, instruction);
    }

    public void registerSubcommand(MappedCommand subcommand) {
        for (String alias : subcommand.getAliases())
            this.subcommands.put(alias, subcommand);
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
        if (!super.equals(o)) return false;
        MappedCommand command = (MappedCommand) o;
        return this.command.equals(command.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command);
    }
}
