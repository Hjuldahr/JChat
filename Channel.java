import java.sql.Timestamp;

public class Channel extends Entity {
    private String channelName;
    private int groupId;
    private Timestamp createdAt;

    // Constructor
    public Channel(int id, String channelName, int groupId, Timestamp createdAt) {
        this.id = id;
        this.channelName = channelName;
        this.groupId = groupId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
