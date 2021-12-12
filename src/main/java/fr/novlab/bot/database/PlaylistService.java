package fr.novlab.bot.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.novlab.bot.NovLab;
import org.bson.Document;

import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class PlaylistService {

    private static final MongoCollection<PlaylistData> collection = NovLab.getMongoPlaylist();

    public static PlaylistData getPlaylist(String id) {
        return collection.find(eq("userId", id)).first();
    }

    public static void updatePlaylist(String id, Consumer<PlaylistData> consumer) {
        PlaylistData playlistData = getPlaylist(id);
        consumer.accept(playlistData);
        updateGuild(playlistData);
    }

    public static void updateGuild(PlaylistData playlistData) {
        Document filterById = new Document("userId", playlistData.getUserId());
        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        PlaylistData updatePlaylistData = collection.findOneAndReplace(filterById, playlistData, returnDocAfterReplace);
    }

    public static boolean isRegistered(String id) {
        if(getPlaylist(id) != null) {
            return true;
        }
        return false;
    }

    public static void addPlaylist(PlaylistData playlistData) {
        collection.insertOne(playlistData);
    }
}
