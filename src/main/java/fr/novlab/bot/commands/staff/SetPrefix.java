package fr.novlab.bot.commands.staff;

import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.managers.command.CommandContext;
import fr.novlab.bot.managers.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetPrefix implements ICommand {

    @Override
    public void handle(CommandContext context) {
        TextChannel channel = context.getChannel();
        Member member = context.getMember();

        if(member.getPermissions().contains(Permission.ADMINISTRATOR)) {

            if(context.getArgs().size() > 0) {

                String prefix = String.valueOf(context.getArgs());
                int index = prefix.indexOf(']');
                String fprefix = prefix.substring(1, index);

                if(fprefix.length() < 6) {

                    GuildService.updateGuild(context.getGuild().getId(), guildData -> {
                        guildData.setPrefix(fprefix);
                    });

                    channel.sendMessage("Le nouveau prefix est ``" + fprefix + "``").queue();

                } else {
                    channel.sendMessage("Vous ne pouvez pas choisir un prefix aussi long ").queue();
                }

            } else {
                channel.sendMessage("Merci de preciser un prefix").queue();
            }
        } else {
            channel.sendMessage("Vous n'avez pas la permission Administrateur").queue();
        }
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
