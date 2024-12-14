import java.sql.Connection;
import java.util.ArrayList;

public class UserGroupDAO extends DAO<UserGroup>
{
    //needs more complex sql to deal with associative tables
    //tables
    //Groups, Channels, GroupUsers, GroupChannels
    
    public UserGroupDAO(Connection connection)
    {
        super(connection);
    }
    
    @Override
    public void add(UserGroup entity) 
    {
        
    }

    @Override
    public UserGroup get(int id) {
        
    }

    @Override
    public ArrayList<UserGroup> getAll() {
        
    }

    @Override
    public void update(UserGroup entity) {
        
    }

    @Override
    public void remove(int id) {
        
    }

}
