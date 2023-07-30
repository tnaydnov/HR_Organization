package Deliveries.BusinessLayer.Enums_and_Interfaces;
// extend compile time exception
public class DeliveryException extends Exception {
    public DeliveryException(String message) {
        super(message);
    }
}
