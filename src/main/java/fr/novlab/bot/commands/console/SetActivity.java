package fr.novlab.bot.commands.console;

import fr.novlab.bot.NovLab;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class SetActivity {

    private static final JDA api = NovLab.getApi();

    public static void execute(String args) {
        System.out.println(args);
        api.getPresence().setActivity(Activity.watching(args));
    }
}
