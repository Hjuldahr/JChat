import java.sql.Timestamp;

public class Group extends Entity {
    private String groupName;
    private String description;
    private Timestamp createdAt;

    // Constructor
    public Group(int id, String groupName, String description, Timestamp createdAt) {
        this.id = id;
        this.groupName = groupName;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}