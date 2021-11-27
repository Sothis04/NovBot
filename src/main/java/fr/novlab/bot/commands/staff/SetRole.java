package fr.novlab.bot.commands.staff;

import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.managers.command.CommandContext;
import fr.novlab.bot.managers.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetRole implements ICommand {

    @Override
    public void handle(CommandContext context) {
        TextChannel channel = context.getChannel();
        Member member = context.getMember();

        if(member.getPermissions().contains(Permission.ADMINISTRATOR)) {

            if(context.getArgs().size() > 0) {

                String arg = String.valueOf(context.getArgs());
                int index = arg.indexOf(']');
                String arg2 = arg.substring(1, index);

                if(arg2.equalsIgnoreCase("empty")) {

                    GuildService.updateGuild(context.getGuild().getId(), guildData -> {
                        guildData.setRoleDjId("");
                    });

                    channel.sendMessage("Le role a était réinitialiser. Tout les membres peuvent a nouveau controller la musique").queue();

                } else {
                    if(context.getGuild().getRolesByName(arg2, false).stream().findFirst().isPresent()) {

                        Role role = context.getGuild().getRolesByName(arg2, false).stream().findFirst().get();

                        GuildService.updateGuild(context.getGuild().getId(), guildData -> {
                            guildData.setRoleDjId(role.getId());
                        });

                        channel.sendMessage("NovLab a bien pris en compte la demande").queue();

                    } else {
                        System.out.println(arg2);

                        channel.sendMessage("Le bot ne trouve pas de role correspondant").queue();
                        channel.sendMessage("Merci de preciser le nom précis du role").queue();
                    }
                }

            } else {
                channel.sendMessage("Merci de précisez le role qui pourra controller le bot NovLab").queue();
            }

        } else {
            channel.sendMessage("Vous n'avez pas la permission Administrateur").queue();
        }
    }

    @Override
    public String getName() {
        return "setrole";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
