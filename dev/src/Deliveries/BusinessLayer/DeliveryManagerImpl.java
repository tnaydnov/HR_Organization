package Deliveries.BusinessLayer;

import Deliveries.BusinessLayer.Enums_and_Interfaces.*;
import Deliveries.BusinessLayer.Generators.DeliveryStopGenerator;
import Deliveries.DataAccessLayer.DeliveryStopDAO;
import Deliveries.PresentationLayer.CLI.CLIUtil;

import java.util.*;


public class DeliveryManagerImpl implements DeliveryManager {
    public static final int PENDING_SIZE = 30;
    private final TruckController truckController;
    private final DriverController driverController;
    private Set<DeliveryStop> pendingDeliveryStops;
    private DeliveryFormsController deliveryFormsController;
    private TripReplanner tripReplanner;
    private int deliveryCount;
    private static DeliveryManagerImpl instance = null;
    private DeliveryStopDAO deliveryStopDAO;
    // Singleton Constructor
   private DeliveryManagerImpl() {
            pendingDeliveryStops = new HashSet<>();
            truckController = TruckController.getInstance();
            driverController = DriverController.getInstance();
            deliveryFormsController = DeliveryFormsController.getInstance();
            tripReplanner = new CLIUtil(); // TODO: change according to the UI chosen
            DeliveryStopGenerator deliveryStopGenerator = new DeliveryStopGenerator();
            pendingDeliveryStops.addAll(deliveryStopGenerator.getPendingDeliveryStops(PENDING_SIZE));
            deliveryStopDAO = new DeliveryStopDAO();
            deliveryCount = deliveryStopDAO.getCount();
            // shouldn't add all to the pending list, the completed stops should be added their forms
    }

    public static DeliveryManagerImpl getInstance() {
        if (instance == null) {
            instance = new DeliveryManagerImpl();
        }
        return instance;
    }

    @Override
    public int addDeliveryStop(Map<String, Integer> items, Site origin, Site destination, TruckType truckType) {
        DeliveryStop deliveryStop = new DeliveryStop(++deliveryCount, items, origin, destination, truckType);
        pendingDeliveryStops.add(deliveryStop);
        deliveryStopDAO.addStop(deliveryStop);
        // decide how to manage the origin.
        return deliveryStop.getShipmentInstanceID();
    }

    public void addDeliveryStop(DeliveryStop deliveryStop) {
        pendingDeliveryStops.add(deliveryStop);
    }

    @Override
    public void removeDeliveryStop(int deliveryId) {
        pendingDeliveryStops.remove(deliveryId);
        deliveryStopDAO.deleteStop(deliveryId);
    }


    //maybe private
    public void createDeliveryGroup(){
        List<HashMap<Site,List<DeliveryStop>>> originToZones = createDeliveryLists(pendingDeliveryStops);
        for(HashMap<Site,List<DeliveryStop>> siteToStopsSortedByOrigin: originToZones) {

            for (Map.Entry<Site, List<DeliveryStop>> entries : siteToStopsSortedByOrigin.entrySet()) {
                try {
                    if (entries.getValue().size() == 0) {
                        continue; // no stops for this origin, do we ever reach this?
                    }
                    deliveryFormsController.createForm(entries.getValue(), entries.getKey());
                    entries.getValue().forEach(pendingDeliveryStops::remove); // removes the added stops from the pending list
//                    for(DeliveryStop stop : entries.getValue()){
//                        pendingDeliveryStops.remove(stop);
//                        deliveryStopDAO.updateStatus(stop.getShipmentInstanceID(), DeliveryStatus.DELIVERED);
//                    }
                } catch (DeliveryException e) {
                    // Notify UI
//                    System.out.println(e.getMessage());
//                    System.out.println("Delivery group creation failed for origin " + entries.getValue().get(0).getOrigin());
                }
            }
        }
    }



