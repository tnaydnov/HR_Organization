package Deliveries.DeliveriesTests;

import Deliveries.BusinessLayer.*;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeliveryFormsControllerTest {
    DeliveryFormsController deliveryFormsController;
    @Mock
    DeliveryStop deliveryStop1;
    @Mock
    DeliveryStop deliveryStop2;
    @Mock
    DeliveryForm deliveryForm1;
    @Mock
    TruckController truckController;
    @Mock
    Truck truck1;
    @Mock
    DriverController driverController;

    @BeforeEach
    void setUp() {
        truckController = TruckController.getInstance();
        driverController = DriverController.getInstance();
        deliveryFormsController = DeliveryFormsController.getInstance();
        deliveryStop1 = mock(DeliveryStop.class);
        deliveryStop2 = mock(DeliveryStop.class);
        deliveryForm1 = mock(DeliveryForm.class);
        deliveryFormsController.setTestingMode();
    }

    @Test
    void printCompletedDeliveryForms() {
    }

    @Test
    void terminateDeliveryForm() {
    }

    @Test
    void createForm() {
    }

    @Test
    void getTruckTypeRegularSuccess() throws Exception {
        when(deliveryStop1.getTruckTypeRequired()).thenReturn(TruckType.REGULAR);
        when(deliveryStop2.getTruckTypeRequired()).thenReturn(TruckType.REGULAR);
        List<DeliveryStop> deliveryStops = List.of(deliveryStop1, deliveryStop2);

        assertEquals(TruckType.REGULAR, deliveryFormsController.getTruckTypeTest(deliveryStops));
    }

    @Test
    void getTruckTypeRegularFail() {
        when(deliveryStop1.getTruckTypeRequired()).thenReturn(TruckType.REGULAR);
        when(deliveryStop2.getTruckTypeRequired()).thenReturn(TruckType.REFRIGERATED);
        List<DeliveryStop> deliveryStops = List.of(deliveryStop1, deliveryStop2);

        try{assertNotEquals(TruckType.REGULAR, deliveryFormsController.getTruckTypeTest(deliveryStops));}
        catch (Exception ignored) {}

    }

    @Test
    void getTruckTypeRefrigeratedSuccess() {
        when(deliveryStop1.getTruckTypeRequired()).thenReturn(TruckType.REGULAR);
        when(deliveryStop2.getTruckTypeRequired()).thenReturn(TruckType.REFRIGERATED);
        List<DeliveryStop> deliveryStops = List.of(deliveryStop1, deliveryStop2);

        try{assertEquals(TruckType.REFRIGERATED, deliveryFormsController.getTruckTypeTest(deliveryStops));}
        catch (Exception ignored) {}
    }

    @Test
    void getTruckTypeRefrigeratedFail() {
        when(deliveryStop1.getTruckTypeRequired()).thenReturn(TruckType.REGULAR);
        when(deliveryStop2.getTruckTypeRequired()).thenReturn(TruckType.REGULAR);
        List<DeliveryStop> deliveryStops = List.of(deliveryStop1, deliveryStop2);

        try{assertNotEquals(TruckType.REFRIGERATED, deliveryFormsController.getTruckTypeTest(deliveryStops));}
        catch (Exception ignored) {}

    }
}