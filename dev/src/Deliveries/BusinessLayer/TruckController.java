package Deliveries.BusinessLayer;

import Deliveries.BusinessLayer.Enums_and_Interfaces.Availability;
import Deliveries.BusinessLayer.Enums_and_Interfaces.DeliveryException;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import Deliveries.DataAccessLayer.TruckDAO;

import java.util.HashSet;

public class TruckController {
    private final HashSet<Truck> trucks;
    private final TruckDAO truckDAO;
    private static TruckController instance = null;
    // Singleton Constructor
    private TruckController() {
        trucks = new HashSet<>();
        truckDAO = new TruckDAO();
        //generateTruckFleet(20); // Will not work if DB is full due to primary key constraint
        trucks.addAll(truckDAO.loadData());
    }

    public static TruckController getInstance() {
        if (instance == null) {
            instance = new TruckController();
        }
        return instance;
    }


    public Truck pickTruck(TruckType requiredType) throws DeliveryException {
        for (Truck truck : trucks) {
            if (truck.getType().equals(requiredType) &&
                    truck.getAvailability().equals(Availability.Available)) {
                        truck.setAvailability(Availability.Busy);
                        return truck;
            }
        }
        // No available trucks of the required type
        throw new DeliveryException("No available trucks of type " + requiredType);
    }



    public Truck pickTruck(TruckType requiredType, int requiredWeight) throws DeliveryException{
        for (Truck truck : trucks) {
            if (truck.getType().equals(requiredType) &&
                    truck.getMaxWeightTons() >= requiredWeight &&
                        truck.getAvailability().equals(Availability.Available)) {
                            truck.setAvailability(Availability.Busy);
                            return truck;
            }
        }
        // No available trucks of the required type and weight
        throw new DeliveryException("No available trucks of type " + requiredType + " and weight " + requiredWeight);
    }

    private void generateTruckFleet(int numTrucks) {
        String[] truckModels = {"Toyota", "Sussita", "Ford", "Chevrolet", "Dodge"};
        String[] licensePlates = {"LOL456", "WTF789", "OMG123", "BBQ456",
                "KFC789", "MCD123", "FUK456", "YEA789", "NOO123", "USA456",
                "UAE789", "RUS123", "CHN456", "JPN789", "KOR123", };
        TruckType[] truckTypes = TruckType.values();
        int[] maxWeights = {4, 5, 6};
        int[] netWeights = {2, 3};

        for (int i = 0; i < numTrucks; i++) {
            String model = truckModels[(int)(Math.random() * truckModels.length)];
            String licensePlate = licensePlates[(int)(Math.random() * licensePlates.length)];
            TruckType type = truckTypes[(int)(Math.random() * truckTypes.length)];
            int maxWeight = maxWeights[(int)(Math.random() * maxWeights.length)];
            int netWeight = netWeights[(int)(Math.random() * netWeights.length)];

            Truck truck = new Truck(model, licensePlate, type, maxWeight, netWeight);
            truck.setAvailability(Availability.Available);
            addTruck(truck);
        }
    }

    private void addTruck(Truck truck) {
        if (truckDAO.addTruck(truck)) trucks.add(truck);
    }

    public int getTruckFleetSize() {
        // To be used for testing. Does not throw an exception since it's only a getter.
        return trucks.size();
    }


}
