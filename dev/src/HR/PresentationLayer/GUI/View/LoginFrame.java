package HR.PresentationLayer.GUI.View;

import HR.PresentationLayer.GUI.Model.LoginModel;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends AbstractFrame {
    protected final JTextField usernameField;
    protected final JPasswordField passwordField;

    public LoginFrame() {
        super(1, new LoginModel());
        setTitle("Login");
        setResizable(false);

        // Create username label and text field
        JLabel usernameLabel = new JLabel("ID:");
        usernameField = new JTextField(20);
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        // Create password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // Create main content panel with BoxLayout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setContentPane(contentPanel);

        // Add the username panel to the content panel
        contentPanel.add(usernamePanel);

        // Add some vertical spacing between panels
        contentPanel.add(Box.createVerticalStrut(10));

        // Add the password panel to the content panel
        contentPanel.add(passwordPanel);

        // Add some vertical spacing between panels
        contentPanel.add(Box.createVerticalStrut(10));

        // Add the buttons panel to the content panel
        contentPanel.add(buttonsPanel);

        // Create login button
        addButton("Login");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
