package Deliveries.DeliveriesTests;

import Deliveries.BusinessLayer.Enums_and_Interfaces.DeliveryException;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import Deliveries.BusinessLayer.Truck;
import Deliveries.BusinessLayer.TruckController;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TruckControllerTest {
    TruckController truckController;


    @BeforeEach
    void setUp() {
        truckController = TruckController.getInstance();
    }

    @Test  @Order(1)
    void pickTruckSuccess() {
        try {
            Truck truck = truckController.pickTruck(TruckType.REGULAR, 10);
            assertEquals(truck.getType(), TruckType.REGULAR);
            assertTrue(truck.getMaxWeightTons() >= 10);
        } catch (DeliveryException e) {
            throw new RuntimeException(e);
        }
    }

    @Test  @Order(1)
    void pickRefrigeratedTruckSuccess() {
        try {
            Truck truck = truckController.pickTruck(TruckType.REFRIGERATED, 10);
            assertEquals(truck.getType(), TruckType.REFRIGERATED);
            assertTrue(truck.getMaxWeightTons() >= 10);
        } catch (DeliveryException e) {
            throw new RuntimeException(e);
        }
    }

    @Test @Order(2)
    void pickTruckWeightFail() {
        assertThrows(DeliveryException.class, () -> truckController.pickTruck(TruckType.REGULAR, 100));
    }
    @Test @Order(10)
    void pickTruckAvailabilityFail() {
        assertThrows(DeliveryException.class, this::pickAllTrucksLoop);
    }

    private void pickAllTrucksLoop() throws DeliveryException {
        for (int i = 0; i < truckController.getTruckFleetSize() + 1; i++) {
            truckController.pickTruck(TruckType.REGULAR, 1);
        }
    }

    @Test @Order(3)
    void testPickTruckSuccess() {
        try {
            Truck truck = truckController.pickTruck(TruckType.REFRIGERATED);
            assertEquals(truck.getType(), TruckType.REFRIGERATED);
        } catch (DeliveryException e) {
            throw new RuntimeException(e);
        }
    }

    @Test @Order(3)
    void PickRegularTruckSuccess() {
        try {
            Truck truck = truckController.pickTruck(TruckType.REGULAR);
            assertEquals(truck.getType(), TruckType.REGULAR);
        } catch (DeliveryException e) {
            throw new RuntimeException(e);
        }
    }
}