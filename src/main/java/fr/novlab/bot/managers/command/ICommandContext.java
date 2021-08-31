package fr.novlab.bot.managers.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;


public interface ICommandContext {

    default Guild getGuild() {
        return this.getEvent().getGuild();
    }

    GuildMessageReceivedEvent getEvent();

    default TextChannel getChannel() {
        return this.getEvent().getChannel();
    }

    default Message getMessage() {
        return this.getEvent().getMessage();
    }

    default User getAuthor() {
        return this.getEvent().getAuthor();
    }

    default Member getMember() {
        return this.getEvent().getMember();
    }

    default JDA getJDA() {
        return this.getEvent().getJDA();
    }

    default ShardManager getShardManager() {
        return this.getJDA().getShardManager();
    }

    default User getSelfUser() {
        return this.getJDA().getSelfUser();
    }

    default Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }

}