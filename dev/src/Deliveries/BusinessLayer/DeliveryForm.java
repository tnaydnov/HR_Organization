package Deliveries.BusinessLayer;

import Deliveries.BusinessLayer.Enums_and_Interfaces.*;
import Deliveries.DataAccessLayer.DeliveryFormDAO;
import HR.BusinessLayer.ShiftController;
import HR_Deliveries_Interface.HRIntegrator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DeliveryForm {
    private final int formId;
    private Timestamp dispatchTime;
    private WeightMeasurer weightMeasurer;
    private TripReplanner tripReplanner;
    private List<DeliveryStop> destinationSitesToVisit;
    private List<DeliveryStop> destinationSitesVisited;
    private int maxWeightAllowed;
    private Driver driver;
    private Truck truck;
    private Site originSite;
    private DeliveryManagerImpl deliveryManager;
    private DeliveryFormsController deliveryFormsController;
    private DeliveryStop stopToCancel;
    private int dispatchWeightTons; // Weight of the truck when it leaves the origin site
    private HRIntegrator hrManager;

    private DeliveryStatus status;


    public DeliveryForm(int formId, List<DeliveryStop> stops, Site originSite,
                        Timestamp dispatchTime) throws DeliveryException {
        this.formId = formId;
        this.destinationSitesToVisit = stops;
        this.destinationSitesVisited = new ArrayList<>();
        this.status = DeliveryStatus.NOT_STARTED;
        this.dispatchTime = dispatchTime;
        this.originSite = originSite;
        deliveryManager = DeliveryManagerImpl.getInstance();
        deliveryFormsController = DeliveryFormsController.getInstance();
        hrManager = ShiftController.getInstance();
        updateArrivalTimes();
      //  updateFormIDinStops(); // Do not update in constructor -
      //  fails in DB due to foreign key constraints (form doesn't exist in DB yet)
//        this.driver = driver;
//        this.truck = truck;
    }

    /*
    * This constructor is used when a delivery form is loaded from the database
    */
    public DeliveryForm(int formId, List<DeliveryStop> stopsToVisit,List<DeliveryStop> stopsVisited,
                        Site originSite, Timestamp dispatchTime, DeliveryStatus status, Driver driver, Truck truck)
            throws DeliveryException {
        this.formId = formId;
        this.destinationSitesToVisit = stopsToVisit;
        this.destinationSitesVisited = stopsVisited;
        this.dispatchTime = dispatchTime;
        this.originSite = originSite;
        deliveryManager = DeliveryManagerImpl.getInstance();
        deliveryFormsController = DeliveryFormsController.getInstance();
        hrManager = ShiftController.getInstance();
        updateArrivalTimes();
        setVisitedStopsArrivalTimes();
        this.status = status;
        this.truck = truck;
        maxWeightAllowed = truck.getMaxWeightTons();
        this.driver = driver;
    }

    /*
    To be used for testing purposes only
     */
    public DeliveryForm(int formId, List<DeliveryStop> stops, Site originSite, Timestamp dispatchTime, HRIntegrator hr)
            throws DeliveryException {
        this.formId = formId;
        this.destinationSitesToVisit = stops;
        this.destinationSitesVisited = new ArrayList<>();
        this.dispatchTime = dispatchTime;
        this.originSite = originSite;
        deliveryManager = DeliveryManagerImpl.getInstance();
        deliveryFormsController = DeliveryFormsController.getInstance();
        hrManager = hr;
        updateArrivalTimes();
        //updateFormIDinStops();  // do we actually want to persist if it's a test?
    }

    public void updateFormIDinStops() {
        for(DeliveryStop stop : destinationSitesToVisit){
            stop.setFormID(formId);
        }
    }


    public void addDeliveryStop(DeliveryStop deliveryStop) {
        destinationSitesToVisit.add(deliveryStop);
    }

    public void visitDeliveryStop(DeliveryStop deliveryStop) {
        if (destinationSitesVisited.contains(deliveryStop) ||
                deliveryStop.getStatus() == DeliveryStatus.DELIVERED ||
                deliveryStop.equals(stopToCancel) ||
                deliveryStop.getStatus() == DeliveryStatus.CANCELLED)
        {
            return; // Already visited or cancelled
        }

        deliveryStop.setStatus(DeliveryStatus.DELIVERED); // update status (also in DB)
        destinationSitesVisited.add(deliveryStop);
        //destinationSitesToVisit.remove(deliveryStop); //???????????????????????
        performWeightCheck(deliveryStop.toString());
    }

    public void performWeightCheck(String currentStop) {
        int currentWeight = measureWeight(currentStop);
        setDispatchWeightTons(currentWeight);
        if (currentWeight == -1) {
            cancelForm();
        }
        if (currentWeight > getMaxWeightAllowed()) {
            deliveryManager.replanDelivery(this, tripReplanner);
        }
    }

    private int measureWeight(String currentStop) {
        return weightMeasurer.measureWeight(this, currentStop);
    }

    @Override
    public String toString() {
        // get destination names
        return "Form- ID: "  + formId + " from " + originSite.getName() + " to " +
                destinationSitesToVisit.stream().map(DeliveryStop::getDestination).toList();
    }

    /* setDispatchWeightTons() is called when the truck leaves the origin site */
    public void setDispatchWeightTons(int dispatchWeightTons) {
        this.dispatchWeightTons = dispatchWeightTons;
    }

    // setMaxWeightAllowed() is called when the truck is loaded with items
    private void setMaxWeightAllowed(int maxWeightAllowed) {
        this.maxWeightAllowed = maxWeightAllowed;
        if (dispatchWeightTons > maxWeightAllowed) {
            deliveryManager.replanDelivery(this, tripReplanner);
        }
    }

    public void setDestinationSitesToVisit(List<DeliveryStop> destinationSitesToVisit) {
        this.destinationSitesToVisit = destinationSitesToVisit;
    }

    public int getFormId() {
        return formId;
    }

    public List<DeliveryStop> getDestinationSitesToVisit() {
        return destinationSitesToVisit;
    }

    public List<DeliveryStop> getDestinationSitesVisited() {
        return destinationSitesVisited;
    }

    public TruckType getTruckType() {
        TruckType truckType = TruckType.REGULAR;
        // for each delivery stop, check if the truck type is the same
        for (DeliveryStop stop : destinationSitesToVisit) {
            if (stop.getTruckTypeRequired() == TruckType.REFRIGERATED) {
                truckType = TruckType.REFRIGERATED;
                break;
            }
        }
        return truckType;
    }

    public int getDispatchWeightTons() {
        return dispatchWeightTons;
    }

    public void startJourney(WeightMeasurer weightMeasurer, TripReplanner tripReplanner)  {
        // visit the stops in the order they were added
        this.tripReplanner = tripReplanner;
        this.weightMeasurer = weightMeasurer;
        performWeightCheck(originSite.toString());
        ListIterator<DeliveryStop> iterator = destinationSitesToVisit.listIterator();

//        if(!iterator.hasNext()){
//            iterator.next();
//        }

        while(iterator.hasNext()){
            DeliveryStop currentStop = iterator.next();
            visitDeliveryStop(currentStop);
            if (currentStop.getStatus() == DeliveryStatus.DELIVERED ||
                    currentStop.equals(stopToCancel) ||
                    currentStop.getStatus() == DeliveryStatus.CANCELLED) {
                if(iterator.hasNext()) { //TODO:did it to fix a bug - need to check it
                    iterator.remove();
                }
                // Needs testing
            }
        }
        completeJourney();
    }

    private void completeJourney() {
        driver.freeDriver();
        truck.freeTruck();
        deliveryFormsController.terminateDeliveryForm(this);
    }

    public void cancelForm() {
        driver.freeDriver();
        truck.freeTruck();
        // for delivery stops that were not visited, set their status to cancelled
        for (DeliveryStop stop : destinationSitesToVisit) {
            if (stop.getStatus() != DeliveryStatus.DELIVERED) { // Not sure if this is needed, but just in case
                stop.setStatus(DeliveryStatus.CANCELLED);
                deliveryManager.addDeliveryStop(stop);
            }
        }
    }

    public void cancelStop(DeliveryStop stop) {
        stopToCancel = stop;
        stop.setStatus(DeliveryStatus.CANCELLED);
    }

    public void setWeightMeasurer(WeightMeasurer weightMeasurer) {
        // This is used for testing
        this.weightMeasurer = weightMeasurer;
    }

    public void setTripReplanner(TripReplanner tripReplanner) {
        // This is used for testing
        this.tripReplanner = tripReplanner;
    }


    public int getMaxWeightAllowed() {
        return truck.getMaxWeightTons();
    }

    public void setTruck(Truck newTruck) {
        if (truck != null){
            truck.freeTruck();
        }
        truck = newTruck;
        setMaxWeightAllowed(truck.getMaxWeightTons());
    }

    public void setDriver(Driver newDriver) {
        if (driver != null){
            driver.freeDriver();
        }
        this.driver = newDriver;
    }

    private void updateArrivalTimes()  {
        for (DeliveryStop stop : destinationSitesToVisit) {
            stop.updateArrivalTime(dispatchTime);
            if (!hrManager.checkStoreAvailability(stop.getDestination().getName(), stop.getEstimatedArrivalTime())) {
                // TODO: decide what to do if the site is not available
                // throw new DeliveryException("destination site is not available at the estimated arrival time");
               // System.out.println(stop.getDestination().getName()+" - destination site is not available at the estimated arrival time");
            }
        }
    }

    private void setVisitedStopsArrivalTimes()  {
        for (DeliveryStop stop : destinationSitesVisited) {
            stop.updateArrivalTime(dispatchTime);
        }
    }

    public List<DeliveryStop> getStopsByTime(Timestamp startTime, Timestamp finishTime, String siteName){
        List<DeliveryStop> stops = new ArrayList<>();
        for (DeliveryStop stop : destinationSitesToVisit) {
            if (stop != null && stop.getEstimatedArrivalTime().after(startTime) &&
                    stop.getEstimatedArrivalTime().before(finishTime) &&
                    stop.getDestination().getName().equals(siteName)) {
                stops.add(stop);
            }
        }
        return stops;
    }

    public Timestamp getDispatchTime() {
        return dispatchTime;
    }

    public Driver getDriver() {
        return driver;
    }

    public Truck getTruck() {
        return truck;
    }

    public Site getOriginSite() {
        return originSite;
    }

    public Timestamp getEstimatedTerminationTime() {
        // Returns the estimated arrival time of the last stop
        if (destinationSitesToVisit.isEmpty()) {
            return dispatchTime;
        }
        Timestamp lastStop = destinationSitesToVisit.get(0).getEstimatedArrivalTime();
        for (DeliveryStop stop : destinationSitesToVisit) {
            if (stop.getEstimatedArrivalTime().after(lastStop)) {
                lastStop = stop.getEstimatedArrivalTime();
            }
        }
        return lastStop;
    }

    void setStatus(DeliveryStatus status) { // package-private
        this.status = status;
        DeliveryFormDAO deliveryFormDAO = new DeliveryFormDAO();
        deliveryFormDAO.setStatus(formId, status);
    }

    public DeliveryStatus getStatus() {
        return status;
    }

}