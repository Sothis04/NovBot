package fr.novlab.bot.database;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class GuildData {

    @BsonProperty(value = "guildId")
    private String guildId;
    @BsonProperty(value = "channelId")
    private String channelId;
    @BsonProperty(value = "name")
    private String name;
    @BsonProperty(value = "roleStaffId")
    private String roleStaffId;
    @BsonProperty(value = "roleDjId")
    private String roleDjId;
    @BsonProperty(value = "invite")
    private String invitation;

    public GuildData(String guildId, String channelId, String name, String roleStaffId, String roleDjId, String invitation) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.name = name;
        this.roleStaffId = roleStaffId;
        this.roleDjId = roleDjId;
        this.invitation = invitation;
    }

    public GuildData() {

    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleStaffId() {
        return roleStaffId;
    }

    public void setRoleStaffId(String roleStaffId) {
        this.roleStaffId = roleStaffId;
    }

    public String getRoleDjId() {
        return roleDjId;
    }

    public void setRoleDjId(String roleDjId) {
        this.roleDjId = roleDjId;
    }

    public String getInvitation() {
        return invitation;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }
}
