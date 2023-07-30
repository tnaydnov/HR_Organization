package HR.PresentationLayer.GUI.View;

import HR.PresentationLayer.GUI.Model.ActionModel;

import javax.swing.*;
import java.awt.*;

public class ActionFrame extends AbstractFrame {
    private JTextField[] fieldsText;

    public ActionFrame(String title, int fieldsNum, String[] fields) {
        super(2, new ActionModel());
        fieldsText = new JTextField[fieldsNum];
        setTitle(title);
        setResizable(false);

        JPanel fieldsPanel = new JPanel(new GridLayout(fieldsNum, 2)); // Container panel for the fields

        for (int i = 0; i < fieldsNum; i++) {
            JLabel label = new JLabel(fields[i] + ": ");
            JTextField field = new JTextField(20);
            fieldsText[i] = field;
            fieldsPanel.add(label);
            fieldsPanel.add(field);
        }

        add(fieldsPanel, BorderLayout.NORTH); // Add the fields panel to the top of the frame

        addButton("Done");
        addButton("Back");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String get_field(int index) {
        return fieldsText[index].getText();
    }

    public int get_num_fields() {
        return fieldsText.length;
    }

    public String get_title() {
        return getTitle();
    }
}
