package HR.PresentationLayer.GUI.Model;

import HR.PresentationLayer.GUI.View.ActionFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class InformationModel extends AbstractModel{
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton sourceButton) {
            if (sourceButton.getText().equals("Back")) {
                relatedFrame.clearError();
                relatedFrame.clearInfo();
                relatedFrame.dispose();
            }
        }
    }
}
