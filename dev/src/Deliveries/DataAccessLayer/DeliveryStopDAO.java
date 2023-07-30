package Deliveries.DataAccessLayer;

import Deliveries.BusinessLayer.DeliveryStop;
import Deliveries.BusinessLayer.Enums_and_Interfaces.DeliveryStatus;
import Deliveries.BusinessLayer.Enums_and_Interfaces.TruckType;
import Deliveries.BusinessLayer.Site;
import HR.DataAccessLayer.Connect;

import java.sql.SQLException;
import java.util.*;

public class DeliveryStopDAO {
    private final Connect conn;
    public DeliveryStopDAO() {
        conn = Connect.getInstance();
    }

    public List<DeliveryStop> loadPendingStops() {
        List<DeliveryStop> stops = new ArrayList<>();
        try {
            List<HashMap<String, Object>> stopDetails = conn.executeQuery("SELECT * FROM DeliveryStops " +
                    "WHERE status = 'NOT_STARTED';");
            for (HashMap<String, Object> stopRecord : stopDetails) {
                DeliveryStop stop = getStop(stopRecord);
                stops.add(stop);
            }
            return stops;
        } catch (SQLException exception) {
            return null;
        }
    }


    public boolean addStop(DeliveryStop stop)  {
        int stopID = stop.getShipmentInstanceID();
        String origin = stop.getOrigin().getName();
        String destination = stop.getDestination().getName();
        String truckType = stop.getTruckTypeRequired().toString();
        String status = stop.getStatus().toString();
//        int formID = stop.getFormID();
        //items
        Map<String, Integer> items = stop.getItems();
        String query1 = "INSERT INTO DeliveryStops (stop_id, origin_name, destination_name, truck_type, status)" +
                " VALUES ('" + stopID + "', '" + origin + "', '" + destination+ "', '" + truckType + "', '" + status + "');" ;
//        String query2 = "INSERT INTO Items (form_id, item_name, quantity)" +
//                " VALUES ('" + formID + "', '" + itemName + "', '" + quantity+  "');";
        try {
            conn.executeUpdate(query1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try { // TODO: Avoid generating items if they already exist - This doesn't prevent the stop creation as the stop is already created in an earlier try-catch. Can be marked as a low priority issue.
           for(Map.Entry<String,Integer> item : items.entrySet()) {
               String itemName = item.getKey();
               int quantity = item.getValue();
               String query2 = "INSERT INTO Items (stop_id, item_name, quantity)" +
                       " VALUES ('" + stop.getShipmentInstanceID() + "', '" + itemName + "', '" + quantity + "');";
               conn.executeUpdate(query2);
           }

            return true;
        }
        catch (SQLException exception) {
            return false;
        }
    }

    public boolean updateStatus(int stopId, DeliveryStatus deliveryStatus) {
        String status = deliveryStatus == DeliveryStatus.DELIVERED ? "DELIVERED" : "NOT_STARTED";
        String query = "UPDATE DeliveryStops SET status = '" + status + "' WHERE stop_id = '" + stopId + "';";
        try {
            conn.executeUpdate(query);
            return true;
        }
        catch (SQLException exception) {
            return false;
        }
    }

    public boolean updateFormID(int stopId, int formID) {
        String query = "UPDATE DeliveryStops SET form_id = '" + formID + "' WHERE stop_id = '" + stopId + "';";
        try {
            conn.executeUpdate(query);
            return true;
        }
        catch (SQLException exception) {
            return false;
        }
    }

    public DeliveryStop getStop(HashMap<String, Object> deliveryStopRecord) {
            int stopID = (Integer) deliveryStopRecord.get("stop_id");
            String origin = (String) deliveryStopRecord.get("origin_name");
            String destination = (String) deliveryStopRecord.get("destination_name");
            String truckTypeString = (String) deliveryStopRecord.get("truck_type");
            TruckType truckType = TruckType.valueOf(truckTypeString.toUpperCase());

            Site originSite = getSiteByName(origin);
            Site destinationSite = getSiteByName(destination);
            HashMap<String, Integer> items = new HashMap<>();
        try {
            List<HashMap<String, Object>> itemDetails = conn.executeQuery("SELECT * FROM Items Where stop_id =" + stopID );
            for (HashMap<String, Object> itemRecord : itemDetails) {
                String itemName =  (String) itemRecord.get("item_name");
                int quantity = (Integer) itemRecord.get("quantity");
                items.put(itemName,quantity);
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
            return new DeliveryStop(stopID, items,originSite,destinationSite,truckType);
        }

    public Site getSiteByName(String siteName){
        try {
            List<HashMap<String, Object>> siteDetails = conn.executeQuery("SELECT * FROM Sites Where name = '" + siteName + "';" );
            return SiteDAO.getSite(siteDetails.get(0));
        }
        catch (SQLException exception) {
            return null;
        }
    }


    public int getCount() {
        try {
            List<HashMap<String, Object>> count = conn.executeQuery("SELECT COUNT(*) FROM DeliveryStops;");
            return (Integer) count.get(0).get("COUNT(*)");
        } catch (SQLException exception) {
            return 0;
        }

    }

    public int getMaxID() {
        try {
            List<HashMap<String, Object>> maxID = conn.executeQuery("SELECT MAX(stop_id) FROM DeliveryStops;");
            return (Integer) maxID.get(0).get("MAX(stop_id)");
        } catch (Exception exception) {
            return 0;
        }
    }

    public void deleteStop(int deliveryId) {
        String query = "DELETE FROM DeliveryStops WHERE stop_id = '" + deliveryId + "';";
        try {
            conn.executeUpdate(query);
        }
        catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}

