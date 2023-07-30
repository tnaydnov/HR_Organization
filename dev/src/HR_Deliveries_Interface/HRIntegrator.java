package HR_Deliveries_Interface;

import java.sql.Timestamp;
import java.util.List;

public interface HRIntegrator {
    boolean checkStoreAvailability(String store, Timestamp arrivalTime);
    List<String> getAvailableDrivers(Timestamp startTime, Timestamp endTime);
    boolean assignDrivers(String driverId, Timestamp startTime, Timestamp endTime);

}
