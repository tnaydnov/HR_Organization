package HR.PresentationLayer.GUI.View;

import HR.PresentationLayer.GUI.Model.AbstractModel;
import HR.PresentationLayer.GUI.Model.InformationModel;

public class InformationFrame extends AbstractFrame{
    public InformationFrame(String info) {
        super(0, new InformationModel());
        displayInfo(info);
        addButton("Back");
    }
}
