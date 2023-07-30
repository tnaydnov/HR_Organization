package Deliveries.BusinessLayer.Enums_and_Interfaces;

import Deliveries.BusinessLayer.DeliveryStop;

import java.util.List;

public interface TripReplanner {
    public DeliveryStop removeStop(List<DeliveryStop> stops);
//    public DeliveryStop removeItems(List<DeliveryStop> stops);
    public TripReplanAction chooseAction(List<DeliveryStop> stops);
}