    public void replanDelivery(DeliveryForm form, TripReplanner tripReplanner) {
       this.tripReplanner = tripReplanner;
        try {
            replaceTruck(form);
        } catch (DeliveryException e) {
            // Notify UI
            System.out.println("Truck is overweight for delivery " + form
                    + ". Tried to find a new truck but " + e.getMessage());
        }
        TripReplanAction action = tripReplanner.chooseAction(form.getDestinationSitesToVisit());
        if (action == TripReplanAction.REMOVE_STOP) {
            DeliveryStop stopToCancel = tripReplanner.removeStop(form.getDestinationSitesToVisit());
            form.cancelStop(stopToCancel);
            pendingDeliveryStops.add(stopToCancel);
        }
        else if (action == TripReplanAction.CANCEL_FORM) {
            form.cancelForm();
        }
        else if (action == TripReplanAction.REWEIGH_TRUCK) {
            form.performWeightCheck(form.getDestinationSitesToVisit().get(0).toString());
            // TODO: check that this is the correct currentStop
        }
        // else do nothing, will be handled by the UI according to submission 1
    }

    private void replaceTruck(DeliveryForm form) throws DeliveryException {
        Truck newTruck = truckController.pickTruck(form.getTruckType(), form.getDispatchWeightTons());
        form.setTruck(newTruck);
        Driver newDriver = driverController.pickDriver(newTruck, form.getDispatchTime(),
                form.getEstimatedTerminationTime());
        form.setDriver(newDriver);
    }

    private HashMap<Site,List<DeliveryStop>> sortStopsByOrigin(Set<DeliveryStop> pendingDeliveryStops){
        HashMap<Site,List<DeliveryStop>> sortedByOrigin = new HashMap<>();
        for (DeliveryStop stop : pendingDeliveryStops) {
            Site origin = stop.getOrigin();
            if(!sortedByOrigin.containsKey(origin)){
                ArrayList<DeliveryStop> originStops = new ArrayList<>();
                originStops.add(stop);
                sortedByOrigin.put(origin,originStops);
            }
            else{
                sortedByOrigin.get(origin).add(stop);
            }
        }
        return sortedByOrigin;
    }

    //takes a list of the stops for each origin and separates to lists sorted by zones
    private HashMap<Integer,List<DeliveryStop>> sortByDeliveryZones(List<DeliveryStop> originsStops){
       HashMap<Integer,List<DeliveryStop>> deliveryZonesSorted = new HashMap<>(); //key-delivery zone, value-stops in that delivery zones
        for (DeliveryStop stop: originsStops) {
            if (!deliveryZonesSorted.containsKey(stop.getDestination().getDeliveryZone())) {
                    ArrayList<DeliveryStop> deliveryZoneStops = new ArrayList<>();
                    deliveryZoneStops.add(stop);
                    deliveryZonesSorted.put(stop.getDestination().getDeliveryZone(), deliveryZoneStops);
                } else {
                    deliveryZonesSorted.get(stop.getDestination().getDeliveryZone()).add(stop);
                }

        }
        return deliveryZonesSorted;
    }

    public List<HashMap<Site,List<DeliveryStop>>> createDeliveryLists(Set<DeliveryStop> pendingDeliveryStops){
        List<HashMap<Site,List<DeliveryStop>>> originToSortedByZones = new ArrayList<>();
        HashMap<Site,List<DeliveryStop>> originSorted = sortStopsByOrigin(pendingDeliveryStops);
        for(Map.Entry<Site,List<DeliveryStop>> originsStops: originSorted.entrySet()){
            List<DeliveryStop> stops = originsStops.getValue();
            HashMap<Integer,List<DeliveryStop>> zoneSorted = sortByDeliveryZones(stops);
            for (Map.Entry<Integer,List<DeliveryStop>> entries: zoneSorted.entrySet()) {
                HashMap<Site,List<DeliveryStop>> newList = new HashMap<>();
                newList.put(originsStops.getKey(),entries.getValue());
                originToSortedByZones.add(newList);
            }
        }
        return originToSortedByZones;
    }

    public TruckController getTruckController() {
        return truckController;
    }

    public DriverController getDriverController() {
        return driverController;
    }

    public DeliveryFormsController getDeliveryFormsController() {
        return deliveryFormsController;
    }

    public Iterable<? extends DeliveryStop> getPendingDeliveryStops() {
        return pendingDeliveryStops;
    }
}


