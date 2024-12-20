import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChannelPage extends JFrame {

    private Client client;
    
    private JTextArea chatWindow;
    private JTextField chatInputField;
    private JButton sendButton, settingsButton;
    private JComboBox<String> memberList;

    public ChannelPage(Client client) {
        this.client = client;

        setTitle("Channel Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Chat window
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        panel.add(new JScrollPane(chatWindow), BorderLayout.CENTER);

        // Bottom panel with input and buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        chatInputField = new JTextField(30);
        sendButton = new JButton("Send");
        settingsButton = new JButton("Channel Settings");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle sending message
                String message = chatInputField.getText();
                chatWindow.append("You: " + message + "\n");
                chatInputField.setText("");
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle channel settings for moderators
                JOptionPane.showMessageDialog(null, "Channel settings page.");
            }
        });

        bottomPanel.add(chatInputField);
        bottomPanel.add(sendButton);
        bottomPanel.add(settingsButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Member list (for moderators)
        String[] members = {"User1", "User2", "User3"};
        memberList = new JComboBox<>(members);
        panel.add(memberList, BorderLayout.EAST);

        add(panel);
    }
}
