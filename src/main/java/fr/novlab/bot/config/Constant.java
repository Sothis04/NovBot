package fr.novlab.bot.config;

public enum Constant {

    NOT_IN_VOICE_CHANNEL("You need to be in a voice channel"),
    ALREADY_CONNECT("I'm already in a voice channel"),
    NOT_IN_SAME_CHANNEL("You need to be in the same voice channel"),
    BOT_NOT_IN_CHANNEL("I need to be in a voice channel");

    String message;

    Constant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
