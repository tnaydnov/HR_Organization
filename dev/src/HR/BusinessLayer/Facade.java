package HR.BusinessLayer;

import Deliveries.BusinessLayer.DriverController;
import HR_Deliveries_Interface.DriverSaver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class Facade {

    private final EmployeeController employeeController;
    private final ShiftController shiftController;

    public Facade() {
        employeeController = EmployeeController.getInstance();
        shiftController = ShiftController.getInstance();
    }
    
    public String add_availability(Integer employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_loggedIn(employee_id)) {
            return "User not logged in";
        }
        return shiftController.add_availability(employee_id, shift_date, shift_type, store);
    }
    
    public String remove_availability(Integer employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_loggedIn(employee_id)) {
            return "User not logged in";
        }
        return shiftController.remove_availability(employee_id, shift_date, shift_type, store);
    }
    
    public String get_availability(Integer employee_id) {
        if (!employeeController.is_loggedIn(employee_id)) {
            return "User not logged in";
        }
        List<String> certified_stores = employeeController.get_certified_stores(employee_id);
        return shiftController.get_availability(employee_id, certified_stores);
    }
    
    public String get_shifts(Integer employee_id) {
        if (!employeeController.is_loggedIn(employee_id)) {
            return "User not logged in";
        }
        List<String> certified_stores = employeeController.get_certified_stores(employee_id);
        return shiftController.get_shifts(employee_id, certified_stores);
    }


    public List<ShiftPair> getAssignedShiftsDates(Integer employee_id, LocalDate week_start) {
        List<String> certified_stores = employeeController.get_certified_stores(employee_id);
        return shiftController.getAssignedShiftsDates(week_start, employee_id, certified_stores);
    }
    
    public String login(int employee_id, String password) {
        return employeeController.login(employee_id, password);
    }
    
    public String logout(int employee_id) {
        return employeeController.logout(employee_id);
    }
    
    public String add_employee(Integer hr_id, int employee_id, String name, int bank_account_num, double salary_num, String terms_of_employment, LocalDate shift_date, FamilyStatus family_status, boolean student, String password) {
        return employeeController.add_employee(hr_id, employee_id, name, bank_account_num, salary_num, terms_of_employment, shift_date, family_status, student, password);
    }
    
    public String remove_employee(Integer hr_id, int employee_id) {
        if (!employeeController.employee_exists(employee_id)) {
            return "Employee doesn't exist";
        }
        List<String> certified_stores = employeeController.get_certified_stores(employee_id);
        if (shiftController.has_future_shifts(certified_stores, employee_id)) {
            return "Employee is assigned to future shifts and can't be deleted.";
        }
        return employeeController.remove_employee(hr_id, employee_id);
    }
    
    public String certify_role(Integer hr_id, int employee_id, JobType role) {
        return employeeController.certify_role(hr_id, employee_id, role);
    }
    
    public String remove_role(Integer hr_id, int employee_id, JobType job) {
        if (!employeeController.employee_exists(employee_id)) {
            return "Employee doesn't exist";
        }
        List<String> certified_stores = employeeController.get_certified_stores(hr_id);
        if (shiftController.has_future_shifts_role(certified_stores, job, employee_id)) {
            return "Employee is assigned to future shifts in this store and can't be deleted.";
        }
        return employeeController.remove_role(hr_id, employee_id, job);
    }
    
    public String assign_to_store(Integer hr_id, int employee_id, String store) {
        if (!shiftController.store_exists(store)) {
            return "Store doesn't exists";
        }
        return employeeController.assign_to_store(hr_id, employee_id, store);
    }
    
    public String remove_from_store(Integer hr_id, int employee_id, String store) {
        if (!shiftController.store_exists(store)) {
            return "Store doesn't exists";
        }
        if (shiftController.has_future_shifts(store, employee_id)) {
            return "Employee is assigned to future shifts in this store and can't be deleted.";
        }
        return employeeController.remove_from_store(hr_id, employee_id, store);
    }
    
    public String create_store(Integer hr_id, String store) {
        if (shiftController.store_exists(store)) {
            return "Store already exists";
        }
        String res = employeeController.assign_to_store(hr_id, hr_id, store);
        if (!res.equals("")) {
            return res;
        }
        return shiftController.create_store(store);
    }
    
    public String remove_store(Integer hr_id, String store) {
        String res = employeeController.unassign_all_from_store(hr_id, store);
        if (!res.equals("")) {
            return res;
        }
        return shiftController.remove_store(store);
    }

    public String confirm_shift(Integer hr_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return shiftController.confirm_shift(shift_date, shift_type, store);

    }
    
    public String create_weekly_schedule(Integer hr_id, LocalDate week_start_date, String store, LocalTime morning_start_time, LocalTime morning_end_time, LocalTime evening_start_time, LocalTime evening_end_time) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return shiftController.create_weekly_schedule(week_start_date, store, morning_start_time, morning_end_time, evening_start_time, evening_end_time);
    }
    
    public String assign_to_shift(Integer hr_id, int employee_id, LocalDate shift_date, ShiftType shift_type, String store, JobType role) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        if (!employeeController.employee_exists(employee_id)) {
            return "Employee doesn't exist";
        }
        if (!employeeController.is_certified_to_role(employee_id, role)) {
            return "User is not certified to this role";
        }
        if (!employeeController.is_certified_to_store(employee_id, store)) {
            return "User is not assigned to this store";
        }
        List<String> certified_stores = employeeController.get_certified_stores(employee_id);
        String res = shiftController.shifts_limit(certified_stores, employee_id, shift_date);
        if(!res.equals("")) {
            return res;
        }
        employeeController.add_hours_to_employee(employee_id, get_hours(shift_date, shift_type, store));
        return shiftController.assign_to_shift(employee_id, shift_date, shift_type, store, role);
    }
    
    public double get_hours(LocalDate shift_date, ShiftType shift_type, String store) {
        return shiftController.get_hours(shift_date, shift_type, store);
    }
    
    public String remove_from_shift(Integer hr_id, int employee_id, LocalDate shift_date, ShiftType shift_type, String store, JobType role) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        employeeController.remove_hours_from_employee(employee_id, get_hours(shift_date, shift_type, store));
        return shiftController.remove_from_shift(employee_id, shift_date, shift_type, store, role);
    }
    
    public String limit_employee(Integer hr_id, int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        if (!employeeController.is_certified_to_store(employee_id, store)) {
            return "User is not assigned to this store";
        }
        return shiftController.limit_employee(employee_id, shift_date, shift_type, store);
    }
    
    public String remove_employee_limit(Integer hr_id, int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        if (!employeeController.is_certified_to_store(employee_id, store)) {
            return "User is not assigned to this store";
        }
        return shiftController.remove_employee_limit(employee_id, shift_date, shift_type, store);
    }
    
    public String show_shift_availability(Integer hr_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        if (!shiftController.store_exists(store)) {
            return "Store doesn't exists";
        }
        StringBuilder output = new StringBuilder();
        List<Integer> employees =  shiftController.show_shift_availability(shift_date, shift_type, store);
        for (Integer employee: employees) {
            output.append(employeeController.get_name(employee)).append(", ").append(employee).append(" - ");
            for (JobType role: employeeController.get_certified_roles(employee)) {
                output.append(role.toString()).append(", ");
            }
            output = new StringBuilder(output.substring(0, output.length() - 2));
        }
        return output.toString();
    }
    
    public String change_name(Integer employee_id, String old_name, String new_name) {
        return employeeController.change_name(employee_id, old_name, new_name);
    }
    
    public String change_bank_account(Integer employee_id, Integer old_bank_account, Integer new_bank_account) {
        return employeeController.change_bank_account(employee_id, old_bank_account, new_bank_account);
    }
    
    public String change_family_status(Integer employee_id, FamilyStatus old_family_status, FamilyStatus new_family_status) {
        return employeeController.change_family_status(employee_id, old_family_status, new_family_status);
    }
    
    public String change_student(Integer employee_id, boolean old_student_status, boolean new_student_status) {
        return employeeController.change_student(employee_id, old_student_status, new_student_status);
    }
    
    public String change_employee_salary(Integer hr_id, Integer employee_id, double old_salary, double new_salary) {
        return employeeController.change_employee_salary(hr_id, employee_id, old_salary, new_salary);
    }
    
    public String change_employee_terms(Integer hr_id, Integer employee_id, String new_terms) {
        return employeeController.change_employee_terms(hr_id, employee_id, new_terms);
    }
    
    public String confirm_monthly_salary(Integer hr_id, int employee_id, double bonus_num) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employeeController.confirm_monthly_salary(hr_id, employee_id, bonus_num);
    }

    public boolean is_hr(int employee_id) {
        return employeeController.is_HRManager(employee_id);
    }

    public String show_personal_info(Integer employee_id) {
        return employeeController.show_personal_info(employee_id);
    }

    public String show_role_certifications(Integer employee_id) {
        return employeeController.show_role_certifications(employee_id);
    }

    public String show_assigned_stores(Integer employee_id) {
        return employeeController.show_assigned_stores(employee_id);
    }

    public String show_current_salary(Integer employee_id) {
        return employeeController.show_current_salary(employee_id);
    }

    public void add_hr(Integer hr_id, String name, Integer bank_account, double salary, String terms_of_employment, LocalDate employment_date, FamilyStatus family_status, boolean is_student, String password) {
        employeeController.add_hr(hr_id, name, bank_account, salary, terms_of_employment, employment_date, family_status, is_student, password);
    }

    public String show_employees(Integer hr_id) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employeeController.show_employees(hr_id);
    }

    public String show_employee_info(Integer hr_id, int employee_id) {
        return employeeController.show_employee_info(hr_id, employee_id);
    }

    public String cancel_product(int employee_id, int product_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_ShiftManager(employee_id)) {
            return "User is not a shift manager and can't cancel a product";
        }
        if (!employeeController.is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return shiftController.cancel_product(employee_id, product_id, shift_date, shift_type, store);
    }

    public String show_shift_assigned(int hr_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employeeController.is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        if (!shiftController.store_exists(store)) {
            return "Store doesn't exists";
        }
        return shiftController.show_shift_assigned(shift_date, shift_type, store);
    }

    //Tests functions:
    public boolean employee_exists(int employee_id) {
        return employeeController.employee_exists(employee_id);
    }

    public boolean employee_logged_in(int employee_id) {
        return employeeController.employee_logged_in(employee_id);
    }

    public boolean store_exists(String store) {
        return shiftController.store_exists(store);
    }

    public double get_employee_salary(int employee_id) {
        return employeeController.get_employee_salary(employee_id);
    }

    public String get_employee_name(int employee_id) {
        return employeeController.get_employee_name(employee_id);
    }

    public int get_employee_bank_account(int employee_id) {
        return employeeController.get_employee_bank_account(employee_id);
    }

    public FamilyStatus get_employee_family_status(int employee_id) {
        return employeeController.get_employee_family_status(employee_id);
    }

    public boolean get_employee_student_status(int employee_id) {
        return employeeController.get_employee_student_status(employee_id);
    }

    public String get_employee_terms(int employee_id) {
        return employeeController.get_employee_terms(employee_id);
    }

    public boolean assigned_to_store(int employee_id, String store) {
        return employeeController.assigned_to_store(employee_id, store);
    }

    public boolean certified_to_role(int employee_id, JobType role) {
        return employeeController.is_certified_to_role(employee_id, role);
    }

    public boolean is_limited(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        return shiftController.is_limited(employee_id, shift_date, shift_type, store);
    }

    public boolean future_schedule_exists(LocalDate shift_date, String store) {
        return shiftController.future_schedule_exists(shift_date, store);
    }

    public boolean past_schedule_exists(LocalDate shift_date, String store) {
        return shiftController.past_schedule_exists(shift_date, store);
    }

    public String show_scheduled_deliveries(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        if (!employeeController.is_ShiftManager(employee_id) && employeeController.is_storekeeper(employee_id)) {
            return "Employee is not a shift manager or a storekeeper";
        }
        return shiftController.show_scheduled_deliveries(shift_date, shift_type, store);
    }

    public String certify_driver(int hr_id, int employee_id, String phone, int maxWeight, boolean regularAllowed, boolean refrigeratedAllowed) {
        return employeeController.certify_driver(hr_id, employee_id, phone, maxWeight, regularAllowed, refrigeratedAllowed);
    }

    public List<String> getJobs(int id) {
        List<JobType> jobs = employeeController.get_certified_roles(id);
        List<String> output = new LinkedList<>();
        for (JobType job: jobs) {
            output.add(job.toString());
        }
        return output;
    }
}
