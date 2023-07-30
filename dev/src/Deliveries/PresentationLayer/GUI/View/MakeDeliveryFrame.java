package Deliveries.PresentationLayer.GUI.View;

import Deliveries.BusinessLayer.DeliveryForm;
import Deliveries.PresentationLayer.GUI.Model.MakeDeliveryModel;

public class MakeDeliveryFrame extends AbstractFrame {
    public MakeDeliveryFrame(DeliveryForm deliveryForm) {
        super(4, new MakeDeliveryModel(deliveryForm));
        // TODO: display the delivery form
        addButton("Return to main menu");
        addButton("Start delivery");
    }
}
