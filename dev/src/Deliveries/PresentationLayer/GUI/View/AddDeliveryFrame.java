package Deliveries.PresentationLayer.GUI.View;

import Deliveries.BusinessLayer.Generators.SiteGenerator;
import Deliveries.BusinessLayer.Site;
import Deliveries.PresentationLayer.GUI.Model.AddDeliveryModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDeliveryFrame extends AbstractFrame {
    private List<Site> chosenSites;
    private Site origin;
    private JPanel sitesPanel;
    private JPanel destinationsPanel;
    private Map<String, Integer> itemsMap;
    private String truckType; //TODO: turn to enum when handling logic

    public AddDeliveryFrame(List<Site> sitesList) {

        super(sitesList.size() + 3, new AddDeliveryModel(sitesList));
        setTitle("Choose Origin:");
        chosenSites = new ArrayList<>();
        itemsMap = new HashMap<>();
        JPanel mainPanel = new JPanel(new BorderLayout());
        addButton("Return to main menu");
        for (Site site : sitesList) {
            addButton(site.toString());
        }
    }
//
//        setLayout(new BorderLayout());
//        sitesPanel = new JPanel(new GridLayout(0, 1));
//        JScrollPane scrollPanel = new JScrollPane(sitesPanel);
//        mainPanel.add(scrollPanel, BorderLayout.CENTER);
//        mainPanel.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
//        chooseOrigin(mainPanel, scrollPanel);
//
//        add(mainPanel, BorderLayout.CENTER);
//
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        pack();
//        setLocationRelativeTo(null);
//        setVisible(true);

//    private void chooseOrigin(JPanel mainPanel, JScrollPane siteScrollPanel) {
//        JLabel label = new JLabel("Pick an origin");
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//        mainPanel.add(label, BorderLayout.NORTH);
//
//        SiteGenerator siteGenerator = new SiteGenerator();
//        List<Site> sitesList = siteGenerator.getSitesList();
//
//        for (Site site : sitesList) {
//            addButton(site.toString(), sitesPanel, site, new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    handleOriginSelection(site);
//                    mainPanel.remove(label);  // Remove the origin selection label
//                    mainPanel.remove(siteScrollPanel);  // Remove the site scroll panel
//                    mainPanel.revalidate();
//                    mainPanel.repaint();
//                    chooseDestinations(mainPanel);  // Move to destination selection
//                }
//            });
//        }
//
//        mainPanel.add(siteScrollPanel, BorderLayout.CENTER);
//        add(mainPanel, BorderLayout.CENTER);
//    }

//    private void chooseOrigin(JPanel mainPanel, JScrollPane siteScrollPanel) {
//        JLabel label = new JLabel("Pick an origin");
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//        mainPanel.add(label, BorderLayout.NORTH);
//
//        SiteGenerator siteGenerator = new SiteGenerator();
//        List<Site> sitesList = siteGenerator.getSitesList();
//
//        for (Site site : sitesList) {
//            addButton(site.toString(), sitesPanel, site, new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    handleOriginSelection(site);
//                    chooseDestinations(mainPanel);
//                }
//            });
//        }
//
//        mainPanel.add(siteScrollPanel, BorderLayout.CENTER);
//        add(mainPanel, BorderLayout.CENTER);
//    }

    private void chooseDestinations(JPanel mainPanel) {
        JLabel label = new JLabel("Pick destinations");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label, BorderLayout.NORTH);

        SiteGenerator siteGenerator = new SiteGenerator();
        List<Site> sitesList = siteGenerator.getSitesList();

        destinationsPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(destinationsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        for (Site site : sitesList) {
            JCheckBox checkBox = new JCheckBox(site.toString());
            destinationsPanel.add(checkBox);
        }
        //add space before the next button
        mainPanel.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
        JButton finishButton = new JButton("Next");
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedDestinations = new ArrayList<>();
                Component[] components = destinationsPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            selectedDestinations.add(checkBox.getText());
                        }
                    }
                }
                handleDestinationSelection(selectedDestinations);
                mainPanel.remove(scrollPane);
                mainPanel.remove(finishButton);
                askTruckType(mainPanel);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        mainPanel.add(finishButton, BorderLayout.SOUTH);
    }

    private void askTruckType(JPanel mainPanel) {
        JLabel label = new JLabel("Choose truck type:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label, BorderLayout.NORTH);

        JRadioButton regularTruckButton = new JRadioButton("Regular Truck");
        JRadioButton refrigeratedTruckButton = new JRadioButton("Refrigerated Truck");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(regularTruckButton);
        buttonGroup.add(refrigeratedTruckButton);

        JPanel radioButtonPanel = new JPanel(new FlowLayout());
        radioButtonPanel.add(regularTruckButton);
        radioButtonPanel.add(refrigeratedTruckButton);

        mainPanel.add(radioButtonPanel, BorderLayout.CENTER);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (regularTruckButton.isSelected()) {
                    handleTruckTypeSelection("Regular Truck");
                } else if (refrigeratedTruckButton.isSelected()) {
                    handleTruckTypeSelection("Refrigerated Truck");
                }
                mainPanel.remove(label);
                mainPanel.remove(radioButtonPanel);
                mainPanel.remove(nextButton);
                addItemsInput(mainPanel);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        mainPanel.add(nextButton, BorderLayout.SOUTH);
    }

    private void addItemsInput(JPanel mainPanel) {
        JLabel label = new JLabel("Enter item details:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        JTextField itemNameField = new JTextField();
        JFormattedTextField amountField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        amountField.setValue(0);

        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemNameField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                int amount = Integer.parseInt(amountField.getValue().toString());
                if (!itemName.isEmpty() && amount > 0) {
                    itemsMap.put(itemName, amount);
                    itemNameField.setText("");
                    amountField.setValue(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid item name and amount.");
                }
            }
        });

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "Delivery created successfully!\n";
                message += "Origin: " + chosenSites.get(0).getName() + "\n";
                message += "Destinations: ";
                for (int i = 1; i < chosenSites.size(); i++) {
                    message += chosenSites.get(i).getName() + ", ";
                }
                message += "\nTruck Type: " + truckType + "\n";
                message += "Items:\n";
                for (Map.Entry<String, Integer> entry : itemsMap.entrySet()) {
                    message += entry.getKey() + ": " + entry.getValue() + "\n";
                }
                JOptionPane.showMessageDialog(null, message);
                relatedModel.ReturnToMainMenuClicked();
            }
        });


        JButton addAnotherItemButton = new JButton("Add Another Item");
        addAnotherItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                int amount = Integer.parseInt(amountField.getValue().toString());
                if (!itemName.isEmpty() && amount > 0) {
                    itemsMap.put(itemName, amount);
                    itemNameField.setText("");
                    amountField.setValue(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid item name and amount.");
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(doneButton);
        buttonPanel.add(addAnotherItemButton);

        mainPanel.add(addItemButton, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }


    private void addButton(String text, JPanel panel, Site site, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
        chosenSites.add(site);
    }


    private void addButton(String text, JPanel panel, Object constraints, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button, constraints);
    }

    private void handleOriginSelection(Site site) {
        chosenSites.add(site);
    }

    private void handleDestinationSelection(List<String> destinations) {
        SiteGenerator siteGenerator = new SiteGenerator();
        List<Site> sitesList = siteGenerator.getSitesList();

        for (String destination : destinations) {
            for (Site site : sitesList) {
                if (site.toString().equals(destination)) {
                    chosenSites.add(site);
                    //break;
                }
            }
        }
    }

    private void handleTruckTypeSelection(String truckType) {
        this.truckType = truckType;
    }


}











