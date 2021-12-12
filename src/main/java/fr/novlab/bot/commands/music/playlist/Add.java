package fr.novlab.bot.commands.music.playlist;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.novlab.bot.commands.manager.Command;
import fr.novlab.bot.config.Perms;
import fr.novlab.bot.database.PlaylistData;
import fr.novlab.bot.database.PlaylistService;
import fr.novlab.bot.music.GuildMusicManager;
import fr.novlab.bot.music.PlayerManager;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Add implements Command {

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.size() <= 0) {
            System.out.println("Debug");
            if(PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack() != null) {
                System.out.println("Debug");
                AudioTrack audioTrack = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack();
                String url = audioTrack.getInfo().uri;
                if(PlaylistService.isRegistered(event.getUser().getId())) {
                    PlaylistService.updatePlaylist(event.getUser().getId(), playlistData -> {
                        playlistData.getPlaylist().add(url);
                        if(!playlistData.getListGuilds().contains(event.getGuild().getId())) {
                            playlistData.getListGuilds().add(event.getGuild().getId());
                        }
                    });
                    event.reply("Ajout a votre playlist - ``" + audioTrack.getInfo().title + "`` de ``" + audioTrack.getInfo().author + "``").queue();
                } else {
                    System.out.println("Debug");
                    PlaylistData playlistData = new PlaylistData(event.getUser().getId(), event.getUser().getName(), Arrays.asList(event.getGuild().getId()), Arrays.asList(url));
                    PlaylistService.addPlaylist(playlistData);
                    event.reply("Ajout a votre playlist - ``" + audioTrack.getInfo().title + "`` de ``" + audioTrack.getInfo().author + "``").queue();
                }
            } else {
                event.reply("No music is playing").queue();
            }
        } else {
            AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
            audioPlayerManager.loadItem((AudioReference) args, new AudioLoadResultHandler() {
                
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
                    if(PlaylistService.isRegistered(event.getUser().getId())) {
                        PlaylistService.updatePlaylist(event.getUser().getId(), playlistData -> {
                            playlistData.getPlaylist().add(audioTrack.getInfo().uri);
                            if(!playlistData.getListGuilds().contains(event.getGuild().getId())) {
                                playlistData.getListGuilds().add(event.getGuild().getId());
                            }
                        });
                        event.reply("Ajout a votre playlist - ``" + audioTrack.getInfo().title + "`` de ``" + audioTrack.getInfo().author + "``").queue();
                    } else {
                        PlaylistData playlistData = new PlaylistData(event.getUser().getId(), event.getUser().getName(), Arrays.asList(event.getGuild().getId()), Arrays.asList(audioTrack.getInfo().uri));
                        PlaylistService.addPlaylist(playlistData);
                        event.reply("Ajout a votre playlist - ``" + audioTrack.getInfo().title + "`` de ``" + audioTrack.getInfo().author + "``").queue();
                    }
                }

                @Override
                public void playlistLoaded(AudioPlaylist audioPlaylist) {
                    if(PlaylistService.isRegistered(event.getUser().getId())) {
                        PlaylistService.updatePlaylist(event.getUser().getId(), playlistData -> {
                            audioPlaylist.getTracks().forEach(track -> {
                                playlistData.getPlaylist().add(track.getInfo().uri);
                            });
                            if(!playlistData.getListGuilds().contains(event.getGuild().getId())) {
                                playlistData.getListGuilds().add(event.getGuild().getId());
                            }
                        });
                        event.reply("Ajout a votre playlist - ``" + audioPlaylist.getName() + "`` contenant ``" + audioPlaylist.getTracks().size() + "`` musics").queue();
                    } else {
                        List<String> allMusics = new ArrayList<>();
                        audioPlaylist.getTracks().forEach(track -> {
                            allMusics.add(track.getInfo().uri);
                        });
                        PlaylistData playlistData = new PlaylistData(event.getUser().getId(), event.getUser().getName(), Arrays.asList(event.getGuild().getId()), allMusics);
                        PlaylistService.addPlaylist(playlistData);
                        event.reply("Ajout a votre playlist - ``" + audioPlaylist.getName() + "`` contenant ``" + audioPlaylist.getTracks().size() + "`` musics").queue();
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
    }

    @Override
    public String getCommand() {
        return "padd";
    }

    @Override
    public String getHelp() {
        return "Coming Soon";
    }

    @Override
    public List<Perms> getAllowed() {
        return List.of(Perms.ALL);
    }

    @Override
    public String getDescription() {
        return "Coming Soon";
    }
}
