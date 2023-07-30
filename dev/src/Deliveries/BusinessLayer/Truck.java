package Deliveries.BusinessLayer;

import Deliveries.BusinessLayer.Enums_and_Interfaces.Availability;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;

public class Truck {
    private String model;
    private String licensePlate;
    private TruckType type;
    private int maxWeightTons;
    private int netWeightTons;

    private Availability availability;

    @Override
    public String toString() {
        return "Truck{" +
                "model='" + model + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", type=" + type +
                '}';
    }

    public Truck(String model, String licensePlate, TruckType type, int maxWeightTons, int netWeightTons) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.type = type;
        this.maxWeightTons = maxWeightTons;
        this.netWeightTons = netWeightTons;
        // TODO: check if netWeightTons is relevant
        availability = Availability.Available;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public TruckType getType() {
        return type;
    }

    public int getMaxWeightTons() {
        return maxWeightTons;
    }

    public int getNetWeightTons() {
        return netWeightTons;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public void freeTruck() {
        availability = Availability.Available;
    }
}
