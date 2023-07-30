package Deliveries.PresentationLayer.GUI.View;

import Deliveries.PresentationLayer.GUI.Model.AddDeliveryModel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class AddItemsFrame extends AbstractFrame {
    private Map<String, Integer> itemsMap;
    private AddDeliveryModel relatedModel;
    public AddItemsFrame(int numButtons, AddDeliveryModel relatedModel) {
        super(numButtons, relatedModel);
        itemsMap = new HashMap<>();
        setTitle("Add Items");

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        JTextField itemNameField = new JTextField();
        JFormattedTextField amountField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        amountField.setValue(0);
        itemNameField.setSize(400, 200);
        amountField.setSize(400, 200);
        inputPanel.setSize(800, 400);

        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemNameField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        buttonsPanel.add(inputPanel, BorderLayout.NORTH);

        JButton addItemButton = new JButton("Add Item");
        addItemButton.setSize(800, 30);
        //place button south
        addItemButton.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        addItemButton.setForeground(Color.darkGray);
        addItemButton.setBackground(Color.lightGray);
        addItemButton.addActionListener(e -> {
            String itemName = itemNameField.getText();
            int amount = Integer.parseInt(amountField.getValue().toString());
            if (!itemName.isEmpty() && amount > 0) {
                itemsMap.put(itemName, amount);
                itemNameField.setText("");
                amountField.setValue(0);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid item name and amount.");
            }
        });

        JButton doneButton = new JButton("Done");
        doneButton.setSize(800, 30);
        doneButton.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        doneButton.setForeground(Color.darkGray);
        doneButton.setBackground(Color.lightGray);
        doneButton.addActionListener(e -> relatedModel.addItems(itemsMap));

        buttonsPanel.add(addItemButton, BorderLayout.SOUTH);
        buttonsPanel.add(doneButton, BorderLayout.SOUTH);

    }

}
