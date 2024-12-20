import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Server 
{   
    private final int PORT = 8000;
    
    private ServerSocket serverSocket;
    private ClientConnectionListener clientListener;
    private Thread clientConnectionListenerThread;
    private Map<Integer,ClientSession> clientSessions;
    
    public Server()
    {
        try 
        {
            serverSocket = new ServerSocket(PORT);
            clientListener = new ClientConnectionListener();
            clientConnectionListenerThread = new Thread(clientListener);
            clientSessions = new HashMap<>();

            System.out.println("Server init successful");
        }
        catch (Exception e)
        {
            System.err.println("Server init failure: "+e);
        }
    }

    public void startup()
    {
        clientConnectionListenerThread.run();
    }

    private class ClientConnectionListener implements Runnable
    {
        public boolean running;

        public void run()
        {
            running = true;
            while (running)
            {
                try
                {
                    //get connection
                    Socket socket = serverSocket.accept();
                    //create session id
                    int sessionID = 1;
                    while(clientSessions.containsKey(sessionID))
                    {
                        sessionID++; //increments session id until the lowest unused id is found
                    }
                    //send session id to client for local use when sending requests to server
                    try (DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                        output.writeInt(sessionID);
                    }
                    
                    //add to tables
                    clientSessions.put(sessionID, new ClientSession(sessionID, socket));
                }
                catch (Exception e)
                {
                    System.err.println("Client conn listener failure: "+e);
                }
            }
        }
    }

    private class ClientSession implements Runnable
    {
        public boolean running;
        private int userID;
        public int sessionID;
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;

        private final String SUCCESS = "SUCCESS";
        private final String FAILURE = "FAILURE";

        public ClientSession (int sessionID, Socket socket)
        {
            this.sessionID = sessionID;
            this.socket = socket;

            try
            {
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Created client response listenser for session ID: "+sessionID);
            }
            catch(Exception e)
            {
                System.err.println("Failed to creat client response listenser: "+e);
            }
        }

        public void run()
        {
            running = true;
            while (running)
            {
                try
                {
                    Data data = (Data) input.readObject();
                    processInputData(data);
                }
                catch (Exception e)
                {
                    System.err.println("Client response listener failure: "+e);
                }
            }
        }

        public void processInputData(Data data)
        {
            //wrapper automatically recycles connections
            switch (data.getStatus()) {
                case "LOGIN": 
                    login(data); break;
                case "LOGOUT":  
                    break;
                case "CREATE-GROUP":  
                    break;
                case "JOIN-GROUP":  
                    break;
                case "ACCESS-GROUP":  
                    break;
                case "LEAVE-GROUP":  
                    break;
                case "DELETE-GROUP":  
                    break;
                case "CREATE-CHANNEL":  
                    break;
                case "ACCESS-CHANNEL":  
                    break;
                case "DELETE-CHANNEL":  
                    break;
                case "SIGN-UP":  
                    signup(data); break;
                default:
                    break;
            }
        }

        private void login(Data data) {
            try {
                UserDAO dao = new UserDAO();
    
                // Get user ID using DAO
                int userID = dao.getID(data.getContent("uname"), data.getContent("pass"));
    
                // Send Login-specific response
                sendResponse(data.getID(), userID > -1 ? SUCCESS : FAILURE, "uid", Integer.toString(userID));
            } catch (SQLException e) {
                logError("Login failed due to a database error.", e);
                sendResponse(data.getID(), FAILURE, "error", "Database error during login");
            } catch (Exception e) {
                logError("Unexpected error during login.", e);
                sendResponse(data.getID(), FAILURE, "error", "Unexpected error during login");
            }
        }

        private void signup(Data data) {
            try {
                UserDAO dao = new UserDAO();
        
                // Insert new user and get their ID
                int userID = dao.set(new User(data.getContent("uname"), data.getContent("email"), data.getContent("pass")));
        
                // Send Signup-specific response
                sendResponse(data.getID(), userID > -1 ? SUCCESS : FAILURE, "uid", Integer.toString(userID));
            } catch (Exception e) {
                logError("Unexpected error during signup.", e);
                sendResponse(data.getID(), FAILURE, "error", "Unexpected error during signup");
            }
        }
        // Helper method to send a response (flexible for different content)
        private void sendResponse(int requestID, String status, String... content) {
            try {
                output.writeObject(new Data(requestID, status, content));
            } catch (IOException e) {
                logError("Failed to send response.", e);
            }
        }

        // Helper method to log errors
        private void logError(String message, Exception e) {
            System.err.println(message);
            e.printStackTrace(); // Replace with a proper logging framework in production
        }
    }

    public static void main(String[] args)
    {
        Server server = new Server();
        server.startup();
    }
}
