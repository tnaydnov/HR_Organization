package Deliveries.PresentationLayer.GUI.View;

import Deliveries.BusinessLayer.Site;
import Deliveries.PresentationLayer.GUI.Model.AddDeliveryModel;
import Deliveries.PresentationLayer.GUI.Model.chooseDestinationsModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChooseDestinationsFrame extends AbstractFrame {
    public ChooseDestinationsFrame(List<Site> siteList, AddDeliveryModel addDeliveryModel) {
        super(siteList.size()+4, new chooseDestinationsModel(addDeliveryModel));
        addButton("Return to main menu");
        for (Site site : siteList) {
            JCheckBox checkBox = new JCheckBox(site.getName());
            checkBox.setForeground(Color.darkGray);
            checkBox.setBackground(Color.lightGray);
            checkBox.setBorder(BorderFactory.createLineBorder(Color.black));
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);//might cancel this


            buttonsPanel.add(checkBox);
        }
        //add s[ace between the checkboxes and the buttons
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addButton("Next");
    }
}
