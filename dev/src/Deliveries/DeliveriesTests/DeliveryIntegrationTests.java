//package Deliveries.DeliveriesTests;
//
//import Deliveries.BusinessLayer.*;
//import Deliveries.BusinessLayer.Enums_and_Interfaces.DeliveryStatus;
//import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
//import Deliveries.BusinessLayer.Enums_and_Interfaces.WeightMeasurer;
//import Deliveries.BusinessLayer.Generators.SiteGenerator;
//import HR.BusinessLayer.ShiftController;
//import HR_Deliveries_Interface.HRIntegrator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Spy;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class DeliveryIntegrationTests {
//
//    private DeliveryForm deliveryForm;
//    private DeliveryFormsController deliveryFormsController;
//    private Site origin;
//    private Site destination2;
//    private Site destination;
//    private TruckController truckController;
//    private DriverController driverController;
//    @Spy
//    HRIntegrator hrIntegrator;
//    private DeliveryStop stop1;
//    @Spy
//    private DeliveryStop stop1Spy;
//    private DeliveryStop stop2;
//    @Spy
//    private DeliveryStop stop2Spy;
//    private Truck truck;
//    private Driver driver;
//    @Mock
//    WeightMeasurer weightMeasurer;
//
//    @BeforeEach
//    void setUp() {
//        SiteGenerator siteGenerator = new SiteGenerator();
//        List<Site> siteList = siteGenerator.getSitesList();
//        origin = siteList.get(0);
//        destination2 = siteList.get(1);
//        destination = siteList.get(2);
//        Timestamp mockTimestamp = new Timestamp(System.currentTimeMillis());
//
//        weightMeasurer = mock(WeightMeasurer.class);
//        when(weightMeasurer.measureWeight(any(DeliveryForm.class))).thenReturn(5);
//
//
//
//        hrIntegrator = mock(ShiftController.class);
//        // mocks the hrIntegrator
//
//
//        Map<String, Integer> items1 = new HashMap<>();
//        items1.put("Box", 5);
//        items1.put("Envelope", 10);
//        stop1 = new DeliveryStop(1, items1, origin, destination, TruckType.REGULAR);
//        stop1Spy = spy(stop1);
//        stop1Spy.updateArrivalTime(mockTimestamp);
//        when(stop1Spy.getEstimatedArrivalTime()).thenReturn(mockTimestamp);
//
//        Map<String, Integer> items2 = new HashMap<>();
//        items2.put("Pallet", 2);
//        items2.put("Crate", 1);
//        stop2 = new DeliveryStop(2, items2, origin, destination2, TruckType.REFRIGERATED);
//        stop2Spy = spy(stop2);
//        when(stop2Spy.getEstimatedArrivalTime()).thenReturn(mockTimestamp);
//        //when(hrIntegrator.checkStoreAvailability(destination2.getName(), mockTimestamp)).thenReturn(true);
//        List<DeliveryStop> destinations = new ArrayList<>();
//        destinations.add(stop1Spy);
//        destinations.add(stop2Spy);
//        when(hrIntegrator.checkStoreAvailability(destination.getName(), mockTimestamp)).thenReturn(true);
//        when(hrIntegrator.checkStoreAvailability(destination2.getName(), mockTimestamp)).thenReturn(true);
//        prepareDriverController();
//        truckController = TruckController.getInstance();
//        deliveryFormsController = DeliveryFormsController.getInstance();
//        deliveryFormsController.setTestingMode();
//
//
//        // Simulates that all drivers are available
//
//
//        try {
//            deliveryFormsController.createForm(destinations, origin, mockTimestamp, hrIntegrator);
//            // TODO - change this testing-only constructor to setters
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        deliveryForm = deliveryFormsController.getDeliveryForm(0);
//        deliveryForm.setWeightMeasurer(weightMeasurer);
//
//    }
//
//    private void prepareDriverController() {
//        driverController = DriverController.getInstance();
//        driverController.setTestEnvironment();
//        List<String> driverIds = driverController.getDriverIds();
//        when(hrIntegrator.assignDrivers(any(), any(), any())).thenReturn(true);
//        when(hrIntegrator.getAvailableDrivers(any(), any())).thenReturn(driverIds);
//        try {driverController.setHrManager(hrIntegrator);} catch (Exception ignored) {}
//    }
//
//
//    @Test
//    void visitDeliveryStop() {
////        deliveryForm.addDeliveryStop(stop1Spy);
////        deliveryForm.addDeliveryStop(stop2Spy);
////        deliveryForm.visitDeliveryStop(stop1Spy);
////        assertEquals(DeliveryStatus.DELIVERED, stop1.getStatus());
////        assertEquals(DeliveryStatus.NOT_STARTED, stop2.getStatus());
//    }
//
//    @Test
//    void preparedDeliveryFormStartJourneySuccess() {
//        assertEquals(DeliveryStatus.NOT_STARTED, stop1Spy.getStatus());
//        assertEquals(DeliveryStatus.NOT_STARTED, stop2Spy.getStatus());
//        deliveryForm.startJourney();
//        assertEquals(DeliveryStatus.DELIVERED, stop1Spy.getStatus());
//        assertEquals(DeliveryStatus.DELIVERED, stop2Spy.getStatus());
//    }
//
//    @Test
//    void cancelForm() {
//        deliveryForm.cancelForm();
//        assertEquals(DeliveryStatus.CANCELLED, stop1Spy.getStatus());
//        assertEquals(DeliveryStatus.CANCELLED, stop2Spy.getStatus());
//        assertEquals(new ArrayList<DeliveryStop>(), deliveryForm.getDestinationSitesVisited());
//    }
//
//    @Test
//    void cancelStop() {
//        deliveryForm.cancelStop(stop1Spy);
//        deliveryForm.startJourney();
//        assertEquals(DeliveryStatus.CANCELLED, stop1Spy.getStatus());
//        assertEquals(DeliveryStatus.DELIVERED, stop2Spy.getStatus());
//    }
//
//    @Test
//    void setDispatchWeightTons() {
//        int dispatchWeightTons = deliveryForm.getMaxWeightAllowed();
//        deliveryForm.setDispatchWeightTons(dispatchWeightTons);
//        assertEquals(dispatchWeightTons, deliveryForm.getDispatchWeightTons());
//    }
//
//    @Test
//    void setDestinationSitesToVisit() {
////        List<DeliveryStop> destinationSites = new ArrayList<>();
////        deliveryForm.setDestinationSitesToVisit(destinationSites);
////        assertEquals(destinationSites, deliveryForm.getDestinationSitesToVisit());
////
////
////        destinationSites.add(stop1);
////        destinationSites.add(stop2);
////        deliveryForm.setDestinationSitesToVisit(destinationSites);
////        assertEquals(destinationSites, deliveryForm.getDestinationSitesToVisit());
//    }
//
//    @Test
//    void getFormId() {
//        assertEquals(0, deliveryForm.getFormId());
//    }
//
//    @Test
//    void getTruckType() {
//        TruckType truckType = TruckType.REFRIGERATED;
//        assertEquals(truckType, deliveryForm.getTruckType());
//    }
//
//    @Test
//    void getDispatchWeightTons() {
//        int dispatchWeightTons = 5;
//        deliveryForm.setDispatchWeightTons(dispatchWeightTons);
//        assertEquals(dispatchWeightTons, deliveryForm.getDispatchWeightTons());
//    }
//}
//
