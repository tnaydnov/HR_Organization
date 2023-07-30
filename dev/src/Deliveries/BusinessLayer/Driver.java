package Deliveries.BusinessLayer;


import Deliveries.BusinessLayer.Enums_and_Interfaces.Availability;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;

public class Driver {
    private final String name;
    private final String id;
    private final String phone;

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    private License license;

    private Availability availability;

    public Driver(String name, String id, String phone, License license) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.license = license;
        availability = Availability.Available;
    }


    public String getName() {
        return name;
    }

    public License getLicense() {
        return license;
    }

    public Availability getAvailability() {
        return availability;
    }

    public boolean isAvailable() {
        return availability == Availability.Available;
    }

    public String getId() {
        return id;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public void freeDriver() {
        availability = Availability.Available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver driver)) return false;

        return getId().equals(driver.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public boolean isLicensed(Truck truck) {
        return license.isLicensed(truck);
    }

    public String getPhone() {
        return phone;
    }
}
