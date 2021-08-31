package fr.novlab.bot.managers;

import fr.novlab.bot.commands.music.*;
import fr.novlab.bot.config.Config;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        // Music
        addCommand(new Join());
        addCommand(new Play());
        addCommand(new Stop());
        addCommand(new Skip());
        addCommand(new NowPlay());
        addCommand(new Queue());
        addCommand(new Loop());
        addCommand(new Leave());
    }

    public void addCommand(ICommand cmd) {
        boolean nameFound = commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if(nameFound) {
            throw new IllegalArgumentException("Debug");
        }

        commands.add(cmd);
    }

    @Nullable
    private ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if(cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.getPrefix()), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if(cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext context = new CommandContext(event, args);

            cmd.handle(context);
        }
    }
}
