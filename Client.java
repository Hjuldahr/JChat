import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JFrame;

public class Client 
{
    private final int SERVER_PORT = 8000;
    private final String SERVER_ADDRESS = "127.0.0.1";
    private final int RECONNECT_INTERVAL = 5000;

    private int sessionID; //disposable and unique to the session
    private String userName;
    private int userID; //unique to the profile and set on login/signup
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ServerResponseHandler serverResponseHandler;
    private Thread serverResponseHandlerThread;
    private JFrame page;
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }

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
                serverResponseHandler = new ServerResponseHandler(socket);
                serverResponseHandlerThread = new Thread(serverResponseHandler);
                page = new WelcomePage(this);
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
        serverResponseHandlerThread.run();
    }

    public boolean sendLogin(String username, String password)
    {
        try 
        {
            //to server
            this.userName = username;
            String[] contents = new String[]{"uname", username, "pass", password};
            output.writeObject(new Data(sessionID, "LOGIN", contents));
            //from server
            Data data = (Data) input.readObject();
            if (data.getStatus().equals("SUCCESS"))
            {
                userID = Integer.parseInt(data.getContent("uid"));
                return true;
            }
        }
        catch (Exception e)
        {
            System.err.println("Client login request failed: "+e);
        }
        return false;
    }

    public boolean sendSignup(String username, String email, String password)
    {
        try 
        {
            this.userName = username;
            String[] contents = new String[]{"uname", username, "email", email, "pass", password};
            output.writeObject(new Data(sessionID, "SIGNUP", contents));
            //from server
            Data data = (Data) input.readObject();
            if (data.getStatus().equals("SUCCESS"))
            {
                userID = Integer.parseInt(data.getContent("uid"));
                return true;
            }
        }
        catch (Exception e)
        {
            System.err.println("Client signup request failed: "+e);
        }
        return false;
    }
    
    private class ServerResponseHandler implements Runnable
    {
        public boolean running;
        public Socket socket;
        private ObjectInputStream input;

        public ServerResponseHandler (Socket socket)
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
                    switch(data.getStatus())
                    {
                        case "LOGIN-SUCCESS": 
                            userID = Integer.parseInt(data.getContent("userID"));
                            break;
                    }
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
        Client client = new Client();
        client.startup();
    }
}
