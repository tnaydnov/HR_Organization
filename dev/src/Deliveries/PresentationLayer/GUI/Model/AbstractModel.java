package Deliveries.PresentationLayer.GUI.Model;

import Deliveries.BusinessLayer.Site;
import Deliveries.PresentationLayer.GUI.View.AbstractFrame;
import Deliveries.PresentationLayer.GUI.View.MainMenuFrame;

import java.util.List;

public abstract class AbstractModel implements java.awt.event.ActionListener {
    protected AbstractFrame relatedFrame;
    public void addFrame(AbstractFrame frame){
        relatedFrame = frame;
    }

    public void ReturnToMainMenuClicked() {
        relatedFrame.dispose();
        new MainMenuFrame();
    }

}
