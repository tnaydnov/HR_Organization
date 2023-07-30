package Deliveries.DeliveriesTests;

import Deliveries.BusinessLayer.Driver;
import Deliveries.BusinessLayer.DriverController;
import Deliveries.BusinessLayer.Enums_and_Interfaces.DeliveryException;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import Deliveries.BusinessLayer.Truck;
import HR_Deliveries_Interface.HRIntegrator;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DriverControllerTest {
    public static final int LAST_TEST_INDEX = 4; // TODO: change this as more tests are added
    private DriverController driverController;
    @Mock
    Truck truck1;
    @Mock
    HRIntegrator hrManager;
    Timestamp startTime;
    Timestamp endTime;


    @BeforeEach
    void setUp() {
        driverController = DriverController.getInstance();
        driverController.setTestEnvironment();
        // driverController.generateFleet(20);
        truck1 = mock(Truck.class);
        hrManager = mock(HRIntegrator.class);
        try {driverController.setHrManager(hrManager);} catch (Exception ignored) {}
        startTime = new Timestamp(120, 1, 1, 8, 0, 0, 0);
        endTime = new Timestamp(120, 1, 1, 16, 0, 0, 0);
    }

    @Test @Order(1)
    void pickDriverSuccess() {
        when(truck1.getType()).thenReturn(TruckType.REGULAR);
//        when(truck2.getType()).thenReturn(TruckType.Refrigerated);
        when(truck1.getMaxWeightTons()).thenReturn(10);
//        when(truck2.getMaxWeightTons()).thenReturn(20);
//        when(truck3.getMaxWeightTons()).thenReturn(30);
// driverController.generateFleet(1);
        List<String> driverIds = driverController.getDriverIds();
        when(hrManager.getAvailableDrivers(startTime, endTime)).thenReturn(driverIds);
        when(hrManager.assignDrivers(any(), any(), any())).thenReturn(true);
        try {
            Driver driver = driverController.pickDriver(truck1, startTime, endTime);
            assertTrue(driver.isLicensed(truck1));
        } catch (DeliveryException e) {
            fail();
        }
    }

    @Test @Order(2)
    void pickDriverFailWeight() {
        when(truck1.getType()).thenReturn(TruckType.REGULAR);
//        when(truck2.getType()).thenReturn(TruckType.Refrigerated);
        when(truck1.getMaxWeightTons()).thenReturn(40);
//        when(truck2.getMaxWeightTons()).thenReturn(20);
//        when(truck3.getMaxWeightTons()).thenReturn(30);
        List<String> driverIds = driverController.getDriverIds();
        when(hrManager.getAvailableDrivers(startTime, endTime)).thenReturn(driverIds);

        assertThrows(DeliveryException.class, () -> driverController.pickDriver(truck1, startTime, endTime));

    }

    @Test @Order(3)
    void pickDriverFailHRAvailability() {
        when(truck1.getType()).thenReturn(TruckType.REGULAR);
//        when(truck2.getType()).thenReturn(TruckType.Refrigerated);
        when(truck1.getMaxWeightTons()).thenReturn(10);
//        when(truck2.getMaxWeightTons()).thenReturn(20);
//        when(truck3.getMaxWeightTons()).thenReturn(30);
        List<String> driverIds = new ArrayList<>();
        when(hrManager.getAvailableDrivers(startTime, endTime)).thenReturn(driverIds);

        assertThrows(DeliveryException.class, () -> driverController.pickDriver(truck1, startTime, endTime));

    }

    @Test @Order(LAST_TEST_INDEX)
    void pickDriverFailDriverAvailability() {
        when(truck1.getType()).thenReturn(TruckType.REGULAR);
        when(truck1.getMaxWeightTons()).thenReturn(10);
        List<String> driverIds = driverController.getDriverIds();
        when(hrManager.getAvailableDrivers(startTime, endTime)).thenReturn(driverIds);
        assertThrows(DeliveryException.class, () -> pickAllDriversLoop(driverIds));
        // Check if all drivers are unavailable after we picked them all
    }

    private void pickAllDriversLoop(List<String> driverIds) throws DeliveryException {
        for (int i = 0; i < driverIds.size() + 1; i++) {
            driverController.pickDriver(truck1, startTime, endTime);
        }
    }


}