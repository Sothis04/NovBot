package fr.novlab.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import fr.novlab.bot.NovLab;
import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Constant;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

public class Skip implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        Member member = event.getMember();

        if (!botVoiceState.inVoiceChannel()) {
            event.reply(Constant.BOT_NOT_IN_CHANNEL.getMessage()).queue();
            return;
        }

        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            event.reply(Constant.NOT_IN_VOICE_CHANNEL.getMessage()).queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(botVoiceState.getChannel())) {
            event.reply(Constant.NOT_IN_SAME_CHANNEL.getMessage()).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null) {
            event.reply("There is no track playing currently").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        event.reply("Music Skip \u23E9").queue();
    }

    @Override
    public String getCommand() {
        return "skip";
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
}
