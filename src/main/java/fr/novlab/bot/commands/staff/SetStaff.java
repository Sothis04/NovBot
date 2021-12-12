package fr.novlab.bot.commands.staff;

import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.GuildService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

public class SetStaff implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        Member member = event.getMember();

        if(member.getPermissions().contains(Permission.ADMINISTRATOR)) {

            if (args.size() > 0) {

                String arg = String.valueOf(args);
                int index = arg.indexOf(']');
                String arg2 = arg.substring(1, index);

                if(arg2.equalsIgnoreCase("empty")) {

                    GuildService.updateGuild(event.getGuild().getId(), guildData -> {
                        guildData.setRoleStaffId("");
                    });

                    event.reply("Le role a était réinitialiser. Seul les administrateurs peuvent modifier les settings du bot NovLab").queue();

                } else {
                    if(event.getGuild().getRolesByName(arg2, false).stream().findFirst().isPresent()) {

                        Role role = event.getGuild().getRolesByName(arg2, false).stream().findFirst().get();

                        GuildService.updateGuild(event.getGuild().getId(), guildData -> {
                            guildData.setRoleStaffId(role.getId());
                        });

                        event.reply("NovLab a bien pris en compte la demande").queue();

                    } else {
                        System.out.println(arg2);

                        event.reply("Le bot ne trouve pas de role correspondant").queue();
                        event.reply("Merci de preciser le nom précis du role").queue();
                    }
                }

            } else {
                event.reply("Merci de précisez le role qui pourra controller le bot NovLab").queue();
            }
        } else {
            event.reply("Vous n'avez pas la permission Administrateur").queue();
        }
    }

    @Override
    public String getCommand() {
        return "setstaff";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public List<Perms> getAllowed() {
        return List.of(Perms.ADMINISTRATOR);
    }

    @Override
    public String getDescription() {
        return "Description Coming Soon";
    }
}
