package Deliveries.PresentationLayer.GUI.Model;

import Deliveries.BusinessLayer.DeliveryForm;
import Deliveries.BusinessLayer.DeliveryFormsController;
import Deliveries.PresentationLayer.GUI.View.MainMenuFrame;
import Deliveries.PresentationLayer.GUI.View.MakeDeliveryFrame;

import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecuteDeliveriesModel extends AbstractModel {
    DeliveryFormsController deliveryFormsController;
    public ExecuteDeliveriesModel() {
        deliveryFormsController = DeliveryFormsController.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Return to main menu")) {
            ReturnToMainMenuClicked();
        }
        else {
            String buttonText = e.getActionCommand();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(buttonText);
            if (matcher.find()) {
                int formId = Integer.parseInt(matcher.group());
                DeliveryForm form = deliveryFormsController.getDeliveryForm(formId);
                relatedFrame.dispose();
                new MakeDeliveryFrame(form);
            }
            else {
                relatedFrame.displayError("Couldn't find form ID!");
            }

        }

    }


}
