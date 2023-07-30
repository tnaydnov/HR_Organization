package Deliveries.DeliveriesTests;

import Deliveries.BusinessLayer.DeliveryStop;
import Deliveries.BusinessLayer.Generators.DeliveryStopGenerator;
import Deliveries.BusinessLayer.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class DeliveryStopTest {
    DeliveryStop deliveryStop1;
    DeliveryStop deliveryStop2;
    DeliveryStop deliveryStop3;
    @Mock
    Site site1;
    @Mock
    Site site2;

    @BeforeEach
    void setUp() {
        site1 = mock(Site.class);
        site2 = mock(Site.class);
        try {
            deliveryStop1 = new DeliveryStop(true, site1, site2);
        } catch (Exception ignored) {}
    }

    @Test
    void testToString() {
        DeliveryStopGenerator deliveryStopGenerator = new DeliveryStopGenerator();
        List<DeliveryStop> deliveryStop = deliveryStopGenerator.getPendingDeliveryStops(30);
        System.out.println(deliveryStop);
    }


    @Test
    void testEqualsSuccess() {
        try {
            deliveryStop2 = new DeliveryStop(true, 1);
            deliveryStop3 = new DeliveryStop(true, 1);
        } catch (Exception ignored) {}
        assertEquals(deliveryStop3, deliveryStop2);
    }

    @Test
    void testEqualsFailure() {
        try {
            deliveryStop2 = new DeliveryStop(true, 1);
            deliveryStop3 = new DeliveryStop(true, 7);
        } catch (Exception ignored) {}
        assertNotEquals(deliveryStop3, deliveryStop2);
    }

    @Test
    void updateArrivalTimeSuccess() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(site1.getDeliveryZone()).thenReturn(0);
        when(site2.getDeliveryZone()).thenReturn(10);
        deliveryStop1.updateArrivalTime(timestamp);
        Timestamp expectedArrival = Timestamp.from(timestamp.toInstant().plusSeconds((110/80)/(60*60)));
        assertEquals(deliveryStop1.getEstimatedArrivalTime(), expectedArrival);
    }

    @Test
    void updateArrivalTimeFail() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(site1.getDeliveryZone()).thenReturn(0);
        when(site2.getDeliveryZone()).thenReturn(10);
        deliveryStop1.updateArrivalTime(timestamp);
        // should fail due to incorrect time calculation
        Timestamp expectedArrival = Timestamp.from(timestamp.toInstant().plusSeconds((100/80)/(60*60)));
        assertEquals(deliveryStop1.getEstimatedArrivalTime(), expectedArrival);
    }

}