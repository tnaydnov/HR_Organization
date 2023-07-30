package HR.DataAccessLayer;

import HR.BusinessLayer.Employee;
import HR.BusinessLayer.FamilyStatus;
import HR.BusinessLayer.JobType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDAO {

    private final Connect conn = Connect.getInstance();

    public EmployeeDAO() {}

    public Map<Employee, String> load_Data() {
        Map<Employee, String> employees = new HashMap<>();
        try {
            List<HashMap<String, Object>> employees_details = conn.executeQuery("SELECT * FROM Employees");
            for (HashMap<String, Object> record: employees_details) {
                String pass = employee_record_password(record);
                Employee employee = employee_record(record);
                employees.put(employee, pass);
            }
            return employees;
        }
        catch (SQLException exception) {
            return null;
        }
    }

    private String employee_record_password(HashMap<String, Object> personalDetails) {
        return (String) personalDetails.get("password");
    }

    private Employee employee_record(HashMap<String, Object> personalDetails) {
        try {
            Integer id = (Integer) personalDetails.get("id");
            String name = (String) personalDetails.get("name");
            Integer bank_account = (Integer) personalDetails.get("bankAccount");
            double salary = (Double) personalDetails.get("salary");
            String terms_of_employment = (String) personalDetails.get("termsOfEmployment");
            String date = (String) personalDetails.get("employmentDate");
            LocalDate employment_date = LocalDate.parse(date);
            String family_status = (String) personalDetails.get("familyStatus");
            Integer student = (Integer) personalDetails.get("isStudent");
            boolean is_student = student.equals(1);
            double current_monthly_salary = (Double) personalDetails.get("currentSalary");
            Employee employee = new Employee(id, name, bank_account, salary, terms_of_employment, employment_date, FamilyStatus.valueOf(family_status), is_student, this);
            List<HashMap<String, Object>> rolesSet = conn.executeQuery("SELECT jobType FROM Roles WHERE id = ?", id);
            List<HashMap<String, Object>> stores = conn.executeQuery("SELECT * FROM Stores WHERE id = ?", id);
            reconstructEmployeeStores(employee, stores);
            reconstructEmployeeRoles(employee, rolesSet);
            employee.set_current_salary(current_monthly_salary);
            return employee;
        } catch (SQLException e) {
            return null;
        }
    }

    private void reconstructEmployeeRoles(Employee employee, List<HashMap<String,Object>> rolesSet) throws SQLException {
        for (HashMap<String, Object> stringObjectHashMap : rolesSet) {
            String role = (String) stringObjectHashMap.get("jobType");
            if (role == null)
                return;
            employee.add_role(JobType.valueOf(role));
        }
    }

    private void reconstructEmployeeStores(Employee employee, List<HashMap<String,Object>> storesSet) throws SQLException {
        for (HashMap<String, Object> stringObjectHashMap : storesSet) {
            String store = (String) stringObjectHashMap.get("store");
            if (store == null)
                return;
            employee.add_store(store);
        }
    }

    public String add_employee(int employee_id, String name, int bank_account, double salary, String terms_of_employment, LocalDate shift_date, FamilyStatus family_status, boolean student, String password) {
        try{
            int student_index = 0;
            if (student) {
                student_index = 1;
            }
            conn.executeUpdate("INSERT INTO Employees (id, name, password, salary, termsOfEmployment, familyStatus, isStudent, bankAccount, employmentDate, currentSalary) VALUES(?,?,?,?,?,?,?,?,?,?)",employee_id, name, password, salary, terms_of_employment, family_status.toString(), student_index, bank_account, shift_date, 0);
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is already registered in the system";
        }
    }

    public String remove_employee(int employee_id) {
        try {
            conn.executeUpdate("DELETE FROM Employees WHERE id = " + employee_id);
            return "";
        }
        catch (SQLException e){
            return "Unable to delete employee.";
        }
    }

    public String certify_role(Integer employee_id, String role) {
        try{
            conn.executeUpdate("INSERT INTO EmployeeRoles (id, jobType) VALUES(?,?)", employee_id, role);
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is already certified to this role";
        }
    }


    public String remove_role(Integer employee_id, String role) {
        try{
            conn.executeUpdate("DELETE FROM EmployeeRoles WHERE id = " + employee_id + " AND jobType = '" + role + "'");
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " isn't certified to this role";
        }
    }

    public String assign_to_store(Integer employee_id, String store) {
        try{
            conn.executeUpdate("INSERT INTO EmployeeStores (id, store) VALUES(?,?)", employee_id, store);
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " is already certified to this store";
        }
    }


    public String remove_from_store(Integer employee_id, String store) {
        try{
            conn.executeUpdate("DELETE FROM EmployeeStores WHERE id = " + employee_id + " AND store = '" + store + "'");
            return "";
        } catch (SQLException e) {
            return "Employee with id " + employee_id + " isn't certified to this store";
        }
    }

    public String change_name(Integer employee_id, String new_name) {
        try{
            conn.executeUpdate("UPDATE Employees SET name = '" + new_name + "' WHERE id = " + employee_id);
            return "";
        } catch (SQLException e) {
            return "Couldn't update the data base";
        }
    }

    public String change_bank_account(Integer employee_id, Integer new_bank_account) {
        try{
            conn.executeUpdate("UPDATE Employees SET bankAccount = " + new_bank_account + " WHERE id = " + employee_id);
            return "";
        } catch (SQLException e) {
            return "Couldn't update the data base";
        }
    }

    public String change_family_status(Integer employee_id, FamilyStatus new_family_status) {
        try{
            conn.executeUpdate("UPDATE Employees SET familyStatus = '" + new_family_status.toString() + "' WHERE id = " + employee_id);
            return "";
        } catch (SQLException e) {
            return "Couldn't update the data base";
        }
    }

    public String change_student(Integer employee_id, boolean new_student_status) {
        int student = 0;
        if (new_student_status) {
            student = 1;
        }
        try{
            conn.executeUpdate("UPDATE Employees SET isStudent = " + student + " WHERE id = " + employee_id);
            return "";
        } catch (SQLException e) {
            return "Couldn't update the data base";
        }
    }

    public String change_employee_salary(Integer employee_id, double new_salary) {
        try{
            conn.executeUpdate("UPDATE Employees SET salary = " + new_salary + " WHERE id = " + employee_id);
            return "";
        } catch (SQLException e) {
            return "Couldn't update the data base";
        }
    }

    public String change_employee_terms(Integer employee_id, String new_terms) {
        try{
            conn.executeUpdate("UPDATE Employees SET termsOfEmployment = '" + new_terms + "' WHERE id = " + employee_id);
            return "";
        } catch (SQLException e) {
            return "Couldn't update the data base";
        }
    }


}
