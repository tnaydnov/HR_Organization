package Deliveries.BusinessLayer.Enums_and_Interfaces;

import Deliveries.BusinessLayer.DeliveryForm;

public interface WeightMeasurer {
    public int measureWeight(DeliveryForm form, String currentStop);
}
