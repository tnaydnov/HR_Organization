package HR.BusinessLayer;

import HR.DataAccessLayer.ShiftDAO;
import HR_Deliveries_Interface.HRIntegrator;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;

public class ShiftController implements HRIntegrator {

    private final Map<String, List<Schedule>> stores_schedules;
    private List<String> stores;
    private final Map<String, List<Schedule>> schedules_history;
    private final ShiftDAO shiftDAO;
    private static ShiftController instance;

    // private constructor to prevent instantiation from outside
    private ShiftController() {
        stores_schedules = new HashMap<>();
        stores = new LinkedList<>();
        stores.add("drivers");
        schedules_history = new HashMap<>();
        shiftDAO = new ShiftDAO();
        load_data();
    }

    // public static method to get the instance of the singleton class
    public static ShiftController getInstance() {
        if (instance == null) {
            instance = new ShiftController();
        }
        return instance;
    }

    //Tests usage only
    public static void reset_instance() {
        instance = null;
    }

    public boolean store_exists(String store) {
        return stores.contains(store);
    }

    public String add_availability(Integer employee_id, LocalDate shift_date, ShiftType type, String store) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        if (stores_schedules.containsKey(store)) {
            Schedule schedule = get_schedule(store, shift_date);
            if (schedule != null) {
                return schedule.add_availability(employee_id, shift_date, type);
            }
        }
        return "No schedule available for that date";
    }

    public String remove_availability(Integer employee_id, LocalDate shift_date, ShiftType type, String store) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        if (stores_schedules.containsKey(store)) {
            Schedule schedule = get_schedule(store, shift_date);
            if (schedule != null) {
                return schedule.remove_availability(employee_id, shift_date, type);
            }
        }
        return "No schedule available for that date";
    }

    public String get_availability(Integer employee_id, List<String> certified_stores) {
        StringBuilder availability = new StringBuilder();
        for (String store: certified_stores) {
            if (stores_schedules.containsKey(store)) {
                availability.append(store).append(":\n");
                for (Schedule schedule : stores_schedules.get(store)) {
                    availability.append(schedule.get_availability(employee_id));
                }
            }
        }
        return availability.toString();
    }

    public String get_shifts(Integer employee_id, List<String> certified_stores) {
        StringBuilder shifts = new StringBuilder();
        for (String store: certified_stores) {
            if (stores_schedules.containsKey(store)) {
                shifts.append(store).append(":\n");
                for (Schedule schedule : stores_schedules.get(store)) {
                    shifts.append(schedule.get_shifts(employee_id));
                }
            }
        }
        return shifts.toString();

    }

    public List<ShiftPair> getAssignedShiftsDates(LocalDate week_start, Integer employee_id, List<String> certified_stores) {
        List<ShiftPair> shifts = new LinkedList<>();
        for (String store: certified_stores) {
            if (stores_schedules.containsKey(store)) {
                List<ShiftPair> store_shifts = get_schedule(store, week_start).getAssignedShiftsDates(employee_id);
                shifts.addAll(store_shifts);
            }
        }
        return shifts;

    }

    public String create_store(String store) {
        stores.add(store);
        schedules_history.put(store, new LinkedList<>());
        return "";
    }

    public String remove_store(String store) {
        if (store_exists(store)) {
            stores.remove(store);
            stores_schedules.remove(store);
            return "";
        }
        return "Store doesn't exists";
    }

    public String confirm_shift(LocalDate shift_date, ShiftType shift_type, String store) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        Schedule schedule = get_schedule(store, shift_date);
        if (schedule != null) {
            return schedule.confirm_shift(shift_date, shift_type);
        }
        return "Schedule doesn't exist";
    }

    public String create_weekly_schedule(LocalDate week_start_date, String store, LocalTime morning_start_time, LocalTime morning_end_time, LocalTime evening_start_time, LocalTime evening_end_time) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        Schedule schedule = new Schedule(store, week_start_date, morning_start_time, morning_end_time, evening_start_time, evening_end_time, shiftDAO);
        if (!stores_schedules.containsKey(store)) {
            stores_schedules.put(store, new LinkedList<>());
        }
        handle_schedules(store, schedule);
        return "";
    }

    private void handle_schedules(String store, Schedule schedule) {
        LocalDate date = LocalDate.now(); // or any other LocalDate object
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1); // week starts on Sunday
        int weekOfYear = date.get(weekFields.weekOfWeekBasedYear());
        if (!schedule.current_or_future_week(weekOfYear)) {
            if (schedules_history.containsKey(store)) {
                schedules_history.get(store).add(schedule);
            }
            else {
                schedules_history.put(store, new LinkedList<>());
                schedules_history.get(store).add(schedule);
            }
        }
        else {
            if (stores_schedules.containsKey(store)) {
                stores_schedules.get(store).add(schedule);
            }
            else {
                stores_schedules.put(store, new LinkedList<>());
                stores_schedules.get(store).add(schedule);
            }
        }
        for (Schedule a: stores_schedules.get(store)) {
            if (!a.current_or_future_week(weekOfYear)) {
                schedules_history.get(store).add(a);
                stores_schedules.get(store).remove(a);
            }
        }
    }

    private Schedule get_schedule(String store, LocalDate date) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1); // week starts on Sunday
        int weekOfYear = date.get(weekFields.weekOfWeekBasedYear());
        if (!stores_schedules.containsKey(store)) {
            return null;
        }
        for (Schedule schedule: stores_schedules.get(store)) {
            if (schedule.get_week() == weekOfYear) {
                return schedule;
            }
        }
        return null;
    }

    public String assign_to_shift(int employee_id, LocalDate shift_date, ShiftType shift_type, String store, JobType role) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        Schedule schedule = get_schedule(store, shift_date);
        if (schedule != null) {
            return schedule.assign_to_shift(employee_id, shift_date, shift_type, role);
        }
        return "No schedule available for that date";
    }

    public String shifts_limit(List<String> certified_stores, int employee_id, LocalDate shift_date) {
        int weekly_shifts_num = 0;
        for (String schedule_name: certified_stores) {
            Schedule schedule = get_schedule(schedule_name, shift_date);
            if (schedule != null) {
                int num = schedule.shifts_limit(employee_id, shift_date);
                if (num == -1) {
                    return "Employee is already assigned to a shift on this day. 2 shifts in a day isn't allowed.";
                }
                weekly_shifts_num = weekly_shifts_num + num;
            }
        }
        if (weekly_shifts_num >= 6) {
            return "Employee is already assigned to 6 shifts this week. 6 is the limit.";
        }
        return "";
    }

    public String remove_from_shift(int employee_id, LocalDate shift_date, ShiftType shift_type, String store, JobType role) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        Schedule schedule = get_schedule(store, shift_date);
        if (schedule != null) {
            return schedule.remove_from_shift(employee_id, shift_date, shift_type, role);
        }
        return "No schedule available for that date";
    }

    public String limit_employee(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        Schedule schedule = get_schedule(store, shift_date);
        if (schedule != null) {
            return schedule.limit_employee(employee_id, shift_date, shift_type);
        }
        return "No schedule available for that date";
    }

    public String remove_employee_limit(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        Schedule schedule = get_schedule(store, shift_date);
        if (schedule != null) {
            return schedule.remove_employee_limit(employee_id, shift_date, shift_type);
        }
        return "No schedule available for that date";
    }

    public List<Integer> show_shift_availability(LocalDate shift_date, ShiftType shift_type, String store) {
        Schedule schedule = get_schedule(store, shift_date);
        if (schedule != null) {
            return schedule.show_shift_availability(shift_date, shift_type);
        }
        return new LinkedList<>();
    }

    public boolean has_future_shifts(List<String> certified_stores, int employee_id) {
        for (String store: certified_stores) {
            if (stores_schedules.containsKey(store)) {
                for (Schedule schedule : stores_schedules.get(store)) {
                    if (schedule.has_future_shifts(LocalDate.now(), employee_id)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean has_future_shifts_role(List<String> certified_stores, JobType role, int id_num) {
        for (String store: certified_stores) {
            if (stores_schedules.containsKey(store)) {
                for (Schedule schedule : stores_schedules.get(store)) {
                    if (schedule.has_future_shifts_role(LocalDate.now(), role, id_num)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean has_future_shifts(String store, int id_num) {
        if(!stores_schedules.containsKey(store)) {
            return false;
        }
        for (Schedule schedule : stores_schedules.get(store)) {
            if (schedule.has_future_shifts(LocalDate.now(), id_num)) {
                return true;
            }
        }
        return false;
    }

    public double get_hours(LocalDate date_object, ShiftType shift_type, String store) {
        Schedule schedule = get_schedule(store, date_object);
        if (schedule != null) {
            return schedule.get_hours(date_object, shift_type);
        }
        return 0;
    }

    @Override
    public boolean checkStoreAvailability(String store, Timestamp arrivalTime) {
        LocalDateTime localDateTime = arrivalTime.toLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        Schedule schedule = get_schedule(store, localDate);
        if (schedule != null) {
            return schedule.check_availability(arrivalTime);
        }
        return false;
    }

    @Override
    public List<String> getAvailableDrivers(Timestamp startTime, Timestamp endTime) {
        LocalDateTime localDateTime = startTime.toLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        Schedule schedule = get_schedule("drivers", localDate);
        if (schedule != null) {
            return schedule.get_available_drivers(startTime, endTime);
        }
        return new LinkedList<>();
    }

    @Override
    public boolean assignDrivers(String driverId, Timestamp startTime, Timestamp endTime) {
        int id_num;
        try {id_num = Integer.parseInt(driverId);} catch (Exception exception) {return false;}
        LocalDateTime localDateTime = startTime.toLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        Schedule schedule = get_schedule("drivers", localDate);
        if (schedule != null) {
            return schedule.assign_drivers(id_num, startTime, endTime);
        }
        return false;
    }

    public void load_data() {
        this.stores = shiftDAO.get_stores();
        Map<ShiftPair, Shift> shifts = shiftDAO.get_shifts();
        if (shifts.isEmpty()) {
            return;
        }
        Map<Integer, Map<String, Map<ShiftPair, Shift>>> organized_shifts = organizeDatesIntoWeeks(shifts);
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1);
        LocalDate date = LocalDate.now();
        int current_week = date.get(weekFields.weekOfYear());
        for (Integer num: organized_shifts.keySet()) {
            Map<String, Map<ShiftPair, Shift>> week_schedules = organized_shifts.get(num);
            for (String store: week_schedules.keySet()) {
                Schedule schedule = new Schedule(store, week_schedules.get(store), shiftDAO);
                if (num >= current_week) {
                    if (stores_schedules.containsKey(store)) {
                        stores_schedules.get(store).add(schedule);
                    }
                    else {
                        stores_schedules.put(store, new LinkedList<>());
                        stores_schedules.get(store).add(schedule);
                    }
                }
                else {
                    if (!schedules_history.containsKey(store)) {
                        schedules_history.put(store, new LinkedList<>());
                        schedules_history.get(store).add(schedule);
                    }
                    else {
                        schedules_history.get(store).add(schedule);
                    }
                }
            }
        }
    }

    public static Map<Integer, Map<String, Map<ShiftPair, Shift>>> organizeDatesIntoWeeks(Map<ShiftPair, Shift> shifts) {
        Map<Integer, Map<String, Map<ShiftPair, Shift>>> weeksMap = new HashMap<>();
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1);
        for (ShiftPair date : shifts.keySet()) {
            String store = shifts.get(date).get_store();
            int weekNumber = date.getDate().get(weekFields.weekOfYear());
            if (weeksMap.containsKey(weekNumber)) {
                if (weeksMap.get(weekNumber).containsKey(store)) {
                    weeksMap.get(weekNumber).get(store).put(date, shifts.get(date));
                }
                else {
                    Map<ShiftPair, Shift> curr = new HashMap<>();
                    curr.put(date, shifts.get(date));
                    weeksMap.get(weekNumber).put(store, curr);
                }
            }
            else {
                weeksMap.put(weekNumber, new HashMap<>());
                if (weeksMap.get(weekNumber).containsKey(store)) {
                    weeksMap.get(weekNumber).get(store).put(date, shifts.get(date));
                }
                else {
                    Map<ShiftPair, Shift> curr = new HashMap<>();
                    curr.put(date, shifts.get(date));
                    weeksMap.get(weekNumber).put(store, curr);
                }
            }
        }
        return weeksMap;
    }

    public String cancel_product(int employee_id, int product_id, LocalDate shift_date, ShiftType shift_type, String store) {
        Schedule schedule = get_schedule(store, shift_date);
        if (schedule != null) {
            return schedule.cancel_product(employee_id, product_id, shift_date, shift_type);
        }
        return "There isn't a schedule for that date";
    }

    public String show_shift_assigned(LocalDate date_object, ShiftType shift_type, String store) {
        if (get_schedule(store, date_object) != null) {
            StringBuilder output = new StringBuilder();
            Map<JobType, List<Integer>> map = Objects.requireNonNull(get_schedule(store, date_object)).show_shift_assigned(date_object, shift_type);
            for (JobType job: map.keySet()) {
                output.append(job.toString()).append(" - ").append(map.get(job)).append("\n");
            }
            return output.toString();
        }
        return "";
    }

    public boolean is_limited(int id, LocalDate date_object, ShiftType shift_type, String store) {
        if (!store_exists(store)) {
            return false;
        }
        Schedule schedule = get_schedule(store, date_object);
        if (schedule != null) {
            return schedule.is_limited(id, date_object, shift_type);
        }
        return false;
    }

    private Schedule get_past_schedule(String store, LocalDate date) {
        if (!schedules_history.containsKey(store)) {
            return null;
        }
        for (Schedule schedule: schedules_history.get(store)) {
            if (schedule.current_or_future_week(date.get(WeekFields.ISO.weekOfYear()))) {
                return schedule;
            }
        }
        return null;
    }

    public boolean future_schedule_exists(LocalDate date_object, String store) {
        return get_schedule(store, date_object) != null;
    }

    public boolean past_schedule_exists(LocalDate date_object, String store) {
        return get_past_schedule(store, date_object) != null;
    }

    public String show_scheduled_deliveries(LocalDate shift_date, ShiftType shift_type, String store) {
        if (!store_exists(store)) {
            return "Store doesn't exists";
        }
        Schedule schedule = get_schedule(store, shift_date);
        if (schedule != null) {
            return schedule.show_scheduled_deliveries(shift_date, shift_type);
        }
        return "No schedule available for that date";
    }

    public List<ShiftPair> get_shifts_pairs(String store, LocalDate week_start) {
        Schedule schedule = get_schedule(store, week_start);
        assert schedule != null;
        return schedule.get_shifts_pairs();
    }

    public List<Integer> get_availables(String store, ShiftPair shift) {
        Schedule schedule = get_schedule(store, shift.getDate());
        assert schedule != null;
        return schedule.get_availables(shift);
    }
}
