package fr.novlab.bot.commands.music;

import fr.novlab.bot.utils.Utils;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class Stop implements ICommand {

    @Override
    public void handle(CommandContext context) {
        TextChannel channel = context.getChannel();
        Member bot = context.getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();

        if (!botVoiceState.inVoiceChannel()) {
            channel.sendMessage(Constant.BOT_NOT_IN_CHANNEL.getMessage()).queue();
            return;
        }

        Member member = context.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage(Constant.NOT_IN_VOICE_CHANNEL.getMessage()).queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(botVoiceState.getChannel())) {
            channel.sendMessage(Constant.NOT_IN_SAME_CHANNEL.getMessage()).queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(context.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        Utils.Instance.resetActivity();

        channel.sendMessage("Music stop and queue cleared").queue();

    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "coming soon";
    }
}
