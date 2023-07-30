package HR.DataAccessLayer;

import HR.BusinessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftDAO {
    private final Connect conn = Connect.getInstance();
    public ShiftDAO() {}

    /**
     * A function that adds employee's availability to the database
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return "" / an error messaged if occurred
     */
    public String add_availability(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("INSERT INTO Availability (store, shiftType, day, month, year, employeeID, availabilityOrConstraint) VALUES(?,?,?,?,?,?,?)", store, shift_type.toString(), dayOfMonth, month, year, employee_id, 1);
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is already available in this date";
        }
    }

    /**
     * A function that removes employee's availability from the database
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return "" / an error messaged if occurred
     */
    public String remove_availability(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("DELETE FROM Availability WHERE employeeID = " + employee_id + " AND store = '" + store + "' AND day = '" + dayOfMonth + "' AND month = '" + month + "' AND year = '" + year + "' AND shiftType = '" + shift_type.toString() + "'");
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is not available in this date";
        }
    }

    /**
     * A function that adds a work limit of an employee in a shift to the database
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return "" / an error messaged if occurred
     */
    public String limit_employee(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("INSERT INTO Availability (store, shiftType, day, month, year, employeeID, availabilityOrConstraint) VALUES(?,?,?,?,?,?,?)", store, shift_type.toString(), dayOfMonth, month, year, employee_id, 0);
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is already assigned / limited in this date.";
        }
    }

    /**
     * A function that removes a work limit of an employee in a shift from the database
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return "" / an error messaged if occurred
     */
    public String remove_employee_limit(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("DELETE FROM Availability WHERE employeeID = " + employee_id + " AND store = '" + store + "' AND day = '" + dayOfMonth + "' AND month = '" + month + "' AND year = '" + year + "' AND shiftType = '" + shift_type.toString() + "'");
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is not available in this date";
        }
    }

    /**
     * A function that assigns an employee to a shift in the database
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return "" / an error messaged if occurred
     */
    public String assign_to_shift(int employee_id, LocalDate shift_date, ShiftType shift_type, JobType role, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("INSERT INTO EmployeesInShift (store, shiftType, day, month, year, employeeID, job) VALUES(?,?,?,?,?,?,?)", store, shift_type.toString(), dayOfMonth, month, year, employee_id, role.toString());
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is already assigned to this shift";
        }
    }

    /**
     * A function that remove an employee from a shift in the database
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return "" / an error messaged if occurred
     */
    public String remove_from_shift(int employee_id, LocalDate shift_date, ShiftType shift_type, JobType role, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("DELETE FROM EmployeesInShift WHERE employeeID = " + employee_id + " AND store = '" + store + "' AND day = '" + dayOfMonth + "' AND month = '" + month + "' AND year = '" + year + "' AND shiftType = '" + shift_type.toString() + "' AND job = '" + role.toString() + "'");
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is not assigned to this date";
        }
    }

    /**
     * A function that confirms a shift in the database
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return "" / an error messaged if occurred
     */
    public String confirm_shift(LocalDate shift_date, ShiftType shift_type, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("UPDATE Shifts SET confirmed = " + 1 + " WHERE store = '" + store + "' AND day = '" + dayOfMonth + "' AND month = '" + month + "' AND year = '" + year + "' AND shiftType = '" + shift_type.toString() + "'");
            return "";
        } catch (SQLException e) {
            return "Couldn't update the data base";
        }
    }

    /**
     * A function that creates a shift in the database
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     */
    public void create_shift(LocalDate shift_date, ShiftType shift_type, LocalTime start_time, LocalTime end_time, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("INSERT INTO Shifts (store, shiftType, day, month, year, start, end, confirmed) VALUES(?,?,?,?,?,?,?,?)", store, shift_type.toString(), dayOfMonth, month, year, start_time.toString(), end_time.toString(), 0);
        }
        catch (SQLException ignored) {
        }
    }

    public Map<ShiftPair, Shift> get_shifts() {
        try {
            Map<ShiftPair, Shift> shifts_objects = new HashMap<>();
            List<HashMap<String, Object>> Shifts = conn.executeQuery("SELECT * FROM Shifts");
            for (HashMap<String, Object> record: Shifts) {
                Map<ShiftPair, Shift> rec = shift_record(record);
                Map.Entry<ShiftPair, Shift> firstEntry = rec.entrySet().iterator().next();
                ShiftPair key = firstEntry.getKey();
                Shift value = firstEntry.getValue();
                shifts_objects.put(key, value);
            }
            return shifts_objects;
        }
        catch (SQLException e) {
            return null;
        }
    }

    private Map<ShiftPair, Shift> shift_record(HashMap<String, Object> personalDetails) {
        String store = (String) personalDetails.get("store");
        String shiftType = (String) personalDetails.get("shiftType");
        String day = (String) personalDetails.get("day");
        String month = (String) personalDetails.get("month");
        String year = (String) personalDetails.get("year");
        String start = (String) personalDetails.get("start");
        String end = (String) personalDetails.get("end");
        Integer confirmed = (Integer) personalDetails.get("confirmed");
        Shift shift = new Shift(store, LocalTime.parse(start), LocalTime.parse(end));
        if (confirmed == 1) {
            shift.confirm_shift();
        }
        List<Integer> available_employees = shift_available(store, shiftType, day, month, year);
        Map<JobType, List<Integer>> employees = shift_employees(store, shiftType, day, month, year);
        Map<Integer, Integer> events = shift_events(store, shiftType, day, month, year);
        List<Integer> manager_constraints = shift_manager_constraints(store, shiftType, day, month, year);
        shift.set_available(available_employees);
        shift.set_constraints(manager_constraints);
        shift.set_employees(employees);
        shift.initialize_roles();
        shift.set_events(events);
        LocalDate localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        ShiftPair pair = new ShiftPair(localDate, ShiftType.valueOf(shiftType));
        Map<ShiftPair, Shift> output = new HashMap<>();
        output.put(pair, shift);
        return output;
    }

    private List<Integer> shift_manager_constraints(String store, String shift_type, String day, String month, String year) {
        try {
            List<Integer> output = new LinkedList<>();
            List<HashMap<String, Object>> shift_manager_constraints = conn.executeQuery("SELECT * FROM Availability WHERE store = '" + store + "' AND day = '" + day + "' AND month = '" + month + "' AND year = '" + year + "' AND shiftType = '" + shift_type + "' AND availabilityOrConstraint = 0");
            for (HashMap<String, Object> record: shift_manager_constraints) {
                Integer id = (Integer) record.get("employeeID");
                output.add(id);
            }
            return output;
        }
        catch (SQLException e) {
            return null;
        }
    }

    private Map<Integer, Integer> shift_events(String store, String shift_type, String day, String month, String year) {
        try {
            Map<Integer, Integer> output = new HashMap<>();
            List<HashMap<String, Object>> shift_events = conn.executeQuery("SELECT * FROM ShiftEvents WHERE store = '" + store + "' AND day = '" + day + "' AND month = '" + month + "' AND year = '" + year + "' AND shiftType = '" + shift_type + "'");
            for (HashMap<String, Object> record: shift_events) {
                Integer id = (Integer) record.get("employeeId");
                Integer product_id = (Integer) record.get("productId");
                output.put(id, product_id);
            }
            return output;
        }
        catch (SQLException e) {
            return null;
        }
    }

    private Map<JobType, List<Integer>> shift_employees(String store, String shift_type, String day, String month, String year) {
        try {
            Map<JobType, List<Integer>> output = new HashMap<>();
            List<HashMap<String, Object>> shift_events = conn.executeQuery("SELECT * FROM EmployeesInShift WHERE store = '" + store + "' AND day = '" + day + "' AND month = '" + month + "' AND year = '" + year + "' AND shiftType = '" + shift_type + "'");
            for (HashMap<String, Object> record: shift_events) {
                String Job = (String) record.get("job");
                Integer employeeID = (Integer) record.get("employeeID");
                if (output.containsKey(JobType.valueOf(Job))) {
                    output.get(JobType.valueOf(Job)).add(employeeID);
                }
                else {
                    output.put(JobType.valueOf(Job), new LinkedList<>());
                    output.get(JobType.valueOf(Job)).add(employeeID);
                }
            }
            return output;
        }
        catch (SQLException e) {
            return null;
        }
    }


    private List<Integer> shift_available(String store, String shift_type, String day, String month, String year) {
        try {
            List<Integer> output = new LinkedList<>();
            List<HashMap<String, Object>> shift_manager_constraints = conn.executeQuery("SELECT * FROM Availability WHERE store = '" + store + "' AND day = '" + day + "' AND month = '" + month + "' AND year = '" + year + "' AND shiftType = '" + shift_type + "' AND availabilityOrConstraint = 1");
            for (HashMap<String, Object> record: shift_manager_constraints) {
                Integer id = (Integer) record.get("employeeID");
                output.add(id);
            }
            return output;
        }
        catch (SQLException e) {
            return null;
        }
    }


    public String cancel_product(int employee_id, int product_id, LocalDate shift_date, ShiftType shift_type, String store) {
        try{
            int dayOfMonth = shift_date.getDayOfMonth();
            int month = shift_date.getMonthValue();
            int year = shift_date.getYear();
            conn.executeUpdate("INSERT INTO ShiftEvents (store, shiftType, day, month, year, employeeId, productId) VALUES(?,?,?,?,?,?,?)", store, shift_type.toString(), dayOfMonth, month, year, employee_id, product_id);
            return "";
        } catch (SQLException e) {
            return "Couldn't update the database";
        }
    }

    public List<String> get_stores() {
        try {
            List<String> output = new LinkedList<>();
            List<HashMap<String, Object>> stores = conn.executeQuery("SELECT DISTINCT store FROM EmployeeStores");
            for (HashMap<String, Object> record: stores) {
                String store = (String) record.get("store");
                output.add(store);
            }
            return output;
        }
        catch (SQLException e) {
            return null;
        }
    }
}
