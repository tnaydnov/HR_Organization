package Deliveries.PresentationLayer.CLI;

import Deliveries.BusinessLayer.*;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import Deliveries.BusinessLayer.Generators.SiteGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class CLI {
    // main method
    public static void main(String[] args) {
        // create a new DeliveryFormsController
        // DeliveryManagerService deliveryManagerService = new DeliveryManagerService();
        Scanner scanner = new Scanner(System.in);
        CLIUtil cliUtil = new CLIUtil();
        SiteGenerator siteGenerator = new SiteGenerator();
        List<Site> sitesList = siteGenerator.getSitesList();
        DeliveryManagerImpl deliveryManager = DeliveryManagerImpl.getInstance(); //removed the use of service class
        DeliveryFormsController deliveryFormsController = deliveryManager.getDeliveryFormsController();
        deliveryFormsController.loadFormsData();
        System.out.println("Welcome to the delivery manager!");
        System.out.println("The following sites were auto generated and can be used for this demo: ");
        for (Site site : sitesList) {
            // print the site and its index in the list
            System.out.println(sitesList.indexOf(site) + "- " + site);
        }
        System.out.println();
        System.out.println();
        System.out.println("In order to launch a delivery, you should first provide details.");
        System.out.println("A delivery consists of a list of delivery stops");
        System.out.println("Each delivery stop contains an origin, a destination and a list of items to deliver");

        menuLoop(scanner, sitesList, deliveryManager, deliveryFormsController);
        System.out.println("Thank you for using the delivery manager, bye bye!");

    }

    private static int getMenuAns(Scanner scanner) {
        printMenu();
        while (!scanner.hasNextInt()) {
            System.out.print("Your choice: ");
            scanner.next();
        }
        int ans = scanner.nextInt();
        return ans;
    }

    private static void menuLoop(Scanner scanner, List<Site> sitesList, DeliveryManagerImpl deliveryManager, DeliveryFormsController deliveryFormsController) {
        try {
            int ans = getMenuAns(scanner);
            while (ans != 4) {
                if (ans == 1) {
                    addDeliveryStop(scanner, deliveryManager, sitesList);
                } else if (ans == 2) {
                    removeStop(scanner, deliveryManager);
                } else if (ans == 3) {
                    executeDeliveries(scanner, deliveryManager, deliveryFormsController);
                }
                ans = getMenuAns(scanner);
            }
        } catch (Exception e) {
            System.out.println("An error occurred -" + e.getMessage());
            menuLoop(scanner, sitesList, deliveryManager, deliveryFormsController);
        }
    }

    private static void removeStop(Scanner scanner, DeliveryManagerImpl deliveryManager) {
        System.out.println("Note: This should only be used for stops that you added by mistake," +
                " it is not related to the overweight truck requirement");
        System.out.println("The following stops are available:");
        for (DeliveryStop deliveryStop : deliveryManager.getPendingDeliveryStops()) {
            System.out.println(deliveryStop);
        }
        System.out.println();
        System.out.println("Enter the ID of the stop you want to remove:");
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid ID: ");
            scanner.next();
        }
        int id = scanner.nextInt();
        deliveryManager.removeDeliveryStop(id);
    }

    private static void executeDeliveries(Scanner scanner, DeliveryManagerImpl deliveryManager, DeliveryFormsController deliveryFormsController) {
        deliveryManager.createDeliveryGroup();
        if (!deliveryManager.getDeliveryFormsController().getPendingDeliveryForms().iterator().hasNext()) {
            System.out.println("Couldn't create delivery groups due to an illegal combination of delivery stops");
            return;
        }
        System.out.println("The following delivery forms were created:");
        deliveryFormsController.printPendingDeliveryForms();

        System.out.println("Enter the ID of the delivery form you want to execute:");
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid ID: ");
            scanner.next();
        }
        int id = scanner.nextInt();
        DeliveryForm deliveryForm = deliveryFormsController.getDeliveryForm(id);
        CLIUtil cliUtil = new CLIUtil();
        deliveryFormsController.startDeliveryForm(deliveryForm, cliUtil, cliUtil);
        if (deliveryFormsController.isCompleted(deliveryForm)) {
            System.out.println("Delivery form " + id + " was completed successfully!");
        } else {
            System.out.println("Delivery form " + id + " cancelled, " +
                    "its unvisited stops were returned to the pool of pending stops");
        }


    }

    private static void addDeliveryStop(Scanner scanner, DeliveryManagerImpl deliveryManager, List<Site> sitesList) {
        System.out.println("Here you can add a delivery stop to the pool of visits to be made");
        while (true) {
            System.out.println("Would you like to add a delivery stop? (Y/N)");
            String answer = scanner.next();
            if (answer.equals("Y") || answer.equals("y")) {
                System.out.println("Please enter the origin details");
                Site originBranch = getSite(scanner, sitesList);
                System.out.println();
                System.out.println("Origin is set! Please enter the details about the destinations");
                System.out.println("Please enter the destination details");
                Site destinationBranch = getSite(scanner, sitesList);
                HashMap<String, Integer> deliveryItems = new HashMap<>();
                System.out.println("do you need a regular(1) or a refrigerated(2) truck? (1/2)");
                //
                int truckTypeAns = 0;
                TruckType truckType = TruckType.REGULAR;

                while(true) {
                    try {
                            truckTypeAns = scanner.nextInt();

                            if ((truckTypeAns == 1) || (truckTypeAns == 2)) {
                                truckType = (truckTypeAns == 1) ? TruckType.REGULAR : TruckType.REFRIGERATED;
                                break;
                            } else {
                                throw new Exception("invalid input");
                            }

                    } catch (Exception e) {
                        System.out.println("Invalid answer. try again.");
                        System.out.println("do you need a regular(1) or a refrigerated(2) truck? (1/2)");
                        scanner.nextLine();
                    }
                }


                while (true) {
                    System.out.println("Would you like to add an item to deliver to "
                            + destinationBranch.getName() + "? (Y/N)");
                    String answer2 = scanner.next();
                    if (answer2.equals("Y") || answer2.equals("y")) {

                        String item = askForItem(scanner);
                        int quantity = askForQuantity(scanner);
                        deliveryItems.put(item, quantity);
                    } else if (answer2.equals("N") || answer2.equals("n")) {
                        int id = deliveryManager.addDeliveryStop(deliveryItems, originBranch, destinationBranch,truckType);
                        System.out.println("Delivery added successfully. The delivery ID is " + id);

                        break;
                    }
                }
            } else if (answer.equals("N") || answer.equals("n")) {
                break;
            }
        }
    }

    private static Site getSite(Scanner scanner) {
        System.out.println("Please enter the branch name: ");
        String branchName = scanner.nextLine();
        // ask for an address
        System.out.println("Please enter the address of the branch: ");
        String branchAddress = scanner.nextLine();
        // ask for a contact name
        System.out.println("Please enter the contact name of the branch: ");
        String branchContactName = scanner.nextLine();
        // ask for a contact phone
        System.out.println("Please enter the contact phone of the branch: ");
        String branchContactPhone = scanner.nextLine();
        // ask for a delivery zone
        System.out.println("Please enter the delivery zone of the branch: ");
        // create a new branch
        Site branch = new Site(branchName, branchAddress, branchContactName, branchContactPhone, 2);
        return branch;
    }

    private static Site getSite(Scanner scanner, List<Site> sitesList) {
        System.out.println("Please enter the index (shown on top) of the site you would like to choose: ");
        // ask for an int. if not int, ask again
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid index: ");
            scanner.next();
        }
        int SiteIndex = scanner.nextInt();
        return sitesList.get(SiteIndex);
    }

    public static String askForItem(Scanner scanner){
        System.out.println("Please enter the item name: ");
        String item = scanner.next();
        return item;
    }

    public static int askForQuantity(Scanner scanner){
        System.out.println("Please enter the quantity: ");
        // ask for an int. if not int, ask again
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid quantity. Please enter a valid quantity: ");
            scanner.next();
        }
        int quantity = scanner.nextInt();

        return quantity;
    }

    public static void printMenu() {
        System.out.println();
        System.out.println();
        System.out.println("Please enter the number of the option you would like to choose: ");
        System.out.println("1. Add a delivery stop");
        System.out.println("2. Remove a delivery stop");
        System.out.println("3. Execute a delivery");
        System.out.println("4. Exit");
        System.out.print("Your choice: ");
    }


}
