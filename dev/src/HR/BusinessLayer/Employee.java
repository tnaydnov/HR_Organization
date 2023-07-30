package HR.BusinessLayer;

import Deliveries.BusinessLayer.DriverController;
import HR.DataAccessLayer.EmployeeDAO;
import HR_Deliveries_Interface.DriverSaver;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Employee {
    private final Integer id;
    private String name;
    private Integer bank_account;
    private double salary;
    private String terms_of_employment;
    private final LocalDate employment_date;
    private FamilyStatus family_status;
    private boolean is_student;
    private final List<JobType> roles;
    private final List<String> certified_stores;
    private final EmployeeDAO employeeDAO;
    private double current_total_salary;
    private double monthly_salary;

    public Employee(Integer id, String name, Integer bank_account, double salary, String terms_of_employment, LocalDate employment_date, FamilyStatus family_status, boolean is_student, EmployeeDAO employeeDAO) {
        this.id = id;
        this.name = name;
        this.bank_account = bank_account;
        this.salary = salary;
        this.terms_of_employment = terms_of_employment;
        this.employment_date = employment_date;
        this.family_status = family_status;
        this.is_student = is_student;
        this.employeeDAO = employeeDAO;
        this.roles = new LinkedList<>();
        this.certified_stores = new LinkedList<>();
        this.current_total_salary = 0;
        this.monthly_salary = 0;
    }

    public List<String> get_stores() {
        return certified_stores;
    }

    public boolean is_HR() {
        return roles.contains(JobType.HRMANAGER);
    }

    public String certify_role(JobType role) {
        if (roles.contains(role)) {
            return "Employee already certified to this role";
        }
        String res = employeeDAO.certify_role(this.id, role.toString());
        if (res.equals("")) {
            roles.add(role);
            return res;
        }
        return res;
    }

    public String remove_role(JobType role) {
        if (roles.contains(role)) {
            String res = employeeDAO.remove_role(this.id, role.toString());
            if (res.equals("")) {
                if (role.equals(JobType.DRIVER)) {
                    DriverSaver object = DriverController.getInstance();
                    object.DeleteDriverFromSystem(this.id.toString());
                }
                roles.remove(role);
                return res;
            }
            return res;
        }
        return "Employee isn't certified to this role";
    }

    public String assign_to_store(String store) {
        if (certified_stores.contains(store)) {
            return "Already assigned to this store";
        }
        String res = employeeDAO.assign_to_store(this.id, store);
        if (res.equals("")) {
            certified_stores.add(store);
            return res;
        }
        return "";
    }

    public String remove_from_store(String store) {
        if (certified_stores.contains(store)) {
            String res = employeeDAO.remove_from_store(this.id, store);
            if (res.equals("")) {
                certified_stores.remove(store);
                return res;
            }
            return res;
        }
        return "Already not assigned to this store";
    }

    public boolean is_certified_to_role(JobType role) {
        return roles.contains(role);
    }

    public boolean is_certified_to_store(String store) {
        return certified_stores.contains(store);
    }

    public String get_name() {
        return name;
    }

    public List<JobType> get_roles() {
        return roles;
    }

    public String change_name(String old_name, String new_name) {
        if (!this.name.equalsIgnoreCase(old_name)) {
            return "Old value isn't right";
        }
        String res = employeeDAO.change_name(this.id, new_name);
        if (res.equals("")) {
            this.name = new_name;
            return res;
        }
        return res;
    }

    public String change_bank_account(Integer old_bank_account, Integer new_bank_account) {
        if (!Objects.equals(this.bank_account, old_bank_account)) {
            return "Old value isn't right";
        }
        String res = employeeDAO.change_bank_account(this.id, new_bank_account);
        if (res.equals("")) {
            this.bank_account = new_bank_account;
            return res;
        }
        return res;
    }

    public String change_family_status(FamilyStatus old_family_status, FamilyStatus new_family_status) {
        if (this.family_status != old_family_status) {
            return "Old value isn't right";
        }
        String res = employeeDAO.change_family_status(this.id, new_family_status);
        if (res.equals("")) {
            this.family_status = new_family_status;
            return res;
        }
        return res;
    }

    public String change_student(boolean old_student_status, boolean new_student_status) {
        if (this.is_student != old_student_status) {
            return "Old value isn't right";
        }
        String res = employeeDAO.change_student(this.id, new_student_status);
        if (res.equals("")) {
            this.is_student = new_student_status;
            return res;
        }
        return res;
    }

    public String change_employee_salary(double old_salary, double new_salary) {
        if (this.salary != old_salary) {
            return "Old value isn't right";
        }
        String res = employeeDAO.change_employee_salary(this.id, new_salary);
        if (res.equals("")) {
            this.salary = new_salary;
            return res;
        }
        return res;
    }

    public String change_employee_terms(String new_terms) {
        String res = employeeDAO.change_employee_terms(this.id, new_terms);
        if (res.equals("")) {
            this.terms_of_employment = new_terms;
            return res;
        }
        return res;
    }

    public void add_hours(double hours) {
        current_total_salary = current_total_salary + (hours * salary);
    }

    public void remove_hours(double hours) {
        current_total_salary = current_total_salary - (hours * salary);
    }

    public String confirm_monthly_salary(double bonus_num) {
        monthly_salary = current_total_salary + bonus_num;
        current_total_salary = 0;
        return "";
    }

    public void set_current_salary(double num) {
        current_total_salary = num;
    }

    public String show_personal_info() {
        return  "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Bank Account: " + bank_account + "\n" +
                "Salary: " + salary + "\n" +
                "Terms of Employment: " + terms_of_employment + "\n" +
                "Employment Start Date: " + employment_date + "\n" +
                "Family Status: " + family_status + "\n" +
                "Student: " + is_student + "\n" +
                "Role Certifications: " + roles + "\n" +
                "Stores Assigned: " + certified_stores;
    }

    public String show_role_certifications() {
        StringBuilder output = new StringBuilder();
        for (JobType job: roles) {
            output.append(job.toString()).append(", ");
        }
        if (!output.toString().equals("")) {
            return output.substring(0, output.length() - 2);
        }
        return "";
    }

    public String show_assigned_stores() {
        StringBuilder output = new StringBuilder();
        for (String store: certified_stores) {
            output.append(store).append(", ");
        }
        if (output.isEmpty()) {
            return "";
        }
        return output.substring(0, output.length() - 2);
    }

    public String show_current_salary() {
        return String.valueOf(current_total_salary);
    }

    public int get_id() {
        return id;
    }

    /** Load_Data use only **/
    public void add_role(JobType role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }
    /** Load_Data use only **/
    public void add_store(String store) {
        if (!certified_stores.contains(store)) {
            certified_stores.add(store);
        }
    }

    public boolean is_shift_manager() {
        return roles.contains(JobType.SHIFTMANAGER);
    }

    public double get_salary() {
        return salary;
    }

    public int get_bank() {
        return bank_account;
    }

    public FamilyStatus get_family_status() {
        return family_status;
    }

    public boolean get_student() {
        return is_student;
    }

    public String get_terms() {
        return terms_of_employment;
    }

    public double get_monthly_salary() {
        return monthly_salary;
    }

    public String certify_driver(String phone, int maxWeight, boolean regularAllowed, boolean refrigeratedAllowed) {
        if (roles.contains(JobType.DRIVER)) {
            return "Employee already certified to this role";
        }
        String res = employeeDAO.certify_role(this.id, JobType.DRIVER.toString());
        if (res.equals("")) {
            roles.add(JobType.DRIVER);
            DriverSaver object = DriverController.getInstance();
            object.AddDriverToSystem(name, id.toString(), phone, maxWeight, regularAllowed, refrigeratedAllowed);
            return res;
        }
        return res;
    }
}
