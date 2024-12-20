import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChannelDAO implements DAO<Channel> {

    public ChannelDAO() {
        
    }

    @Override
    public Channel get(int id) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM JChatChannels WHERE channel_id = ?")) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Channel(
                            rs.getInt("channel_id"),
                            rs.getString("channel_name"),
                            rs.getInt("group_id"),
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
    public List<Channel> getAll(int start, int size) {
        List<Channel> channels = new ArrayList<>();
        
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM JChatChannels LIMIT ? OFFSET ?")) {
                stmt.setInt(1, size);  // Set the number of records to fetch
                stmt.setInt(2, start); // Set the starting position (offset)
                
                try (ResultSet rs = stmt.executeQuery()) {  // Use try-with-resources for ResultSet
                    while (rs.next()) {
                        channels.add(new Channel(
                            rs.getInt("channel_id"),
                            rs.getString("channel_name"),
                            rs.getInt("group_id"),
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

        return channels;
    }

    @Override
    public int set(Channel channel) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection();
            Connection connection = connectionWrapper.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO JChatChannels (channel_name, group_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
    
            // Set parameters for the PreparedStatement
            stmt.setString(1, channel.getChannelName());
            stmt.setInt(2, channel.getGroupId());
    
            // Execute the INSERT statement
            int affectedRows = stmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Inserting channel failed, no rows affected.");
            }
    
            // Retrieve the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserting channel failed, no ID obtained.");
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
    public boolean update(Channel channel) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();
        
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE JChatChannels SET channel_name = ?, group_id = ? WHERE channel_id = ?")) {
                stmt.setString(1, channel.getChannelName());
                stmt.setInt(2, channel.getGroupId());
                stmt.setInt(3, channel.getId());
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
        
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM JChatChannels WHERE channel_id = ?")) {
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
