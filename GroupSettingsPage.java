import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GroupSettingsPage extends JFrame {

    private JButton updateSettingsButton;
    private String name;

    public GroupSettingsPage(String name) {
        this.name = name;
        setTitle("Group Moderator Settings");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        updateSettingsButton = new JButton("Update Settings");

        updateSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle updating settings
                JOptionPane.showMessageDialog(null, "Settings updated.");
            }
        });

        panel.add(updateSettingsButton);

        add(panel);
    }
}
