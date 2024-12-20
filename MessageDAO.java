import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO implements DAO<Message> {

    public MessageDAO() {
       
    }

    @Override
    public Message get(int id) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM JChatMessages WHERE message_id = ?")) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Create and return the User object
                        return new Message(
                            rs.getInt("message_id"),
                            rs.getInt("user_id"),
                            rs.getInt("channel_id"),
                            rs.getInt("group_id"),
                            rs.getString("content"),
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
    public List<Message> getAll(int start, int size) {
        List<Message> messages = new ArrayList<>();
        
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM JChatUsers LIMIT ? OFFSET ?")) {
                stmt.setInt(1, size);  // Set the number of records to fetch
                stmt.setInt(2, start); // Set the starting position (offset)
                
                try (ResultSet rs = stmt.executeQuery()) {  // Use try-with-resources for ResultSet
                    while (rs.next()) {
                        messages.add(new Message(
                            rs.getInt("message_id"),
                            rs.getInt("user_id"),
                            rs.getInt("channel_id"),
                            rs.getInt("group_id"),
                            rs.getString("content"),
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

        return messages;
    }

    @Override
    public int set(Message message) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection();
            Connection connection = connectionWrapper.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO JChatMessages (user_id, channel_id, group_id, content) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
    
            // Set parameters for the PreparedStatement
            stmt.setInt(1, message.getUserId());
            stmt.setInt(2, message.getChannelId());
            stmt.setInt(3, message.getGroupId());
            stmt.setString(4, message.getContent());
    
            // Execute the INSERT statement
            int affectedRows = stmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Inserting message failed, no rows affected.");
            }
    
            // Retrieve the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserting message failed, no ID obtained.");
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
    public boolean update(Message message) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();
        
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE JChatMessages SET content = ? WHERE message_id = ?")) {
                stmt.setString(1, message.getContent());
                stmt.setInt(2, message.getId());
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
        
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM JChatMessages WHERE message_id = ?")) {
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
