import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupPage extends JFrame {

    private Client client;
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JButton signupButton;

    public SignupPage(Client client) {
        this.client = client;

        setTitle("Sign Up");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        signupButton = new JButton("Sign Up");

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle signup logic here
                //JOptionPane.showMessageDialog(null, "Signed up successfully!");
                if (signup(usernameField.getText(), emailField.getText(), passwordField.getPassword()))
                    new HomePage(client).setVisible(true);
                else
                    new WelcomePage(client).setVisible(true);
                dispose();
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(signupButton);

        add(panel);
    }
    private boolean signup(String user, String email, char[] password)
    {
        try
        {
            client.sendSignup(user, email, PasswodHasher.HashPassword(password));
            return true;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
