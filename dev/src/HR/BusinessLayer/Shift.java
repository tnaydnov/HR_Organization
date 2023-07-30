package HR.BusinessLayer;

import Deliveries.BusinessLayer.DeliveryFormsController;
import HR_Deliveries_Interface.DeliveryIntegrator;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Shift {
    private final String store;
    private List<Integer> available_employees;
    private Map<JobType, List<Integer>> employees;
    private Map<Integer, Integer> product_canceling; //Employee, product_id
    private List<Integer> manager_constraints;
    private boolean confirmed;
    private final LocalTime start;
    private final LocalTime end;
    public Shift(String store, LocalTime start, LocalTime end) {
        available_employees = new LinkedList<>();
        employees = new HashMap<>();
        initialize_roles();
        product_canceling = new HashMap<>();
        this.start = start;
        this.end = end;
        confirmed = false;
        this.store = store;
        manager_constraints = new LinkedList<>();
    }

    public void initialize_roles() {
        for (JobType job: JobType.values()) {
            if (!employees.containsKey(job)) {
                employees.put(job, new LinkedList<>());
            }
        }
    }

    public String add_availability(Integer employee_id) {
        if (checkAvailability(employee_id)) {
            return "Already available on this shift";
        }
        if (checkManagerConstraint(employee_id)) {
            return "Can't work in this shift";
        }
        available_employees.add(employee_id);
        return "";
    }

    private boolean checkManagerConstraint(Integer employee_id) {
        return manager_constraints.contains(employee_id);
    }

    private boolean checkAvailability(Integer employee_id) {
        return available_employees.contains(employee_id);
    }


    public String remove_availability(Integer employee_id) {
        if (!available_employees.contains(employee_id)) {
            return "You're not available on this shift";
        }
        available_employees.remove(employee_id);
        return "";
    }

    public boolean is_available(Integer employee_id) {
        return available_employees.contains(employee_id);
    }

    public String is_assigned(Integer employee_id) {
        StringBuilder job = new StringBuilder();
        for (JobType role: employees.keySet()) {
            if (employees.get(role).contains(employee_id)) {
                job.append(role.toString());
            }
        }
        return job.toString();
    }

    public String confirm_shift() {
        if (employees.get(JobType.SHIFTMANAGER).isEmpty()) {
            return "There is no shift manager";
        }
        confirmed = true;
        return "";
    }

    public String assign_to_shift(int employee_id, JobType role) {
        if (!available_employees.contains(employee_id)) {
            return "Employee isn't available on this shift";
        }
        employees.get(role).add(employee_id);
        return "";
    }

    public String remove_from_shift(int employee_id, JobType role) {
        if (!employees.get(role).contains(employee_id)) {
            return "Employee isn't assigned to this shift";
        }
        employees.get(role).remove(Integer.valueOf(employee_id));
        return "";
    }

    public String limit_employee(int employee_id) {
        if (manager_constraints.contains(employee_id)) {
            return "User already limited on this shift";
        }
        manager_constraints.add(employee_id);
        return "";
    }

    public String remove_employee_limit(int employee_id) {
        if (!manager_constraints.contains(employee_id)) {
            return "User isn't limited on this shift";
        }
        manager_constraints.remove(Integer.valueOf(employee_id));
        return "";
    }

    public List<Integer> show_shift_availability() {
        return available_employees;
    }

    public double get_length() {
        Duration duration = Duration.between(start, end);
        return duration.toHours();
    }

    public LocalTime get_start() {
        return start;
    }

    public LocalTime get_end() {
        return end;
    }

    public boolean check_availability() {
        return !employees.get(JobType.STOREKEEPER).isEmpty();
    }

    public List<String> get_available_drivers() {
        List<String> drivers_ids = new LinkedList<>();
        for (Integer id: available_employees) {
            drivers_ids.add(id.toString());
        }
        return drivers_ids;
    }

    public boolean is_assigned_to_role(int employee_id, JobType role) {
        return employees.get(role).contains(employee_id);
    }

    public void set_available(List<Integer> employees) {
        available_employees = employees;
    }
    public void set_employees(Map<JobType, List<Integer>> employees) {
        this.employees = employees;
    }
    public void set_events(Map<Integer, Integer> events) {
        this.product_canceling = events;
    }
    public void set_constraints(List<Integer> constraints) {
        manager_constraints = constraints;
    }

    public String get_store() {
        return store;
    }

    public String cancel_product(int employee_id, int product_id) {
        product_canceling.put(employee_id, product_id);
        return "";
    }

    public boolean is_confirmed() {
        return confirmed;
    }

    public Map<JobType, List<Integer>> show_shift_assigned() {
        return employees;
    }

    public boolean is_limited(int employee_id) {
        return manager_constraints.contains(employee_id);
    }

    public String show_scheduled_deliveries(LocalDate shift_date) {
        DeliveryIntegrator object = DeliveryFormsController.getInstance();
        LocalDateTime start_localDateTime = LocalDateTime.of(shift_date, start);
        Timestamp start_timestamp = Timestamp.valueOf(start_localDateTime);
        LocalDateTime end_localDateTime = LocalDateTime.of(shift_date, end);
        Timestamp end_timestamp = Timestamp.valueOf(end_localDateTime);
        return object.getDeliveryByArrivalTime(start_timestamp, end_timestamp, store).toString();
    }

    public List<Integer> get_availables() {
        return available_employees;
    }
}
