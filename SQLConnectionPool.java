import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SQLConnectionPool {

    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/JChat";
    private static final String username = "JChat";
    private static final String password = "j@vaChat84?";
    private static final int poolSize = 10;
    private static final Logger logger = Logger.getLogger(SQLConnectionPool.class.getName());

    private static final List<ConnectionWrapper> availableConnections = createConnections();
    private static final List<ConnectionWrapper> usedConnections = new ArrayList<>();

    private SQLConnectionPool() {
        // Private constructor to prevent instantiation
    }

    public static List<ConnectionWrapper> createConnections() {
        try {
            List<ConnectionWrapper> connections = new ArrayList<>();
            for (int i = 0; i < poolSize; i++) {
                connections.add(new ConnectionWrapper(createConnection()));
            }
            return connections;
        } catch (Exception e) {
            logger.severe("Error creating connections: " + e.getMessage());
            throw new RuntimeException("Error creating connections", e);
        }
    }

    private static Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        if (!connection.isValid(2)) {  // 2 seconds timeout for validation
            throw new SQLException("Connection is not valid.");
        }
        return connection;
    }

    public static synchronized ConnectionWrapper getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            if (usedConnections.size() < poolSize) {
                availableConnections.add(new ConnectionWrapper(createConnection()));
            } else {
                throw new SQLException("No available connections in the pool.");
            }
        }
        ConnectionWrapper wrapper = availableConnections.remove(availableConnections.size() - 1);
        usedConnections.add(wrapper);
        return wrapper;
    }

    public static synchronized void releaseConnection(ConnectionWrapper wrapper) {
        if (wrapper != null && usedConnections.remove(wrapper)) {
            availableConnections.add(wrapper);
        }
    }

    public static int getAvailableConnectionsCount() {
        return availableConnections.size();
    }

    public static int getUsedConnectionsCount() {
        return usedConnections.size();
    }

    public static synchronized void shutdown() throws SQLException {
        for (ConnectionWrapper wrapper : availableConnections) {
            wrapper.close();  // This will close the connection automatically
        }
        for (ConnectionWrapper wrapper : usedConnections) {
            wrapper.close();  // This will close the connection automatically
        }
        availableConnections.clear();
        usedConnections.clear();
    }

    // Wrapper class for automatic connection management
    public static class ConnectionWrapper implements AutoCloseable {
        private final Connection connection;

        public ConnectionWrapper(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }

        @Override
        public void close() {
            releaseConnection(this);
            try {
                connection.close(); // Close the connection when done
            } catch (SQLException e) {
                logger.warning("Failed to close connection: " + e.getMessage());
            }
        }
    }
}
