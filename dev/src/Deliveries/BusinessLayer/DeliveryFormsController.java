package Deliveries.BusinessLayer;

import Deliveries.BusinessLayer.Enums_and_Interfaces.*;
import Deliveries.BusinessLayer.Generators.StoreAvailabilityChecker;
import Deliveries.DataAccessLayer.DeliveryFormDAO;
import HR_Deliveries_Interface.DeliveryIntegrator;
import HR_Deliveries_Interface.HRIntegrator;

import java.sql.Timestamp;
import java.util.*;

public class DeliveryFormsController implements DeliveryIntegrator {
    private final Set<DeliveryForm> pendingDeliveryForms;
    private final Set<DeliveryForm> completedDeliveryForms;
    // singleton
    private static DeliveryFormsController instance;
    private int deliveryFormCount;
    private final TruckController truckController;
    private final DriverController driverController;
    private final DeliveryFormDAO deliveryFormDAO;
    private boolean TESTING_MODE;
    StoreAvailabilityChecker storeAvailabilityChecker;


    private DeliveryFormsController() {
        pendingDeliveryForms = new HashSet<>();
        completedDeliveryForms = new HashSet<>();
        deliveryFormCount = 0;
        truckController = TruckController.getInstance();
        driverController = DriverController.getInstance();
        deliveryFormDAO = new DeliveryFormDAO();
        TESTING_MODE = false;
        storeAvailabilityChecker = new StoreAvailabilityChecker();
        // DO NOT load data here - causes infinite loop
    }

    public void loadFormsData() {
        List<DeliveryForm> deliveryForms = new ArrayList<>(deliveryFormDAO.loadData());
        for (DeliveryForm deliveryForm : deliveryForms) {
            if (deliveryForm.getStatus().equals(DeliveryStatus.NOT_STARTED)) {
                pendingDeliveryForms.add(deliveryForm);
            }
            else if (deliveryForm.getStatus().equals(DeliveryStatus.DELIVERED)) {
                completedDeliveryForms.add(deliveryForm);
            }
        }
        deliveryFormCount = deliveryFormDAO.getNextId();

    }

    public static DeliveryFormsController getInstance() {
        if (instance == null) {
            instance = new DeliveryFormsController();
        }
        return instance; // Does it have to be a singleton?
    }


    private void addDeliveryForm(DeliveryForm deliveryForm) {
        pendingDeliveryForms.add(deliveryForm);
        deliveryFormDAO.addDeliveryForm(deliveryForm);
        deliveryForm.updateFormIDinStops();
    }

    public void removeDeliveryForm(DeliveryForm deliveryForm) {
        this.pendingDeliveryForms.remove(deliveryForm);
    }

   public Set<DeliveryForm> getPendingDeliveryForms() {
        // sort the pending forms by ID
        Comparator<DeliveryForm> idComparator = Comparator.comparingInt(DeliveryForm::getFormId);
        Set<DeliveryForm> sortedForms = new TreeSet<>(idComparator);
        sortedForms.addAll(pendingDeliveryForms);
        return sortedForms;
    }

    public void startDeliveryForm(DeliveryForm deliveryForm, WeightMeasurer weightMeasurer, TripReplanner tripReplanner) {
        deliveryForm.startJourney(weightMeasurer, tripReplanner);
    }

    // print deliveries
    public void printPendingDeliveryForms() {
        for (DeliveryForm deliveryForm : pendingDeliveryForms) {
            System.out.println(deliveryForm);
        }
    }


    public void printCompletedDeliveryForms() {
        for (DeliveryForm deliveryForm : completedDeliveryForms) {
            System.out.println(deliveryForm);
        }
    }

    public void terminateDeliveryForm(DeliveryForm deliveryForm) {
        pendingDeliveryForms.remove(deliveryForm);
        completedDeliveryForms.add(deliveryForm);
        deliveryForm.setStatus(DeliveryStatus.DELIVERED);
        deliveryFormDAO.setStatus(deliveryForm.getFormId(), DeliveryStatus.DELIVERED);
    }

    public DeliveryForm getDeliveryForm(int id) {
        for (DeliveryForm deliveryForm : pendingDeliveryForms) {
            if (deliveryForm.getFormId() == id) {
                return deliveryForm;
            }
        }
        return null;
    }

    @Override
    public Set<DeliveryStop> getDeliveryByArrivalTime(Timestamp startTime, Timestamp finishTime, String store) {
        Set<DeliveryStop> deliveryStops = new HashSet<>();
        for (DeliveryForm deliveryForm : pendingDeliveryForms) {
            deliveryStops.addAll(deliveryForm.getStopsByTime(startTime, finishTime, store));
        }
        return deliveryStops;
    }

    /**
     * Creates a delivery form with the given stops and origin
     */
    public void createForm(List<DeliveryStop> stops, Site origin) throws DeliveryException {
        if (stops.size() == 0) {
            throw new DeliveryException("No stops were given");
        }
        Timestamp dispatchTime = storeAvailabilityChecker.checkStoreAvailability(stops);
        if (dispatchTime == null) {
            throw new DeliveryException("Couldn't find a time where all stores are available to accept the delivery");
        }
        DeliveryForm deliveryForm = new DeliveryForm(deliveryFormCount++, stops, origin,
                dispatchTime);
        TruckType truckType = getTruckType(stops);
        Truck truck = truckController.pickTruck(truckType);
        deliveryForm.setTruck(truck);
        Driver driver = driverController.pickDriver(truck, deliveryForm.getDispatchTime(),
                deliveryForm.getEstimatedTerminationTime());

        deliveryForm.setDriver(driver);
        addDeliveryForm(deliveryForm);
        deliveryForm.updateFormIDinStops();
    }



    private TruckType getTruckType(List<DeliveryStop> destinationSitesToVisit) {
        TruckType truckType = TruckType.REGULAR;
        // for each delivery stop, check if the truck type is the same
        for (DeliveryStop stop : destinationSitesToVisit) {
            if (stop.getTruckTypeRequired() == TruckType.REFRIGERATED) {
                truckType = TruckType.REFRIGERATED;
                return truckType;
            }
        }
        return truckType;
    }

    /**
     * should only be called if testing the class
     */
    public TruckType getTruckTypeTest(List<DeliveryStop> destinationSitesToVisit) throws Exception{
        if (!TESTING_MODE){
            throw new Exception("Not in testing mode");
        }
        return getTruckType(destinationSitesToVisit);
    }

    /**
     * should only be called if testing the class
     */
    public void setTestingMode(){
        TESTING_MODE = true;
    }



    /*
    To be used only for testing
     */
    public void createForm(List<DeliveryStop> stops, Site origin, Timestamp dispatchTime ,HRIntegrator hr) throws Exception {
        if (!TESTING_MODE) throw new Exception();
        DeliveryForm deliveryForm = new DeliveryForm(deliveryFormCount++, stops, origin,
                dispatchTime, hr);
        TruckType truckType = getTruckType(stops);
        Truck truck = truckController.pickTruck(truckType);
        deliveryForm.setTruck(truck);
        Driver driver = driverController.pickDriver(truck, deliveryForm.getDispatchTime(),
                deliveryForm.getEstimatedTerminationTime());

        deliveryForm.setDriver(driver);
        addDeliveryForm(deliveryForm);
    }

    public boolean isCompleted(DeliveryForm deliveryForm){
        return completedDeliveryForms.contains(deliveryForm);
    }
}
