package fr.novlab.bot.commands.music;

import fr.novlab.bot.config.Constant;
import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.managers.command.CommandContext;
import fr.novlab.bot.managers.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

@SuppressWarnings("ConstantConditions")
public class Join implements ICommand {

    @SuppressWarnings("ConstantConditions")
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

        if(botVoiceState.inVoiceChannel()) {
            channel.sendMessage(Constant.ALREADY_CONNECT.getMessage()).queue();
            return;
        }

        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage(Constant.NOT_IN_VOICE_CHANNEL.getMessage()).queue();
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessageFormat("Connected to `\uD83D\uDD0A %s`", memberChannel.getName()).queue();
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "coming soon";
    }
}
