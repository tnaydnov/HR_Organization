package Deliveries.DataAccessLayer;

import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import Deliveries.BusinessLayer.Truck;
import HR.DataAccessLayer.Connect;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TruckDAO {
    private final Connect conn;

    public TruckDAO() {
        conn = Connect.getInstance();
    }

    public Set<Truck> loadData(){
        Set<Truck> trucks = new HashSet<>();
        try {
            List<HashMap<String, Object>> truckDetails = conn.executeQuery("SELECT * FROM Trucks");
            for (HashMap<String, Object> truckRecord: truckDetails) {
                Truck truck = getTruck(truckRecord);
                trucks.add(truck);
            }
            return trucks;
        }
        catch (SQLException exception) {
            return null;
        }
    }


    public boolean addTruck(Truck truck) {
        String licensePlate = truck.getLicensePlate();
        int maxWeightTons = truck.getMaxWeightTons();
        String truckType = truck.getType().toString().toLowerCase();
        String model = truck.getModel();
        int netWeightTons = truck.getNetWeightTons();
        String query = "INSERT INTO Trucks (license_plate, max_weight_tons, truck_type, model, net_weight_tons)" +
                " VALUES ('" + licensePlate + "', '" + maxWeightTons + "', '" + truckType+ "', '" + model + "', '" + netWeightTons + "');";
        try {
            conn.executeUpdate(query);
            return true;
        }
        catch (SQLException exception) {
            return false;
        }
    }

    private Truck getTruck(HashMap<String, Object> truckRecord) {
        String licensePlate = (String) truckRecord.get("license_plate");
        int maxWeightTons = (Integer) truckRecord.get("max_weight_tons");
        String model = (String) truckRecord.get("model");
        String truckTypeString = (String) truckRecord.get("truck_type");
        TruckType truckType = TruckType.valueOf(truckTypeString.toUpperCase());
        int netWeightTons = (Integer) truckRecord.get("net_weight_tons");
        return new Truck(model,licensePlate,truckType,maxWeightTons,netWeightTons);
    }

}
