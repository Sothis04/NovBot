package fr.novlab.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(PlayerManager.class);

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandEvent event, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                StringBuilder msg = new StringBuilder("Adding to queue: `")
                        .append(audioTrack.getInfo().title)
                        .append("` by `")
                        .append(audioTrack.getInfo().author)
                        .append("`");

                logger.info(event.getGuild().getName() + " - Listen Music " + audioTrack.getInfo().title + " - " + audioTrack.getInfo().author);

                event.reply(msg.toString()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                if(trackURL.contains("https://youtube.com/playlist")) {
                    List<AudioTrack> tracks = playlist.getTracks();

                    StringBuilder msg = new StringBuilder("Adding to queue: `")
                            .append(String.valueOf(tracks.size()))
                            .append("` musics from playlist `")
                            .append(playlist.getName())
                            .append("`");

                    logger.info(event.getGuild().getName() + " - Listen Music " + playlist.getName() + " - " + tracks.size());

                    event.reply(msg.toString()).queue();

                    for (AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                    }
                } else {
                    AudioTrack audioTrack = playlist.getTracks().get(0);

                    musicManager.scheduler.queue(audioTrack);

                    StringBuilder msg = new StringBuilder("Adding to queue: `")
                            .append(audioTrack.getInfo().title)
                            .append("` by `")
                            .append(audioTrack.getInfo().author)
                            .append("`");

                    event.reply(msg.toString()).queue();

                    playlist.getTracks().clear();
                }

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    public static PlayerManager getINSTANCE() {

        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
