package Deliveries.BusinessLayer;

import Deliveries.BusinessLayer.Enums_and_Interfaces.Availability;
import Deliveries.BusinessLayer.Enums_and_Interfaces.DeliveryException;
import Deliveries.DataAccessLayer.DriverDAO;
import HR.BusinessLayer.ShiftController;
import HR_Deliveries_Interface.DriverSaver;
import HR_Deliveries_Interface.HRIntegrator;

import java.sql.Timestamp;
import java.util.*;

public class DriverController implements DriverSaver {
    private final HashSet<Driver> drivers;
    private HRIntegrator hrManager; //
    private static DriverController instance = null;
    private final DriverDAO driverDAO;
    private boolean TEST_ENVIRONMENT = false;


    // Singleton Constructor
    private DriverController() {
        driverDAO = new DriverDAO();
        drivers = new HashSet<>();
        drivers.addAll(driverDAO.loadData());
        // generateFleet(40);
        hrManager = ShiftController.getInstance();
    }

    public void generateFleet(int numberOfDrivers) {
        Set<String> usedIds = new HashSet<>();
        Random random = new Random();

        for (int i = 0; i < numberOfDrivers; i++) {
            // Generate random driver data
            String name = "Driver " + i;
            String phone = "555-555-" + String.format("%04d", i);

            // Generate unique driver ID
            String id;
            do {
                id = String.format("%09d", random.nextInt(1000000000));
            } while (usedIds.contains(id) || id.length() != 9);
            usedIds.add(id);

            // Create a new driver object and set availability
            Driver driver = new Driver(name, id, phone,
                    new License(random.nextInt(30),
                    random.nextInt(2), random.nextInt(2)));
            driver.setAvailability(Availability.Available);

            // Add the driver to the DriverController instance
            addDriver(driver);

        }
    }

    private void addDriver(Driver driver) {
        drivers.add(driver);
        driverDAO.addDriver(driver);
    }

    public static DriverController getInstance() {
        if (instance == null) {
            instance = new DriverController();
        }
        return instance;
    }

    public Driver pickDriver(Truck truck, Timestamp startTime, Timestamp finishTime) throws DeliveryException {
        List<String> availableDrivers = hrManager.getAvailableDrivers(startTime, finishTime);
        if (availableDrivers.isEmpty()) {
            throw new DeliveryException("No available drivers during the requested time");
        }
        for (Driver driver : drivers) {
            if (isAvailable(availableDrivers, driver) && driver.isLicensed(truck)) {
                assignDriver(startTime, finishTime, driver);
                return driver;
            }
        }
        throw new DeliveryException("No available drivers with license for truck " + truck);
    }

    private void assignDriver(Timestamp startTime, Timestamp finishTime, Driver driver) throws DeliveryException {
        driver.setAvailability(Availability.Busy);
        notifyHR(startTime, finishTime, driver);
    }

    private boolean isAvailable(List<String> availableDrivers, Driver driver) {
        return driver.isAvailable() &&
                // This 'double check' is not redundant -
                // it checks whether the driver is available both in terms of HR and deliveries.
                availableDrivers.contains(driver.getId());
    }

    /*
    * Notifies HR module.
    * Uses a boolean to check if the driver was assigned successfully,
    * and throws an exception if not.
    *  */
    private void notifyHR(Timestamp startTime, Timestamp finishTime, Driver driver) throws DeliveryException {
        if (!hrManager.assignDrivers(driver.getId(), startTime, finishTime)){
            throw new DeliveryException("Driver " + driver.getId() + " was not assigned successfully");
        }

    }

    public List<String> getDriverIds() {
        List<String> driverIds = new ArrayList<>();
        for (Driver driver : drivers) {
            driverIds.add(driver.getId());
        }
        return driverIds;
    }

    /**
     * should only be used for testing, to set a mock HRManager.
     */
    public void setHrManager(HRIntegrator hrManager) throws Exception {
        if (!TEST_ENVIRONMENT) {
            throw new Exception("This method should only be used for testing");
        }
        this.hrManager = hrManager;
    }

    /**
     * Should only be used for testing.
     * This method is used to ensure certain methods are called only for the purpose of testing.
     */
    public void setTestEnvironment() {
        TEST_ENVIRONMENT = true;
    }


    /**
     * @param name - driver's name
     * @param id - driver's id
     * @param phone - driver's phone number
     * @param maxWeight - weight allowed in the driver's license
     * @param regularAllowed - whether the driver is allowed to drive regular trucks
     * @param refrigeratedAllowed - whether the driver is allowed to drive refrigerated trucks
     * @return true if the driver was added successfully to the DB, false otherwise
     */
    @Override
    public boolean AddDriverToSystem(String name, String id, String phone, int maxWeight,
                                     boolean regularAllowed, boolean refrigeratedAllowed) {
        Driver driver = new Driver(name, id, phone,
                new License(maxWeight, regularAllowed?1:0, refrigeratedAllowed?1:0));
        if (driverDAO.addDriver(driver)) {
            drivers.add(driver);
            return true;
        }
        return false;
    }

    @Override
    public boolean DeleteDriverFromSystem(String id) {
        if (driverDAO.deleteDriver(id)){
            drivers.removeIf(driver -> driver.getId().equals(id));
            return true;
        }
        return false;
    }

}

