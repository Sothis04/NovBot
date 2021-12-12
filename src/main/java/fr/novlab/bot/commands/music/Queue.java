package fr.novlab.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Queue implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if(queue.isEmpty()) {
            event.reply("The queue is empty").queue();
            return;
        }

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> trackList = new ArrayList<>(queue);
        StringBuilder messageAction = new StringBuilder("**Current Queue:**\n");

        if(trackCount == 1) {
            AudioTrack track = trackList.get(0);
            AudioTrackInfo info = track.getInfo();

            messageAction.append('#')
                    .append(String.valueOf(1))
                    .append(" `")
                    .append(info.title)
                    .append("` by `")
                    .append(info.author)
                    .append("` [`")
                    .append(formatTime(track.getDuration()))
                    .append("`]");
        }

        for(int i = 0; i < trackCount; i++) {
            AudioTrack track = trackList.get(i);
            AudioTrackInfo info = track.getInfo();

            messageAction.append('#')
                    .append(String.valueOf(i + 1))
                    .append(" `")
                    .append(info.title)
                    .append("` by `")
                    .append(info.author)
                    .append("` [`")
                    .append(formatTime(track.getDuration()))
                    .append("`]\n");
        }

        if(trackList.size() > trackCount) {
            messageAction.append("And `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` more...");
        }

        event.reply(messageAction.toString()).queue();
    }

    @Override
    public String getCommand() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public List<Perms> getAllowed() {
        return List.of(Perms.ALL);
    }

    @Override
    public String getDescription() {
        return "Description Coming Soon";
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long secondes = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, secondes);
    }
}
