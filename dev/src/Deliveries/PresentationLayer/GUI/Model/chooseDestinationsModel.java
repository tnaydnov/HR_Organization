package Deliveries.PresentationLayer.GUI.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class chooseDestinationsModel extends AbstractModel {
    private List<String> destinations;
    private final AddDeliveryModel addDeliveryModel;
    public chooseDestinationsModel(AddDeliveryModel addDeliveryModel) {
        this.addDeliveryModel = addDeliveryModel;
        this.destinations = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Return to main menu")) {
            ReturnToMainMenuClicked();
        }
        else if (e.getActionCommand().equals("Next")){
            handleNextClicked();
            relatedFrame.dispose();
            provideDestinations();
        }
}

    private void provideDestinations() {
        addDeliveryModel.receiveDestinations(destinations);
    }

    private void handleNextClicked() {
        for (Component component : relatedFrame.getComponents()) {
            if (component instanceof JCheckBox checkBox) {
                if (checkBox.isSelected()) {
                    destinations.add(checkBox.getText());
                }
            }
        }
    }
}
