package GUI.Model;

import GUI.View.AbstractFrame;
import GUI.View.MainFrame;

public abstract class AbstractModel implements java.awt.event.ActionListener{
    protected AbstractFrame relatedFrame;
    public void addFrame(AbstractFrame frame){
        relatedFrame = frame;
    }

    public void ReturnToMainMenuClicked() {
        relatedFrame.dispose();
        new MainFrame();
    }
}
