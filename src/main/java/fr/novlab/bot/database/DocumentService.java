package fr.novlab.bot.database;

import com.mongodb.client.MongoCollection;
import fr.novlab.bot.NovLab;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

public class DocumentService {

    private static final MongoCollection<Document> collection = NovLab.getMongoDocument();

    public static void createDocument(Guild guild, User user, String suggestion) {
        Document document = new Document();
        document.append("guildId", guild.getId());
        document.append("guildName", guild.getName());
        document.append("userName", user.getName());
        document.append("userTag", user.getAsTag());
        document.append("suggestion", suggestion);
        collection.insertOne(document);
    }
}
