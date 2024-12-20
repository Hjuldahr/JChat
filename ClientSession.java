import java.net.Socket;

public class ClientSession 
{
    private int sessionID;
    private int userID;
    private Socket socket;
    private Thread thread;

    public ClientSession(int sessionID, Socket socket, Thread thread)
    {
        this.sessionID = sessionID;
        this.socket = socket;
        this.thread = thread;
    }
    public ClientSession(int sessionID)
    {
        this(sessionID, null, null);
    }
    public int getSessionID()
    {
        return sessionID;
    }
    public int getUserID()
    {
        return userID;
    }
    public void setUserID(int userID)
    {
        this.userID = userID;
    }
    public Socket getSocket()
    {
        return socket;
    }
    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }
    public Thread getThread()
    {
        return thread;
    }
    public void setThread(Thread thread)
    {
        this.thread = thread;
    }
}   
