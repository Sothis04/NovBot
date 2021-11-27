package fr.novlab.bot.commands.staff;

import fr.novlab.bot.managers.command.CommandContext;
import fr.novlab.bot.managers.command.ICommand;

public class SetStaff implements ICommand {

    @Override
    public void handle(CommandContext context) {

    }

    @Override
    public String getName() {
        return "setstaff";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
