import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User> {

    public UserDAO() {
        
    }

    @Override
    public User get(int id) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM JChatUsers WHERE user_id = ?")) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Create and return the User object
                        return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
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

    public int getID(String user, String pass) throws SQLException {     
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();
        
            try (PreparedStatement stmt = connection.prepareStatement("SELECT user_id FROM JChatUsers WHERE username = ? AND password_hash = ?")) {
                stmt.setString(1, user);
                stmt.setString(2, pass);
                return stmt.executeQuery().getInt(0);
            } catch (SQLException sqle) {
                // Log other SQL exceptions
                System.err.println("SQL error occurred: " + sqle.getMessage());
            }
        } catch (Exception e) {
            // Log unexpected exceptions
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public List<User> getAll(int start, int size) {
        List<User> users = new ArrayList<>();
        
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM JChatUsers LIMIT ? OFFSET ?")) {
                stmt.setInt(1, size);  // Set the number of records to fetch
                stmt.setInt(2, start); // Set the starting position (offset)
                
                try (ResultSet rs = stmt.executeQuery()) {  // Use try-with-resources for ResultSet
                    while (rs.next()) {
                        users.add(new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
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

        return users;
    }


    @Override
    public int set(User user) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection();
            Connection connection = connectionWrapper.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO JChatUsers (username, email, password_hash) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
    
            // Set parameters for the PreparedStatement
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
    
            // Execute the INSERT statement
            int affectedRows = stmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Inserting user failed, no rows affected.");
            }
    
            // Retrieve the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserting user failed, no ID obtained.");
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
    public boolean update(User user) {
        try (SQLConnectionPool.ConnectionWrapper connectionWrapper = SQLConnectionPool.getConnection()) {
            Connection connection = connectionWrapper.getConnection();
        
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE JChatUsers SET username = ?, email = ?, password_hash = ? WHERE user_id = ?")) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPasswordHash());
                stmt.setInt(4, user.getId());
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
        
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM JChatUsers WHERE user_id = ?")) {
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