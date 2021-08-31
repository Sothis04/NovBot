package fr.novlab.bot.managers.command;

import java.util.List;

public interface ICommand {

    void handle(CommandContext context);

    String getName();

    default List<String> getAliases() {
        return List.of();
    }

    String getHelp();
}
