package fr.novlab.bot.commands.music;

import fr.novlab.bot.config.Constant;
import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.managers.command.CommandContext;
import fr.novlab.bot.managers.command.ICommand;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class Loop implements ICommand {

    @Override
    public void handle(CommandContext context) {
        TextChannel channel = context.getChannel();
        Member bot = context.getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
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

        if (!botVoiceState.inVoiceChannel()) {
            channel.sendMessage(Constant.BOT_NOT_IN_CHANNEL.getMessage()).queue();
            return;
        }

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
        boolean newRepeating = !musicManager.scheduler.repeating;

        musicManager.scheduler.repeating = newRepeating;

        channel.sendMessageFormat("The loop has been set to **%s**", newRepeating ? "enable" : "disable").queue();
    }

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String getHelp() {
        return "coming soon";
    }
}
