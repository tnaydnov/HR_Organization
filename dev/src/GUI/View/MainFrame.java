package GUI.View;

import GUI.Model.AbstractModel;
import GUI.Model.MainModel;

public class MainFrame extends AbstractFrame{
    public MainFrame() {
        super(1, new MainModel());
        addButton("HR System");
        addButton("Deliveries System");
    }
}
