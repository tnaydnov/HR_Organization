package HR.PresentationLayer.GUI.Model;

import HR.PresentationLayer.GUI.View.AbstractFrame;
import HR.PresentationLayer.GUI.View.LoginFrame;
import HR.PresentationLayer.GUI.View.MainMenuFrame;
import HR.ServiceLayer.EmployeeService;

public abstract class AbstractModel implements java.awt.event.ActionListener{
    protected AbstractFrame relatedFrame;
    protected static EmployeeService employeeService = new EmployeeService();
    public void addFrame(AbstractFrame frame){
        relatedFrame = frame;
    }

    public void ReturnToMainMenuClicked() {
        relatedFrame.dispose();
        new LoginFrame();
    }
}
