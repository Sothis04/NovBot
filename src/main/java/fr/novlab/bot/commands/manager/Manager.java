package fr.novlab.bot.commands.manager;

import fr.novlab.bot.NovLab;
import fr.novlab.bot.commands.music.Queue;
import fr.novlab.bot.commands.music.*;
import fr.novlab.bot.commands.music.playlist.Add;
import fr.novlab.bot.commands.staff.SetChannel;
import fr.novlab.bot.commands.staff.SetRole;
import fr.novlab.bot.commands.staff.SetStaff;
import fr.novlab.bot.commands.utils.Shutdown;
import fr.novlab.bot.commands.utils.Suggestion;
import fr.novlab.bot.config.Config;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.GuildService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

public class Manager {

    private final Map<String, Command> commands = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(Manager.class);

    public Manager() {
        // Music
        addCommand(new Join());
        addCommand(new Leave());
        addCommand(new Loop());
        addCommand(new NowPlay());
        addCommand(new Play());
        addCommand(new Queue());
        addCommand(new Skip());
        addCommand(new Stop());
        // // PlayList
        addCommand(new Add());
        // Staff
        addCommand(new SetRole());
        addCommand(new SetStaff());
        addCommand(new SetChannel());
        // Utils
        addCommand(new Suggestion());
        // Owner
        addCommand(new Shutdown());
    }

    public void loadSlash() {
        CommandListUpdateAction CMDS = NovLab.getApi().updateCommands();
        Map<String, Command> cmds = new HashMap<>();
        List<String> prohibitedCMDS = new ArrayList<>(List.of("play", "setrole", "setstaff", "setchannel", "suggestion"));
        commands.forEach((s, command) -> {
            if(!prohibitedCMDS.contains(s)) {
                cmds.put(s, command);
            }
        });
        CMDS.queue();
        cmds.forEach((s, command) -> {
            CMDS.addCommands(new CommandData(s, command.getDescription()));
        });
        CMDS.addCommands(new CommandData("play", commands.get("play").getDescription()).addOptions(new OptionData(OptionType.STRING, "song", "Play a song").setRequired(true))).queue();
        CMDS.addCommands(new CommandData("setrole", commands.get("setrole").getDescription()).addOptions(new OptionData(OptionType.ROLE, "role", "The Role who can access to the music of NovLab").setRequired(true))).queue();
        CMDS.addCommands(new CommandData("setstaff", commands.get("setstaff").getDescription()).addOptions(new OptionData(OptionType.ROLE, "role", "The Role who can access to the bot NovLab").setRequired(true))).queue();
        CMDS.addCommands(new CommandData("setchannel", commands.get("setchannel").getDescription()).addOptions(new OptionData(OptionType.CHANNEL, "channel", "The channel who can only use the bot for the music").setRequired(true))).queue();
        CMDS.addCommands(new CommandData("suggestion", commands.get("suggestion").getDescription()).addOptions(new OptionData(OptionType.STRING, "suggestion", "Enter a suggestion for the developper").setRequired(true))).queue();
        CMDS.queue();
        LOGGER.info("Slash Command Registered");
    }

    private void addCommand(Command command) {
        if(!commands.containsKey(command.getCommand())) {
            commands.put(command.getCommand(), command);
        }
    }

    public Collection<Command> getCommands() {
        return commands.values();
    }

    public void run(SlashCommandEvent event) {
        final String msg = event.getCommandString();

        final String[] split = msg.replaceFirst("(?i)" + Pattern.quote("/"), "").split("\\s+");

        final String command = event.getName();

        if(commands.containsKey(command)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            Command cmd = commands.get(command);
            event.getChannel().sendTyping().queue();

            if(cmd.getAllowed().contains(Perms.ALL)) {
                cmd.run(args, event);
                return;
            } else if(cmd.getAllowed().contains(Perms.DJ)) {
                String djId = GuildService.getGuild(event.getGuild().getId()).getRoleDjId();
                if(djId.equals("")) {
                    cmd.run(args, event);
                } else {
                    if(event.getGuild().getRoleById(djId) != null) {
                        Role role = event.getGuild().getRoleById(djId);
                        if(event.getMember().getRoles().contains(role)) {
                            cmd.run(args, event);
                            return;
                        } else {
                            event.getChannel().sendMessage("Vous n'avez pas la permission d'executer cette commande").queue();
                            return;
                        }
                    } else {
                        event.getChannel().sendMessage("Le role qui avais était configuré pour utiliser le bot n'existe plus").queue();
                        return;
                    }
                }
            } else if(cmd.getAllowed().contains(Perms.STAFF)) {
                String staffId = GuildService.getGuild(event.getGuild().getId()).getRoleStaffId();
                if(staffId.equals("")) {
                    cmd.run(args, event);
                } else {
                    if(event.getGuild().getRoleById(staffId) != null) {
                        Role role = event.getGuild().getRoleById(staffId);
                        if(event.getMember().getRoles().contains(role)) {
                            cmd.run(args, event);
                            return;
                        } else {
                            event.getChannel().sendMessage("Vous n'avez pas la permission d'executer cette commande").queue();
                            return;
                        }
                    } else {
                        event.getChannel().sendMessage("Le role qui avais était configuré pour utiliser le bot n'existe plus").queue();
                        return;
                    }
                }
            } else if(cmd.getAllowed().contains(Perms.ADMINISTRATOR)) {
                if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                    cmd.run(args, event);
                    return;
                } else {
                    event.getChannel().sendMessage("Vous n'avez pas la permission d'executer cette commande").queue();
                    return;
                }
            } else if(cmd.getAllowed().contains(Perms.OWNER)) {
                if(event.getUser().getId().equals(Config.getOwnerId())) {
                    cmd.run(args, event);
                    return;
                } else {
                    event.getChannel().sendMessage("Vous n'avez pas la permission d'executer cette commande").queue();
                    return;
                }
            }
        }
    }
}
