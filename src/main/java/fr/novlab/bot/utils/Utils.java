package fr.novlab.bot.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.novlab.bot.NovLab;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    public static final Utils Instance = new Utils();
    private final JDA api = NovLab.getApi();
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public void setActivity(AudioTrack audioTrack) {
        if(audioTrack == null) {
            resetActivity();
            return;
        }

        String title;
        String author;

        if(audioTrack.getInfo().title == null) {
            title = "UNKNOWN";
        } else {
            title = audioTrack.getInfo().title;
        }

        if(audioTrack.getInfo().author == null) {
            author = "UNKNOWN";
        } else {
            author = audioTrack.getInfo().author;
        }

        api.getPresence().setActivity(Activity.streaming(title + " by " + author, audioTrack.getInfo().uri));
        LOGGER.info("Listen " + title + " by " + author);
    }

    public void resetActivity() {
        api.getPresence().setActivity(Activity.watching("NovLab Coding..."));
        LOGGER.info("Reset Status");
    }
}
