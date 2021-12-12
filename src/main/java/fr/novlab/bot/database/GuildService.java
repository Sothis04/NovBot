package fr.novlab.bot.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.novlab.bot.NovLab;
import org.bson.Document;

import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class GuildService {

    private static final MongoCollection<GuildData> collection = NovLab.getMongoGuild();

    public static GuildData getGuild(String id) {
        return collection.find(eq("guildId", id)).first();
    }

    public static void updateGuild(String id, Consumer<GuildData> consumer) {
        GuildData guildData = getGuild(id);
        consumer.accept(guildData);
        updateGuild(guildData);
    }

    public static void updateGuild(GuildData guildData) {
        Document filterById = new Document("guildId", guildData.getGuildId());
        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        GuildData updatedGuildData = collection.findOneAndReplace(filterById, guildData, returnDocAfterReplace);
    }

    public static boolean isRegistered(String id) {
        if(getGuild(id) != null) {
            return true;
        }
        return false;
    }

    public static void addGuild(GuildData guildData) {
        collection.insertOne(guildData);
    }
}
