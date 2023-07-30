package GUI.View;

import GUI.Model.AbstractModel;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractFrame extends JFrame {
    protected final JPanel buttonsPanel;
    protected final JPanel errorPanel;
    protected final JPanel infoPanel;
    protected AbstractModel relatedModel;


    public AbstractFrame(int numButtons, AbstractModel relatedModel) {
        this.relatedModel = relatedModel;
        relatedModel.addFrame(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Deliveries System");
        setLayout(new BorderLayout());

        // Create buttons panel
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(numButtons, 1));
        JScrollPane buttonsScrollPane = new JScrollPane(buttonsPanel);
        add(buttonsScrollPane, BorderLayout.CENTER);
        //add(buttonsPanel, BorderLayout.CENTER);

        // Create error panel
        errorPanel = new JPanel();
        errorPanel.setBorder(BorderFactory.createTitledBorder("Error Panel"));
        errorPanel.setPreferredSize(new Dimension(800, 90));
        add(errorPanel, BorderLayout.NORTH);
        // Create info panel
        infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information Panel"));
        infoPanel.setPreferredSize(new Dimension(800, 120));
        add(infoPanel, BorderLayout.SOUTH);


        getContentPane().setPreferredSize(new Dimension(800, 600)); // Set the width and height as desired

        pack();
        setVisible(true);
    }

    protected void addButton(String buttonText) {
        JButton button = new JButton(buttonText);
        buttonsPanel.add(button);
        if(buttonText.equals("Return to main menu")) {
            //add space between the buttons
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            button.setSize(800, 40);
        }
        if(buttonText.equals("Next")) {
            //add space between the checkboxes and the buttons
            button.setSize(800, 40);
        }
        button.setForeground(Color.darkGray);
        button.setBackground(Color.lightGray);
        button.setSize(800, 40);
        button.setFont(new Font("Century Gothic", Font.BOLD, 12)); // set the font to Arial, bold, size 16
        // Button action listener
        button.addActionListener(relatedModel);
        button.addActionListener(e -> {
            String buttonText1 = ((JButton) e.getSource()).getText();
            displayInfo(buttonText1 + " clicked");
        });
    }


    public void clearButtonsPanel() {
        buttonsPanel.removeAll();
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    public void displayError(String errorMessage) {
        errorPanel.removeAll();
        JLabel errorLabel = new JLabel(errorMessage);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Century Gothic", Font.BOLD, 18)); // set the font to Arial, bold, size 16
        errorPanel.add(errorLabel);
        errorPanel.revalidate();
        errorPanel.repaint();
    }

    public void clearError() {
        errorPanel.removeAll();
        errorPanel.revalidate();
        errorPanel.repaint();
    }

    public void displayInfo(String infoMessage) {
        infoPanel.removeAll();
        JLabel infoLabel = new JLabel(infoMessage);
        infoLabel.setForeground(Color.darkGray);
        infoLabel.setFont(new Font("Century Gothic", Font.BOLD, 18)); // set the font to Arial, bold, size 16
        infoPanel.add(infoLabel);
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    public void clearInfo() {
        infoPanel.removeAll();
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    public String getItemName() {
        return null;
    }


    public int getItemAmount() {
        return 0;
    }
}
