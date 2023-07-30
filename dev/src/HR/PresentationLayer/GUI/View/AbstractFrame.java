package HR.PresentationLayer.GUI.View;

import HR.PresentationLayer.GUI.Model.AbstractModel;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractFrame extends JFrame {
    protected final JPanel buttonsPanel;
    protected final JPanel infoPanel;
    protected final JPanel contentPanel;
    protected AbstractModel relatedModel;

    public AbstractFrame(int numButtons, AbstractModel relatedModel) {
        this.relatedModel = relatedModel;
        relatedModel.addFrame(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HR System");
        setLayout(new BorderLayout());

        // Create buttons panel
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        // Create info panel
        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());

        // Create content panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(buttonsPanel, BorderLayout.NORTH);
        contentPanel.add(infoPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        getContentPane().setPreferredSize(new Dimension(800, 600));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected void addButton(String buttonText) {
        JButton button = new JButton(buttonText);
        buttonsPanel.add(button);
        button.setForeground(Color.DARK_GRAY);
        button.setBackground(Color.LIGHT_GRAY);
        button.setFont(new Font("Century Gothic", Font.BOLD, 12));
        // Button action listener
        button.addActionListener(relatedModel);
    }

    public void displayError(String errorMessage) {
        String[] lines = errorMessage.split("\n");

        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));

        for (String line : lines) {
            JLabel errorLabel = new JLabel(line);
            errorLabel.setForeground(Color.RED);
            errorPanel.add(errorLabel);
        }

        JScrollPane scrollPane = new JScrollPane(errorPanel);

        infoPanel.removeAll();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.add(scrollPane, BorderLayout.CENTER);
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    public void clearError() {
        infoPanel.removeAll();
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    public void displayInfo(String infoMessage) {
        JTextArea infoTextArea = new JTextArea(infoMessage);
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(infoTextArea);

        infoPanel.removeAll();
        infoPanel.add(scrollPane, BorderLayout.CENTER);
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    public void clearInfo() {
        infoPanel.removeAll();
        infoPanel.revalidate();
        infoPanel.repaint();
    }
}
