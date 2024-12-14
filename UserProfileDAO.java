import java.sql.*;
import java.util.ArrayList;

public class UserProfileDAO extends DAO<UserProfile>
{   
    public UserProfileDAO(Connection connection)
    {
        super(connection);
    }

    @Override
    public void add(UserProfile user) 
    {
        String query = "INSERT INTO Users (userID, userName, userPassword) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) 
        {
            statement.setInt(1, user.userID);
            statement.setString(2, user.userName);
            statement.setString(3, user.userPassword);
            statement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    public UserProfile get(int id) 
    {
        String query = "SELECT userName, userPassword FROM Users WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) 
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) 
            {
                return new UserProfile(
                    id,
                    resultSet.getString("userName"),
                    resultSet.getString("userPassword")
                );
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<UserProfile> getAll() 
    {
        ArrayList<UserProfile> users = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (Statement statement = connection.createStatement()) 
        {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) 
            {
                users.add(new UserProfile(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
                ));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void update(UserProfile user) 
    {
        String query = "UPDATE Users SET userName = ?, userPassword = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) 
        {
            statement.setString(1, user.userName);
            statement.setString(2, user.userPassword);
            statement.setInt(3, user.userID);
            statement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM Users WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) 
        {
            statement.setInt(1, id);
            statement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}
