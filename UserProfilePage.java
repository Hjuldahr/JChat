import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfilePage extends JFrame {

    private Client client;
    private boolean isOwner; // Flag to distinguish between the profile owner and others
    private JTextField nameField, emailField;
    private JButton saveButton, backButton, homeButton, logoutButton;
    private JLabel nameLabel, emailLabel;

    public UserProfilePage(Client client, boolean isOwner) {
        this.client = client;
        this.isOwner = isOwner;

        setTitle("User Profile");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program on window close
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Panel for the profile form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2));

        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");

        if (isOwner) {
            // Editable fields for the profile owner
            nameField = new JTextField("John Doe"); // Example default value
            emailField = new JTextField("john.doe@example.com"); // Example default value
        } else {
            // Non-editable fields for others viewing the profile
            nameField = new JTextField("John Doe");
            emailField = new JTextField("john.doe@example.com");

            nameField.setEditable(false);
            emailField.setEditable(false);
        }

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        // Panel for the action buttons (back, save, home, logout)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Action buttons for the profile owner
        if (isOwner) {
            saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Logic to save the updated profile information
                    String updatedName = nameField.getText();
                    String updatedEmail = emailField.getText();
                    JOptionPane.showMessageDialog(null, "Profile updated: " + updatedName + ", " + updatedEmail);
                }
            });
            buttonPanel.add(saveButton);
        }

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to the previous screen (HomePage)
                new HomePage(client).setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(backButton);

        if (isOwner) {
            homeButton = new JButton("Home");
            homeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Navigate to the HomePage
                    new HomePage(client).setVisible(true);
                    dispose();
                }
            });
            buttonPanel.add(homeButton);
        }

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle logout logic here
                JOptionPane.showMessageDialog(null, "Logged out successfully!");
                new WelcomePage(client).setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(logoutButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Simulating profile owner view (pass true for profile owner, false for others)
                new UserProfilePage(null, true).setVisible(true);
            }
        });
    }
}

