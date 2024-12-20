import java.sql.Timestamp;

public class Message extends Entity {
    private int userId;
    private int channelId;
    private int groupId;
    private String content;
    private Timestamp createdAt;

    // Constructor
    public Message(int id, int userId, int channelId, int groupId, String content, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.channelId = channelId;
        this.groupId = groupId;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
