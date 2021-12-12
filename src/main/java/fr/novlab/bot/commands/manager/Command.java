package fr.novlab.bot.commands.manager;

import fr.novlab.bot.config.Perms;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface Command {

    void run(List<String> args, SlashCommandEvent event);

    String getCommand();

    String getHelp();

    List<Perms> getAllowed();

    String getDescription();

    default void LOG(Class clazz, String msg) {
        LoggerFactory.getLogger(clazz).info(msg);
    }
}
