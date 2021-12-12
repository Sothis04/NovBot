package fr.novlab.bot.database;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

public class PlaylistData {

    @BsonProperty(value = "userId")
    private String userId;
    @BsonProperty(value = "userName")
    private String userName;
    @BsonProperty(value = "listGuilds")
    private List<String> listGuilds;
    @BsonProperty(value = "playlist")
    private List<String> playlist;

    public PlaylistData() {
    }

    public PlaylistData(String userId, String userName, List<String> listGuilds, List<String> playlist) {
        this.userId = userId;
        this.userName = userName;
        this.listGuilds = listGuilds;
        this.playlist = playlist;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getListGuilds() {
        return listGuilds;
    }

    public void setListGuilds(List<String> listGuilds) {
        this.listGuilds = listGuilds;
    }

    public List<String> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<String> playlist) {
        this.playlist = playlist;
    }
}
