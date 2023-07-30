package Deliveries.BusinessLayer.Generators;

import Deliveries.BusinessLayer.DeliveryStop;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import Deliveries.BusinessLayer.Site;
import Deliveries.DataAccessLayer.DeliveryStopDAO;

import java.util.*;

public class DeliveryStopGenerator {
    private static final int MAX_ITEMS = 8; // Maximum number of items per delivery
    private static final int MAX_QUANTITY = 500; // Maximum quantity per item
    private static final int MAX_TRUCK_TYPE_ORDINAL = TruckType.values().length; // Maximum ordinal value of a truck type

    private static final String[] sampleGroceryItems = {"Apple", "Banana", "Bread", "Butter", "Cheese",
            "Eggs", "Milk", "Orange", "Potato", "Tomato", "Watermelon", "Yogurt", "Zucchini", "Pasta",
            "Rice", "Chicken", "Beef", "Pork", "Fish", "Shrimp", "Salmon", "Tuna", "Crab", "Lobster"};
    private final List<Site> sites;
    private final Random random;
    private List<DeliveryStop> pendingDeliveryStops;

    public DeliveryStopGenerator() {
        SiteGenerator siteGenerator = new SiteGenerator();
        sites = siteGenerator.getSitesList();
        this.random = new Random();
    }

    /**
     * Generates a list of delivery stops
     * @param size The number of delivery stops to be generated, must be smaller than the number of possible pairs of sites
     * @return A list of delivery stops
     */
    public List<DeliveryStop> getPendingDeliveryStops(int size) {
        assert size < sites.size() * sites.size()-1 : "Cannot generate more delivery stops than the number" +
                " of possible pairs of sites";
        // Warning: If called without a plausible solution - will cause an infinite loop
        // From the pigeonhole principle, will happen when trying to create
        // more delivery stops than the number of possible pairs of sites
        // The assertion above is to prevent this from happening (not sure if mathematically guaranteed to work)
        DeliveryStopDAO deliveryStopDAO = new DeliveryStopDAO();
        pendingDeliveryStops = new ArrayList<>(deliveryStopDAO.loadPendingStops());
        int nextDeliveryStopID = deliveryStopDAO.getMaxID() + 1;
        if (pendingDeliveryStops.size() < size) {
            for (int i = nextDeliveryStopID; i < nextDeliveryStopID + size; i++) {
                DeliveryStop deliveryStop = generateDeliveryStop(i);
                deliveryStopDAO.addStop(deliveryStop);
                pendingDeliveryStops.add(deliveryStop);
            }
        }
        return pendingDeliveryStops;
    }

    private DeliveryStop generateDeliveryStop(int shipmentInstanceID) {
        Set<Map.Entry<String, String>> existingStops = pendingDeliveryStops.stream().map(deliveryStop ->
                new AbstractMap.SimpleEntry<>(deliveryStop.getOrigin().getName(), deliveryStop.getDestination().getName()))
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        // add pending delivery stops to the set of existing stops
//        for (DeliveryStop deliveryStop : pendingDeliveryStops) {
//            existingStops.add(new AbstractMap.SimpleEntry<>(deliveryStop.getOrigin().getName(), deliveryStop.getDestination().getName()));
//        }

        Site origin;
        // Generate a site that is different from the origin
        Site destination;
        do {
            origin = sites.get(random.nextInt(sites.size()));
            destination = sites.get(random.nextInt(sites.size()));
            // The while checks that the destination is different from the origin
            // and that the pair of sites has not been generated before
            // currently inefficient, needs optimization
        } while (destination.equals(origin) || existingStops.contains(new AbstractMap.SimpleEntry<>(origin.getName(), destination.getName())));

        Map<String, Integer> items = generateItems();

        TruckType truckTypeRequired = TruckType.values()[random.nextInt(MAX_TRUCK_TYPE_ORDINAL)];

        return new DeliveryStop(shipmentInstanceID, items, origin, destination, truckTypeRequired);
    }


    private Map<String, Integer> generateItems() {
        Map<String, Integer> items = new HashMap<>();




        int numItems = Math.min(random.nextInt(MAX_ITEMS), sampleGroceryItems.length); // Generate a random number of items between 1 and MAX_ITEMS
        ArrayList<String> groceryNames = new ArrayList<>(Arrays.asList(sampleGroceryItems));
        Collections.shuffle(groceryNames, random);
        for (int i = 0; i < numItems; i++) {
            String itemName = groceryNames.get(i);
            int quantity = random.nextInt(MAX_QUANTITY);
            items.put(itemName, quantity);
        }


        return items;
    }


}