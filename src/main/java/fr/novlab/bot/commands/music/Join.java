package fr.novlab.bot.commands.music;

import fr.novlab.bot.NovLab;
import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Constant;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.GuildService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class Join implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        Member member = event.getMember();

        if(botVoiceState.inVoiceChannel()) {
            event.reply(Constant.ALREADY_CONNECT.getMessage()).queue();
            return;
        }

        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            event.reply(Constant.NOT_IN_VOICE_CHANNEL.getMessage()).queue();
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        event.reply("Connected to `\uD83D" + memberChannel.getName() + "\uDD0A`").queue();
    }

    @Override
    public String getCommand() {
        return "join";
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
