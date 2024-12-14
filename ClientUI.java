

public class ClientUI extends JFrame
{
    public ClientUI()
    {
        add(new LoginPanel());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class LoginPanel extends JPanel
    {
        
    }
}