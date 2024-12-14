import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;

public class Server 
{   
    private final int PORT = 8000;
    
    private ServerSocket serverSocket;
    private ClientConnectionListener clientListener;
    private Thread clientConnectionListenerThread;
    private TreeMap<Integer,Thread> clientSessionThreads;
    
    public Server()
    {
        try 
        {
            serverSocket = new ServerSocket(PORT);
            clientListener = new ClientConnectionListener();
            clientConnectionListenerThread = new Thread(clientListener);
            clientSessionThreads = new TreeMap<>();
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
                    while(clientSessionThreads.containsKey(sessionID))
                    {
                        sessionID++; //increments session id until the lowest unused id is found
                    }
                    //send session id to client for local use when sending requests to server
                    try (DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                        output.writeInt(sessionID);
                    }
                    
                    //get input stream
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    //read id
                    int id = input.readInt();
                    //close connection
                    input.close();
                    //add to tables
                    createClientResponseListener(socket, id);
                }
                catch (Exception e)
                {
                    System.err.println("Client conn listener failure: "+e);
                }
            }
        }
    }

    private void createClientResponseListener(Socket socket, int id)
    {
        ClientResponseListener listener = new ClientResponseListener(id, socket);
        Thread thread = new Thread(listener);
        clientSessionThreads.put(id, thread);
    }

    private class ClientResponseListener implements Runnable
    {
        public boolean running;
        public int clientID;
        public Socket socket;
        private ObjectInputStream input;

        public ClientResponseListener (int clientID, Socket socket)
        {
            this.clientID = clientID;
            this.socket = socket;
            try
            {
                input = new ObjectInputStream(socket.getInputStream());
                System.out.println("Created client response listenser for client ID: "+clientID);
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
            switch (data.status) {
                case "LOGIN": 

                    break;
                case "LOGOUT":  
                    break;
                case "SIGN-UP":  
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
                default:
                    break;
            }
        }
    }

    public static void main(String[] args)
    {
        Server server = new Server();
        server.startup();
    }
}
