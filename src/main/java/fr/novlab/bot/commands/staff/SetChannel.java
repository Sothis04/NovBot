package fr.novlab.bot.commands.staff;

import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.managers.command.CommandContext;
import fr.novlab.bot.managers.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetChannel implements ICommand {

    @Override
    public void handle(CommandContext context) {
        TextChannel channel = context.getChannel();
        Member member = context.getMember();

        if(member.getPermissions().contains(Permission.ADMINISTRATOR)) {

            if(context.getArgs().size() > 0) {

                String arg = String.valueOf(context.getArgs());
                int index = arg.indexOf(']');
                String arg2 = arg.substring(1, index);

                if(context.getGuild().getTextChannelById(arg2) != null) {

                    GuildService.updateGuild(context.getGuild().getId(), guildData -> {
                        guildData.setChannelId(arg2);
                    });

                    TextChannel textChannel = context.getGuild().getTextChannelById(arg2);

                    assert textChannel != null;
                    channel.sendMessage("Vous pouvez maintenant utiliser NovLab seulement dans le channel ``" + textChannel.getName() + "``").queue();

                } else {
                    channel.sendMessage("Le salon textuel n'existe pas").queue();
                }

            } else {
                channel.sendMessage("Merci de preciser l'identifiant du channel").queue();
            }
        } else {
            channel.sendMessage("Vous n'avez pas la permission Administrateur").queue();
        }
    }

    @Override
    public String getName() {
        return "setchannel";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
