package HR_Deliveries_Interface;
public interface DriverSaver {
    boolean AddDriverToSystem(String name, String id, String phone, int maxWeight, boolean regularAllowed,
                              boolean refrigeratedAllowed);
    boolean DeleteDriverFromSystem(String id);
}
