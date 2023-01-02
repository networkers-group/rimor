package st.networkers.rimor.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, MappedCommand> commands = new HashMap<>();

    public void registerCommand(MappedCommand command) {
        this.commands.put(command.getName().toLowerCase(), command);
        command.getAliases().forEach(alias -> this.commands.put(alias.toLowerCase(), command));
    }

    public Collection<MappedCommand> getRegisteredCommands() {
        return this.commands.values();
    }

    public MappedCommand getCommand(String alias) {
        return this.commands.get(alias.toLowerCase());
    }
}
