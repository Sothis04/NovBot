package fr.novlab.bot.commands.music;

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

public class Loop implements Command {

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
        boolean newRepeating = !musicManager.scheduler.repeating;

        musicManager.scheduler.repeating = newRepeating;
        String formatRept = newRepeating ? "enable" : "disable";
        event.reply("The loop has been set to **" + formatRept + "**").queue();
    }

    @Override
    public String getCommand() {
        return "loop";
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
