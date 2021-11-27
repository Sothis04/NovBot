package fr.novlab.bot;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.novlab.bot.commands.console.Manager;
import fr.novlab.bot.config.Config;
import fr.novlab.bot.database.GuildData;
import fr.novlab.bot.database.GuildService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Arrays;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class NovLab {

    private static JDA api;
    private static MongoCollection<GuildData> mongoCollection;

    private NovLab() throws LoginException {

        api = JDABuilder.createDefault(Config.getToken())
                .addEventListeners(new Listener())
                .build();

        api.getPresence().setActivity(Activity.watching("NovLab Coding..."));
    }

    public static void main(String[] args) throws LoginException, IOException {
        new NovLab();

        Manager.loadManager();
    }

    public static void execute() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION, Conventions.USE_GETTERS_FOR_SETTERS)).build();
        CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClient mongoClient = MongoClients.create("mongodb+srv://novlab-bot:js0xVRgZ73ndsx59@novlab.4alqq.mongodb.net/bot?retryWrites=true&w=majority");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("bot").withCodecRegistry(codecRegistry);
        mongoCollection = mongoDatabase.getCollection("guilds", GuildData.class);

        for (Guild guild : api.getGuilds()) {
            guild.getAudioManager().closeAudioConnection();
            String guildId = guild.getId();

            if(GuildService.isRegistered(guildId)) {
                System.out.println("Connected to guild : " + guild.getName());
                GuildService.updateGuild(guildId, guildData -> {
                    guildData.setName(guild.getName());
                });
            } else {
                GuildData guildData = new GuildData(guildId, "", guild.getName(), "", "", "/");
                GuildService.addGuild(guildData);
                System.out.println("Database creation for guild : " + guild.getName());
            }
        }
    }

    public static JDA getApi() {
        return api;
    }

    public static MongoCollection<GuildData> getMongoCollection() {
        return mongoCollection;
    }
}
