package HR.BusinessLayer;

import HR.DataAccessLayer.EmployeeDAO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EmployeeController {
    private final Map<Integer, String> login_info; //<id, password>
    private final List<Integer> logged_in;
    private final Map<Integer, Employee> employees;
    private final EmployeeDAO employeeDAO;

    private static EmployeeController instance;

    // private constructor to prevent instantiation from outside
    private EmployeeController() {
        login_info = new HashMap<>();
        logged_in = new LinkedList<>();
        employees = new HashMap<>();
        employeeDAO = new EmployeeDAO();
        load_data();
    }

    // public static method to get the instance of the singleton class
    public static EmployeeController getInstance() {
        if (instance == null) {
            instance = new EmployeeController();
        }
        return instance;
    }

    //Tests usage only
    public static void reset_instance() {
        instance = null;
    }

    public String login(int employee_id, String password) {
        if (!login_info.containsKey(employee_id)) {
            return "User doesn't exists";
        }
        if (!login_info.get(employee_id).equals(password)) {
            return "Password is wrong";
        }
        if (is_loggedIn(employee_id)) {
            return "User already logged in";
        }
        logged_in.add(employee_id);
        return "";
    }

    public String logout(int employee_id) {
        if (!login_info.containsKey(employee_id)) {
            return "User doesn't exists";
        }
        if (!is_loggedIn(employee_id)) {
            return "User already logged out";
        }
        logged_in.remove(Integer.valueOf(employee_id));
        return "";
    }


    public boolean is_loggedIn(int employee_id) {
        return logged_in.contains(employee_id);
    }

    public List<String> get_certified_stores(Integer employee_id) {
        if (employees.containsKey(employee_id)) {
            return employees.get(employee_id).get_stores();
        }
        return new LinkedList<>();
    }

    public String add_employee(Integer hr_id, int employee_id, String name, int bank_account_num, double salary_num, String terms_of_employment, LocalDate date_object, FamilyStatus family_status, boolean student, String password) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (employees.containsKey(employee_id)) {
            return "Employee already exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        String res = employeeDAO.add_employee(employee_id, name, bank_account_num, salary_num, terms_of_employment, date_object, family_status, student, password);
        if (res.equals("")) {
            employees.put(employee_id, new Employee(employee_id, name, bank_account_num, salary_num, terms_of_employment, date_object, family_status, student, this.employeeDAO));
            login_info.put(employee_id, password);
            return res;
        }
        return res;
    }

    public boolean is_HRManager(int employee_id) {
        if (!employees.containsKey(employee_id)) {
            return false;
        }
        return employees.get(employee_id).is_HR();
    }

    public String remove_employee(Integer hr_id, int employee_id) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        String res = employeeDAO.remove_employee(employee_id);
        if (res.equals("")) {
            employees.remove(employee_id);
            login_info.remove(employee_id);
            return res;
        }
        return res;
    }

    public String certify_role(Integer hr_id, int employee_id, JobType role) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).certify_role(role);
    }

    public String remove_role(Integer hr_id, int employee_id, JobType job) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).remove_role(job);
    }

    public String assign_to_store(Integer hr_id, int employee_id, String store) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).assign_to_store(store);
    }

    public String remove_from_store(Integer hr_id, int employee_id, String store) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).remove_from_store(store);
    }

    public boolean is_certified_to_role(int employee_id, JobType role) {
        return employees.get(employee_id).is_certified_to_role(role);
    }

    public boolean is_certified_to_store(int employee_id, String store) {
        return employees.get(employee_id).is_certified_to_store(store);
    }

    public List<JobType> get_certified_roles(Integer employee_id) {
        if (employees.containsKey(employee_id)) {
            return employees.get(employee_id).get_roles();
        }
        return new LinkedList<>();
    }

    public String get_name(Integer employee_id) {
        return employees.get(employee_id).get_name();
    }

    public String change_name(Integer employee_id, String old_name, String new_name) {
        if (!is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).change_name(old_name, new_name);
    }

    public String change_bank_account(Integer employee_id, Integer old_bank_account, Integer new_bank_account) {
        if (!is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).change_bank_account(old_bank_account, new_bank_account);
    }

    public String change_family_status(Integer employee_id, FamilyStatus old_family_status, FamilyStatus new_family_status) {
        if (!is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).change_family_status(old_family_status, new_family_status);
    }

    public String change_student(Integer employee_id, boolean old_student_status, boolean new_student_status) {
        if (!is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).change_student(old_student_status, new_student_status);
    }

    public String change_employee_salary(Integer hr_id, Integer employee_id, double old_salary, double new_salary) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).change_employee_salary(old_salary, new_salary);
    }

    public String change_employee_terms(Integer hr_id, Integer employee_id, String new_terms) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).change_employee_terms(new_terms);
    }

    public String unassign_all_from_store(int hr_id, String store) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        for (Integer id: employees.keySet()) {
            if (employees.get(id).is_certified_to_store(store)) {
                employees.get(id).remove_from_store(store);
            }
        }
        return "";
    }

    public void add_hours_to_employee(int employee_id, double hours) {
        employees.get(employee_id).add_hours(hours);
    }

    public void remove_hours_from_employee(int employee_id, double hours) {
        employees.get(employee_id).remove_hours(hours);
    }

    public String confirm_monthly_salary(int hr_id, int employee_id, double bonus_num) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).confirm_monthly_salary(bonus_num);
    }

    public String show_personal_info(Integer employee_id) {
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).show_personal_info();
    }

    public String show_role_certifications(Integer employee_id) {
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).show_role_certifications();
    }

    public String show_assigned_stores(Integer employee_id) {
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).show_assigned_stores();
    }

    public String show_current_salary(Integer employee_id) {
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(employee_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).show_current_salary();
    }

    public void add_hr(Integer employee_id, String name, Integer bank_account, double salary, String terms_of_employment, LocalDate employment_date, FamilyStatus family_status, boolean is_student, String password) {
        employeeDAO.add_employee(employee_id, name, bank_account, salary, terms_of_employment, employment_date, family_status, is_student, password);
        Employee employee = new Employee(employee_id, name, bank_account, salary, terms_of_employment, employment_date, family_status, is_student, this.employeeDAO);
        employee.certify_role(JobType.HRMANAGER);
        employees.put(employee_id, employee);
        login_info.put(employee_id, password);
    }

    public String show_employees(int hr_id) {
        StringBuilder output = new StringBuilder();
        for (Integer id: employees.keySet()) {
            if (id != hr_id) {
                output.append(id).append(" - ").append(employees.get(id).get_name()).append("\n");
            }
        }
        return output.toString();
    }

    public String show_employee_info(Integer hr_id, int employee_id) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exist";
        }
        return employees.get(employee_id).show_personal_info();
    }

    public String load_data() {
        try {
            Map<Employee, String> employees = employeeDAO.load_Data();
            for (Employee employee: employees.keySet()) {
                this.employees.put(employee.get_id(), employee);
                login_info.put(employee.get_id(), employees.get(employee));
            }
            return "";
        }
        catch (Exception exception) {
            return "Restoring the employees went wrong!";
        }
    }

    public boolean is_ShiftManager(int employee_id) {
        return employees.get(employee_id).is_shift_manager();
    }


    //Tests functions:
    public boolean employee_exists(int employee_id) {
        return employees.containsKey(employee_id);
    }

    public boolean employee_logged_in(int employee_id) {
        return logged_in.contains(employee_id);
    }

    public double get_employee_salary(int employee_id) {
        return employees.get(employee_id).get_salary();
    }

    public String get_employee_name(int employee_id) {
        return employees.get(employee_id).get_name();
    }

    public int get_employee_bank_account(int employee_id) {
        return employees.get(employee_id).get_bank();
    }

    public FamilyStatus get_employee_family_status(int employee_id) {
        return employees.get(employee_id).get_family_status();
    }

    public boolean get_employee_student_status(int employee_id) {
        return employees.get(employee_id).get_student();
    }

    public String get_employee_terms(int employee_id) {
        return employees.get(employee_id).get_terms();
    }

    public boolean assigned_to_store(int employee_id, String store) {
        return employees.get(employee_id).is_certified_to_store(store);
    }

    public double get_monthly_salary(int manager_id, int employee_id) {
        if (!is_HRManager(manager_id)) {
            return 0;
        }
        if (!employees.containsKey(employee_id)) {
            return 0;
        }
        if (!is_loggedIn(employee_id)) {
            return 0;
        }
        return employees.get(employee_id).get_monthly_salary();
    }

    public boolean is_storekeeper(int employee_id) {
        if (employees.containsKey(employee_id)) {
            return employees.get(employee_id).is_certified_to_role(JobType.STOREKEEPER);
        }
        return false;
    }

    public String certify_driver(int hr_id, int employee_id, String phone, int maxWeight, boolean regularAllowed, boolean refrigeratedAllowed) {
        if (!is_HRManager(hr_id)) {
            return "User is not an HR manager";
        }
        if (!employees.containsKey(employee_id)) {
            return "Employee doesn't exists";
        }
        if (!is_loggedIn(hr_id)) {
            return "User not logged in currently";
        }
        return employees.get(employee_id).certify_driver(phone, maxWeight, regularAllowed, refrigeratedAllowed);
    }
}
