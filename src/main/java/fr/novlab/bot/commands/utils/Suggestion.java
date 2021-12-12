package fr.novlab.bot.commands.utils;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.DocumentService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

public class Suggestion implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();
        String message = "";
        for (int i = 0; i < args.size(); i++) {
            message = message + args.get(i);
        }
        DocumentService.createDocument(guild, user, message);
        event.reply("Votre suggestion a été enregistré").queue();
    }

    @Override
    public String getCommand() {
        return "suggestion";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public List<Perms> getAllowed() {
        return List.of(Perms.ALL);
    }

    @Override
    public String getDescription() {
        return "None";
    }
}
