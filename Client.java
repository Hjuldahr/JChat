import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Client 
{
    private final int SERVER_PORT = 8000;
    private final String SERVER_ADDRESS = "127.0.0.1";
    private final int RECONNECT_INTERVAL = 5000;

    private int sessionID;
    private String password = "1234";
    private Socket socket;
    private ObjectOutputStream input;
    private ObjectOutputStream output;
    private ServerResponseListener serverResponseListener;
    private Thread serverResponseListenerThread;
    
    public Client()
    {
        initialize();
    }

    public void initialize()
    {
        while (socket == null)
        {
            try 
            {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                //get session id from server for use when sending requests
                try (DataInputStream output = new DataInputStream(socket.getInputStream())) {
                    sessionID = output.readInt();
                }

                output = new ObjectOutputStream(socket.getOutputStream());
                serverResponseListener = new ServerResponseListener(socket);
                serverResponseListenerThread = new Thread(serverResponseListener);
                System.out.println("Client connect successful");
            }
            catch (ConnectException ce)
            {
                try 
                {
                    System.err.println("Client connect failed, reattempting in: "+RECONNECT_INTERVAL+"ms"); 
                    Thread.sleep(RECONNECT_INTERVAL);
                    continue;
                }
                catch (InterruptedException ie)
                {
                    continue;
                }
            }
            catch (Exception e)
            {
                System.err.println("Client connect failed, error unresolvable: "+e); 
            }
        }
    }

    public void startup()
    {
        serverResponseListenerThread.run();
    }

    public void requestLogin(String username, String password)
    {
        try 
        {
            String[] contents = new String[]{"user", username, "pass", password};
            output.writeObject(new Data(sessionID, "LOGIN", contents));
        }
        catch (Exception e)
        {
            System.err.println("Client login request failed: "+e);
        }
    }
    
    private class ServerResponseListener implements Runnable
    {
        public boolean running;
        public Socket socket;
        private ObjectInputStream input;

        public ServerResponseListener (Socket socket)
        {
            this.socket = socket;
            try
            {
                input = new ObjectInputStream(socket.getInputStream());
            }
            catch(Exception e)
            {
                System.out.println("Failed to creat client response listenser: "+e);
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
                }
                catch (Exception e)
                {
                    System.err.println("Client response listener failure: "+e);
                }
            }
        }
    }

    public static void main(String[] args)
    {
        //TODO loop for connecting if fails, with attempt delay to prevent network overload
        
        Client client = new Client();
        client.startup();
        client.requestLogin("User1", "1234");
    }
}
