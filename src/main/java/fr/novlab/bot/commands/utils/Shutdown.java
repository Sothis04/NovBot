package fr.novlab.bot.commands.utils;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Perms;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

public class Shutdown implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        event.reply("Shutdown Bot").queue();
        LOG(Shutdown.class, "Command Execute");
        event.getJDA().shutdown();
        System.exit(0);
    }

    @Override
    public String getCommand() {
        return "shutdown";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public List<Perms> getAllowed() {
        return List.of(Perms.OWNER);
    }

    @Override
    public String getDescription() {
        return "A command destinate for the dev";
    }
}
