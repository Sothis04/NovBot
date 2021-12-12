package fr.novlab.bot.commands.staff;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.GuildService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

public class SetChannel implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        GuildChannel guildChannel = event.getOption("channel").getAsGuildChannel();

        if(guildChannel instanceof TextChannel) {

            GuildService.updateGuild(event.getGuild().getId(), guildData -> {
                guildData.setChannelId(guildChannel.getId());
            });

            event.reply("Vous pouvez maintenant utiliser NovLab seulement dans le channel ``" + guildChannel.getName() + "``").queue();

        } else {
            event.reply("You need to specify a text channel");
        }
    }

    @Override
    public String getCommand() {
        return "setchannel";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public List<Perms> getAllowed() {
        return List.of(Perms.STAFF, Perms.ADMINISTRATOR);
    }

    @Override
    public String getDescription() {
        return "Description Coming Soon";
    }
}
