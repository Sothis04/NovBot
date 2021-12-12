package fr.novlab.bot;

import fr.novlab.bot.commands.manager.Manager;
import fr.novlab.bot.database.GuildData;
import fr.novlab.bot.database.GuildService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class Listener extends ListenerAdapter implements EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    public final Manager commandManager = new Manager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());

        commandManager.loadSlash();
        NovLab.execute();
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        Guild guild = event.getGuild();
        if(GuildService.isRegistered(guild.getId())) {
            List<GuildChannel> guildChannel = guild.getChannels();
            TextChannel textChannel = (TextChannel) guildChannel.stream().filter(guildChannel1 -> guildChannel1 instanceof TextChannel).findFirst().get();
            Invite invitation = textChannel.createInvite().setTemporary(false).setMaxUses(100).complete();
            GuildService.updateGuild(guild.getId(), guildData -> {
                guildData.setName(guild.getName());
                guildData.setInvitation(invitation.getUrl());
            });
            LOGGER.info("Connected to guild : " + guild.getName());
        } else {
            List<GuildChannel> guildChannel = guild.getChannels();
            TextChannel textChannel = (TextChannel) guildChannel.stream().filter(guildChannel1 -> guildChannel1 instanceof TextChannel).findFirst().get();
            Invite invitation = textChannel.createInvite().setTemporary(false).setMaxUses(100).complete();
            GuildData guildData = new GuildData(guild.getId(), "", guild.getName(), "", "", invitation.getUrl());
            GuildService.addGuild(guildData);
            LOGGER.info("Database creation for guild : " + guild.getName());
        }
    }

    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        String id = GuildService.getGuild(event.getGuild().getId()).getChannelId();
        if(id.equals("")) {
            commandManager.run(event);
        } else {
            if(event.getChannel().getId().equals(id)) {
                commandManager.run(event);
            } else {
                return;
            }
        }
    }
}
