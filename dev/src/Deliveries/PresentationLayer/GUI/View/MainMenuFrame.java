package Deliveries.PresentationLayer.GUI.View;

import Deliveries.BusinessLayer.Generators.SiteGenerator;
import Deliveries.BusinessLayer.Site;
import Deliveries.PresentationLayer.GUI.Model.MainMenuModel;

import java.util.List;

public class MainMenuFrame extends AbstractFrame {
    public MainMenuFrame() {
        super(4, new MainMenuModel());
        addButton("Add a delivery stop");
        addButton("Remove a delivery stop");
        addButton("Execute a delivery");
        addButton("Exit");
  }

}