package st.networkers.rimor.command;

import st.networkers.rimor.internal.command.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(Command command) {
        for (String alias : command.getAliases())
            this.commands.put(alias.toLowerCase(), command);
    }

    public Command getCommand(String alias) {
        return this.commands.get(alias.toLowerCase());
    }
}
