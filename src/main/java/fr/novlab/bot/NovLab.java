package fr.novlab.bot;

import fr.novlab.bot.config.Config;
import fr.novlab.bot.utils.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;

public class NovLab {

    private static JDA api;

    private NovLab() throws LoginException {

        api = JDABuilder.createDefault(Config.getToken())
                .addEventListeners(new Listener())
                .build();

        for (Guild guild : api.getGuildsByName("NovLab", true)) {
            guild.getAudioManager().closeAudioConnection();
        }

        Utils.Instance.resetActivity();
    }

    public static void main(String[] args) throws LoginException {
        new NovLab();
    }

    public static JDA getApi() {
        return api;
    }
}
