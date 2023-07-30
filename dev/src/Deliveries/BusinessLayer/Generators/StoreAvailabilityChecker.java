package Deliveries.BusinessLayer.Generators;

import Deliveries.BusinessLayer.DeliveryStop;
import HR.BusinessLayer.ShiftController;
import HR_Deliveries_Interface.HRIntegrator;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class StoreAvailabilityChecker {
    public static final int SHIFT_LENGTH_HOURS = 8;
    private final HRIntegrator hrManager;
    private final Timestamp firstCheckTime;
    private final Timestamp endWindow;

    public StoreAvailabilityChecker() {
        this.hrManager = ShiftController.getInstance();
        firstCheckTime = new Timestamp(2023-1900, 6, 1, 8, 1, 0, 0);
        endWindow = new Timestamp(2023-1900, 6, 9, 8, 1, 0, 0);
        // end window is 1 month after first check time
    }
//    private Timestamp getStoreAvailability(String store) {
//        Timestamp arrivalTime = new Timestamp(firstCheckTime.getTime());
//        while (!hrManager.checkStoreAvailability(store, arrivalTime) && arrivalTime.before(endWindow)) {
//            arrivalTime.setTime(arrivalTime.getTime() + SHIFT_LENGTH_HOURS * HOUR_LENGTH);
//        }
//        return arrivalTime;
//    }
//
//    public boolean checkStoreAvailability(String store){
//        Timestamp arrivalTime = getStoreAvailability(store);
//        return hrManager.checkStoreAvailability(store, arrivalTime);
//    }

    public Timestamp checkStoreAvailability(List<DeliveryStop> stops) {
        Timestamp suggestedArrivalTime = new Timestamp(firstCheckTime.getTime());
        for (DeliveryStop stop : stops) {
            while (!hrManager.checkStoreAvailability(stop.getDestination().getName(), suggestedArrivalTime) &&
                    suggestedArrivalTime.before(endWindow)) {
                suggestedArrivalTime = Timestamp.from(suggestedArrivalTime.toInstant().plus(SHIFT_LENGTH_HOURS, ChronoUnit.HOURS));
            }
            if (!hrManager.checkStoreAvailability(stop.getDestination().getName(), suggestedArrivalTime)) {
                return null; // If we've reached the end of the window and still can't find a time, return null
            }
        }
        return suggestedArrivalTime;
    }



}
