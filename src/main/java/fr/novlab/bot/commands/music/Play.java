package fr.novlab.bot.commands.music;

import fr.novlab.bot.NovLab;
import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Constant;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Play implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        Member bot = event.getGuild().getSelfMember();
        Member member = event.getMember();

        if(args.isEmpty()) {
            event.reply("You need an URL/TitleSong").queue();
        }

        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            event.reply(Constant.NOT_IN_VOICE_CHANNEL.getMessage()).queue();
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        VoiceChannel memberChannel = memberVoiceState.getChannel();

        if(!channel.getGuild().getAudioManager().isConnected()) {
            audioManager.openAudioConnection(memberChannel);
            event.reply("Connected to `\uD83D" + memberChannel.getName() + "\uDD0A`").queue();
        } else {
            if(!memberVoiceState.getChannel().equals(botVoiceState.getChannel())) {
                event.reply(Constant.NOT_IN_SAME_CHANNEL.getMessage()).queue();
                return;
            }
        }

        System.out.println(args);

        String link = String.join(" ", args);

        System.out.println(link);

        if(!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        System.out.println(link);

        PlayerManager.getINSTANCE().loadAndPlay(event, link);
    }

    @Override
    public String getCommand() {
        return "play";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public List<Perms> getAllowed() {
        return List.of(Perms.DJ, Perms.STAFF);
    }

    @Override
    public String getDescription() {
        return "Description Coming Soon";
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
