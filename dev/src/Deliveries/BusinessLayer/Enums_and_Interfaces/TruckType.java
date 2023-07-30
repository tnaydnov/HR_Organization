package Deliveries.BusinessLayer.Enums_and_Interfaces;

public enum TruckType {
    REFRIGERATED, REGULAR;

    @Override
    public String toString() {
        // capitalize first letter
        String s = super.toString();
        return s.charAt(0) + s.substring(1).toLowerCase();
    }
}
