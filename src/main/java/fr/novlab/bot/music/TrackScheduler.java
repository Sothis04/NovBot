package fr.novlab.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import fr.novlab.bot.utils.Utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;
    public boolean repeating = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if(!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        } else {
            Utils.Instance.setActivity(track);
        }
    }

    public void nextTrack() {
        AudioTrack audioTrack = queue.poll();
        this.player.startTrack(audioTrack, false);
        Utils.Instance.setActivity(audioTrack);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            if(this.repeating) {
                this.player.startTrack(track.makeClone(), false);
                return;
            }

            nextTrack();
        } else {
            Utils.Instance.resetActivity();
        }
    }
}
