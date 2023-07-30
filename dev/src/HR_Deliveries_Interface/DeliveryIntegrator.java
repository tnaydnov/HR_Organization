package HR_Deliveries_Interface;

import Deliveries.BusinessLayer.DeliveryStop;
import java.sql.Timestamp;
import java.util.Set;

public interface DeliveryIntegrator {
    Set<DeliveryStop> getDeliveryByArrivalTime(Timestamp startTime, Timestamp finishTime, String store);
}
