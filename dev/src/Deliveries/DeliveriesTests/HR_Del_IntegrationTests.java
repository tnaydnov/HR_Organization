package Deliveries.DeliveriesTests;

import Deliveries.BusinessLayer.*;
import Deliveries.BusinessLayer.Enums_and_Interfaces.DeliveryException;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import Deliveries.DataAccessLayer.DriverDAO;
import HR.BusinessLayer.ShiftController;
import HR_Deliveries_Interface.HRIntegrator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HR_Del_IntegrationTests {
    DriverDAO driverDAO;

    @Test
    void pickDriverIntegratedSuccess() {
        DriverController driverController = DriverController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 6, 4, 10, 0, 0, 0);
        Timestamp timestamp2 = new Timestamp(2023-1900, 6, 4, 12, 0, 0, 0);
        Truck truck = mock(Truck.class);
        when(truck.getType()).thenReturn(TruckType.REGULAR);
        when(truck.getMaxWeightTons()).thenReturn( 1);

        try {
           Driver driver = driverController.pickDriver(truck, timestamp, timestamp2);
           assertTrue(driver.isLicensed(truck));
        } catch (DeliveryException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void pickDriverIntegratedAvailabilityFailures() {
        DriverController driverController = DriverController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 1, 4, 10, 0, 0, 0);
        Timestamp timestamp2 = new Timestamp(2023-1900, 1, 4, 12, 0, 0, 0);
        Truck truck = mock(Truck.class);
        when(truck.getType()).thenReturn(TruckType.REGULAR);
        when(truck.getMaxWeightTons()).thenReturn( 1);

        assertThrows(DeliveryException.class, () -> driverController.pickDriver(truck, timestamp, timestamp2));
    }

    @Test
    void pickDriverIntegratedShiftLengthFailures() {
        DriverController driverController = DriverController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 6, 4, 10, 0, 0, 0);
        Timestamp timestamp2 = new Timestamp(2023-1900, 6, 4, 22, 0, 0, 0);
        Truck truck = mock(Truck.class);
        when(truck.getType()).thenReturn(TruckType.REGULAR);
        when(truck.getMaxWeightTons()).thenReturn( 1);
        assertThrows(DeliveryException.class, () -> driverController.pickDriver(truck, timestamp, timestamp2));
    }

    @Test
    void pickDriverIntegratedNegativeTime() {
        DriverController driverController = DriverController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 6, 4, 10, 0, 0, 0);
        Timestamp timestamp2 = new Timestamp(2023-1900, 6, 4, 22, 0, 0, 0);
        Truck truck = mock(Truck.class);
        when(truck.getType()).thenReturn(TruckType.REGULAR);
        when(truck.getMaxWeightTons()).thenReturn( 1);
        assertThrows(DeliveryException.class, () -> driverController.pickDriver(truck, timestamp2, timestamp));
        // trying to pick a driver with start time after end time
    }


    @Test
    void checkStoreAvailabilitySuccess() {
        HRIntegrator shiftController = ShiftController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 6, 2, 10, 0, 0, 0);
        assertFalse(shiftController.checkStoreAvailability("Ashdod", timestamp));
    }

    @Test
    void checkStoreAvailabilityFailure() {
        HRIntegrator shiftController = ShiftController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 8, 3, 16, 0, 0, 0);
        // store is closed on Saturdays
        assertFalse(shiftController.checkStoreAvailability("Tel Aviv", timestamp));
    }


    @Test
    void assignDriverSuccess() {
        DriverController driverController = DriverController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 6, 7, 16, 0, 0, 0);
        Timestamp timestamp2 = new Timestamp(2023-1900, 6, 7, 18, 0, 0, 0);
        Truck truck = mock(Truck.class);
        when(truck.getType()).thenReturn(TruckType.REGULAR);
        when(truck.getMaxWeightTons()).thenReturn( 1);
        String driverId;
        try {
            driverId = (driverController.pickDriver(truck, timestamp, timestamp2)).getId();
        } catch (DeliveryException e) {
            throw new RuntimeException(e);
        }
        HRIntegrator shiftController = ShiftController.getInstance();
        assertTrue(shiftController.assignDrivers(driverId, timestamp, timestamp2));
    }

    @Test
    void assignDriverFalse() {
        DriverController driverController = DriverController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 6, 7, 14, 30, 0, 0);
        Timestamp timestamp2 = new Timestamp(2023-1900, 6, 7, 18, 0, 0, 0);
        Timestamp timestamp3 = new Timestamp(2023-1900, 6, 7, 23, 0, 0, 0);
        Truck truck = mock(Truck.class);
        when(truck.getType()).thenReturn(TruckType.REGULAR);
        when(truck.getMaxWeightTons()).thenReturn( 1);
        String driverId;
        try {
            driverId = (driverController.pickDriver(truck, timestamp, timestamp2)).getId();
        } catch (DeliveryException e) {
            throw new RuntimeException(e);
        }
        HRIntegrator shiftController = ShiftController.getInstance();
        assertFalse(shiftController.assignDrivers(driverId, timestamp, timestamp3));
        // Assigning a driver to a shift that is too long (8+ hours) should fail
    }

    @Test
    void assignDriverFail() {
        DriverController driverController = DriverController.getInstance();
        Timestamp timestamp = new Timestamp(2023-1900, 6, 7, 14, 30, 0, 0);
        Timestamp timestamp2 = new Timestamp(2023-1900, 6, 7, 18, 0, 0, 0);
        Timestamp timestamp3 = new Timestamp(2023-1900, 6, 7, 23, 0, 0, 0);
        Truck truck = mock(Truck.class);
        when(truck.getType()).thenReturn(TruckType.REGULAR);
        when(truck.getMaxWeightTons()).thenReturn( 1);
        String driverId;
        try {
            driverId = (driverController.pickDriver(truck, timestamp, timestamp2)).getId();
        } catch (DeliveryException e) {
            throw new RuntimeException(e);
        }
        HRIntegrator shiftController = ShiftController.getInstance();
        assertFalse(shiftController.assignDrivers(driverId, timestamp3, timestamp));
        // Assigning a driver with start time after end time should fail
    }

    @BeforeEach
    void setUp() {
        driverDAO = new DriverDAO();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testLoadData() {
    }
}