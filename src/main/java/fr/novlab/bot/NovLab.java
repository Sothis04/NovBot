package fr.novlab.bot;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.novlab.bot.config.Config;
import fr.novlab.bot.database.GuildData;
import fr.novlab.bot.database.GuildService;
import fr.novlab.bot.database.PlaylistData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class NovLab {

    private static JDA api;
    private static MongoCollection<GuildData> mongoGuild;
    private static MongoCollection<PlaylistData> mongoPlaylist;
    private static MongoCollection<Document> mongoDocument;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NovLab.class);

    private NovLab() throws LoginException {

        api = JDABuilder.createDefault(Config.getToken())
                .addEventListeners(new Listener())
                .build();

        api.getPresence().setActivity(Activity.watching("NovLab Coding..."));

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb");
        rootLogger.setLevel(Level.OFF);
    }

    public static void main(String[] args) throws LoginException {
        new NovLab();
    }

    public static void execute() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION, Conventions.USE_GETTERS_FOR_SETTERS)).build();
        CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClient mongoClient = MongoClients.create("mongodb+srv://novlab-bot:js0xVRgZ73ndsx59@novlab.4alqq.mongodb.net/bot?retryWrites=true&w=majority");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("bot").withCodecRegistry(codecRegistry);
        mongoGuild = mongoDatabase.getCollection("guilds", GuildData.class);
        mongoPlaylist = mongoDatabase.getCollection("playlists", PlaylistData.class);
        mongoDocument = mongoDatabase.getCollection("documents");

        for (Guild guild : api.getGuilds()) {
            guild.getAudioManager().closeAudioConnection();
            String guildId = guild.getId();

            if(GuildService.isRegistered(guildId)) {
                LOGGER.info("Connected to guild : " + guild.getName());
                List<GuildChannel> guildChannel = guild.getChannels();
                TextChannel textChannel = (TextChannel) guildChannel.stream().filter(guildChannel1 -> guildChannel1 instanceof TextChannel).findFirst().get();
                Invite invitation = textChannel.createInvite().setTemporary(false).setMaxUses(100).complete();
                GuildService.updateGuild(guildId, guildData -> {
                    guildData.setName(guild.getName());
                    guildData.setInvitation(invitation.getUrl());
                });
            } else {
                List<GuildChannel> guildChannel = guild.getChannels();
                TextChannel textChannel = (TextChannel) guildChannel.stream().filter(guildChannel1 -> guildChannel1 instanceof TextChannel).findFirst().get();
                Invite invitation = textChannel.createInvite().setTemporary(false).setMaxUses(100).complete();
                GuildData guildData = new GuildData(guildId, "", guild.getName(), "", "", invitation.getUrl());
                GuildService.addGuild(guildData);
                LOGGER.info("Database creation for guild : " + guild.getName());
            }
        }
    }

    public static JDA getApi() {
        return api;
    }

    public static MongoCollection<GuildData> getMongoGuild() {
        return mongoGuild;
    }

    public static MongoCollection<Document> getMongoDocument() {
        return mongoDocument;
    }

    public static MongoCollection<PlaylistData> getMongoPlaylist() {
        return mongoPlaylist;
    }
}
