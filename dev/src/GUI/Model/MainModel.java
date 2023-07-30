package GUI.Model;

import java.awt.event.ActionEvent;

public class MainModel extends AbstractModel{
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("HR System")) {
            relatedFrame.clearError();
            relatedFrame.clearInfo();
            relatedFrame.dispose();
            new HR.PresentationLayer.GUI.View.LoginFrame();
        }
        else if (e.getActionCommand().equals("Deliveries System")) {
            relatedFrame.clearError();
            relatedFrame.clearInfo();
            relatedFrame.dispose();
            new Deliveries.PresentationLayer.GUI.View.MainMenuFrame();
        }
    }
}
