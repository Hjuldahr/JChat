import java.util.ArrayList;
import java.util.List;

public class UserGroup 
{
    public int groupID;
    public String groupName;
    public List<UserProfile> groupProfiles;
    public List<GroupChannel> groupChannels;

    public UserGroup(int groupID, String groupName, List<UserProfile> groupProfiles, List<GroupChannel> groupChannels)
    {   
        this.groupID = groupID;
        this.groupName = groupName;
        this.groupProfiles = groupProfiles;
        this.groupChannels = groupChannels;
    }
    public UserGroup(int groupID, String groupName)
    {   
        this(groupID, groupName, new ArrayList<>(), new ArrayList<>());
    }
}
