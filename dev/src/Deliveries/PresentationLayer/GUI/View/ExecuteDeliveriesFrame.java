package Deliveries.PresentationLayer.GUI.View;

import Deliveries.BusinessLayer.DeliveryForm;
import Deliveries.PresentationLayer.GUI.Model.ExecuteDeliveriesModel;

import java.util.Set;

public class ExecuteDeliveriesFrame extends AbstractFrame {

    public ExecuteDeliveriesFrame(Set<DeliveryForm> deliveryForms) {
        super(deliveryForms.size() + 4, new ExecuteDeliveriesModel());

        addButton("Return to main menu");

        for (DeliveryForm deliveryForm : deliveryForms) {
                addButton(deliveryForm.toString());
        }

    }

}
