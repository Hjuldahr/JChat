import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO implements DAO<Group> {

    public GroupDAO() {
        super();
    }

    @Override
    public Group get(int id) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM JChatGroups WHERE group_id = ?")) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Group(
                            rs.getInt("group_id"),
                            rs.getString("group_name"),
                            rs.getString("description"),
                            rs.getTimestamp("created_at")
                        );
                    }
                } catch (SQLException sqle) {
                    // Log result set processing error
                    System.err.println("Error processing result set: " + sqle.getMessage());
                }
            } catch (SQLException sqle) {
                // Log SQL exceptions from PreparedStatement
                System.err.println("SQL error occurred: " + sqle.getMessage());
            }
        } catch (SQLException e) {
            // Log connection issues or errors related to acquiring the connection
            System.err.println("Error establishing connection: " + e.getMessage());
        } catch (Exception e) {
            // Log unexpected errors
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }

        // Return null if no user is found
        return null;
    }

    @Override
    public List<Group> getAll(int start, int size) {
        List<Group> groups = new ArrayList<>();
        
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM JChatGroups LIMIT ? OFFSET ?")) {
                stmt.setInt(1, size);  // Set the number of records to fetch
                stmt.setInt(2, start); // Set the starting position (offset)
                
                try (ResultSet rs = stmt.executeQuery()) {  // Use try-with-resources for ResultSet
                    while (rs.next()) {
                        groups.add(new Group(
                            rs.getInt("group_id"),
                            rs.getString("group_name"),
                            rs.getString("description"),
                            rs.getTimestamp("created_at")
                        ));
                    }
                } catch (SQLException sqle) {
                    System.err.println("Error while processing result set: " + sqle.getMessage());
                }
            } catch (SQLException sqle) {
                System.err.println("SQL error occurred: " + sqle.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }

        return groups;
    }

    @Override
    public int set(Group group) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection();
            Connection connection = connectionWrapper.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO JChatGroups (group_name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
    
            // Set parameters for the PreparedStatement
            stmt.setString(1, group.getGroupName());
            stmt.setString(2, group.getDescription());
    
            // Execute the INSERT statement
            int affectedRows = stmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Inserting group failed, no rows affected.");
            }
    
            // Retrieve the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserting group failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            // Log SQL exceptions
            System.err.println("SQL error occurred: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            // Log unexpected exceptions
            System.err.println("Unexpected error occurred: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean update(Group group) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();
        
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE JChatGroups SET group_name = ?, description = ? WHERE group_id = ?")) {
                stmt.setString(1, group.getGroupName());
                stmt.setString(2, group.getDescription());
                stmt.setInt(3, group.getId());
                stmt.executeUpdate();
                return true;
            } catch (SQLIntegrityConstraintViolationException sqle) {
                // Log the exception and handle it (e.g., duplicate key violation)
                System.err.println("Integrity constraint violation: " + sqle.getMessage());
            } catch (SQLException sqle) {
                // Log other SQL exceptions
                System.err.println("SQL error occurred: " + sqle.getMessage());
            }
        } catch (Exception e) {
            // Log unexpected exceptions
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean remove(int id) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();
        
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM JChatGroups WHERE group_id = ?")) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                return true;
            } catch (SQLException sqle) {
                // Log other SQL exceptions
                System.err.println("SQL error occurred: " + sqle.getMessage());
            }
        } catch (Exception e) {
            // Log unexpected exceptions
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }
        return false;
    }
}