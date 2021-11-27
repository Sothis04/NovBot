package fr.novlab.bot.commands.music;

import fr.novlab.bot.config.Constant;
import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.managers.command.CommandContext;
import fr.novlab.bot.managers.command.ICommand;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

public class Play implements ICommand {

    @Override
    public void handle(CommandContext context) {
        TextChannel channel = context.getChannel();
        String id;
        if(GuildService.getGuild(context.getGuild().getId()).getRoleDjId() != null) {
            id = GuildService.getGuild(context.getGuild().getId()).getRoleDjId();
        } else {
            id = null;
        }
        Member member = context.getMember();

        if(!id.equals("")) {
            if(context.getGuild().getRoleById(id) != null) {
                Role dj = context.getGuild().getRoleById(id);
                if(!member.getRoles().contains(dj) && !member.getPermissions().contains(Permission.ADMINISTRATOR)) {
                    channel.sendMessage("Vous n'avez pas les permissions pour controllez NovLab").queue();
                }
            } else {
                channel.sendMessage("Le role qui avais était configuré pour utiliser le bot n'existe plus").queue();
            }
        }

        if(context.getArgs().isEmpty()) {
            channel.sendMessage("You need an URL/TitleSong").queue();
        }

        Member bot = context.getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage(Constant.NOT_IN_VOICE_CHANNEL.getMessage()).queue();
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        VoiceChannel memberChannel = memberVoiceState.getChannel();

        if(!channel.getGuild().getAudioManager().isConnected()) {
            audioManager.openAudioConnection(memberChannel);
            channel.sendMessageFormat("Connected to `\uD83D\uDD0A %s`", memberChannel.getName()).queue();
        } else {
            if(!memberVoiceState.getChannel().equals(botVoiceState.getChannel())) {
                channel.sendMessage(Constant.NOT_IN_SAME_CHANNEL.getMessage()).queue();
                return;
            }
        }

        String link = String.join(" ", context.getArgs());

        if(!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getINSTANCE().loadAndPlay(channel, link);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "coming soon";
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
