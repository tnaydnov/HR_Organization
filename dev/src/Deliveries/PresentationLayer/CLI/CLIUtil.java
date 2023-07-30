package Deliveries.PresentationLayer.CLI;

import Deliveries.BusinessLayer.DeliveryForm;
import Deliveries.BusinessLayer.DeliveryStop;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TripReplanAction;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TripReplanner;
import Deliveries.BusinessLayer.Enums_and_Interfaces.WeightMeasurer;

import java.util.List;
import java.util.Scanner;

public class CLIUtil implements WeightMeasurer, TripReplanner {
    Scanner scanner;
    public CLIUtil() {
        this.scanner = new Scanner(System.in);
    }
    @Override
    public int measureWeight(DeliveryForm form, String currentStop) {
        String currentSite = form.getDestinationSitesToVisit().get(0).getDestination().getName();
        System.out.println("Successfully delivered to: " + currentSite);
        System.out.println("Leaving" + currentSite + "- what's the current weight of the truck?");
        // ask for an int. if not int, ask again
        while (!scanner.hasNextInt()) {
            System.out.println("Enter truck weight: ");
            scanner.next();
        }

        return scanner.nextInt();
    }

    @Override
    public DeliveryStop removeStop(List<DeliveryStop> stops) {
        System.out.println("The truck is overloaded, and we couldn't arrange a larger truck." +
                " You have to remove some stops.");
        System.out.println("These are the stops you have to visit: ");
        int i = 0;
        for (DeliveryStop stop : stops) {
            i++;
            System.out.println("Stop number " + i + ": " + stop);
        }
        System.out.println("Which stops would you like to remove?");
        // ask for an int. if not int, ask again
        while (!scanner.hasNextInt()) {
            System.out.println("Enter the stop index you'd like to remove: ");
            scanner.next();
        }
        int indexToRemove = scanner.nextInt();
        return stops.get(indexToRemove-1);
    }


    @Override
    public TripReplanAction chooseAction(List<DeliveryStop> stops) {
        System.out.println("The truck is overloaded, and we couldn't arrange a larger truck.");
        System.out.println("These are the stops you have to visit: ");
        int i = 0;
        for (DeliveryStop stop : stops) {
            i++;
            System.out.println("Stop number " + i + ": " + stop);
        }
        System.out.println("What would you like to do?");
        System.out.println("1. Remove stops");
        System.out.println("2. Cancel delivery");
        System.out.println("3. Reweigh truck");
        // ask for an int. if not int, ask again
        while (!scanner.hasNextInt() || scanner.nextInt() > 3 || scanner.nextInt() < 1) {
            System.out.println("Enter the stop index you'd like to remove: ");
            scanner.next();
        }
        if (scanner.nextInt() == 1) {
            return TripReplanAction.REMOVE_STOP;
        }
        else if (scanner.nextInt() == 2) {
            return TripReplanAction.CANCEL_FORM;
        }
        else if (scanner.nextInt() == 3) {
            return TripReplanAction.REWEIGH_TRUCK;
        }
        else {
            throw new IllegalArgumentException("Invalid input");
        }

    }
}