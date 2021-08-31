package fr.novlab.bot.config;

public class Config {

    static String token = "token";
    static String channelBot = "879773370265260083";
    static String joinChannel = "879802376494653480";
    static String prefix = "/";

    public static String getToken() {
        return token;
    }

    public static String getChannelBot() {
        return channelBot;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getJoinChannel() {
        return joinChannel;
    }
}
