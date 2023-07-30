package HR.ServiceLayer;

import HR.BusinessLayer.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public class EmployeeService {

    private final Facade facade;
    private int id = -1;

    public EmployeeService() {
        facade = new Facade();
    }

    /**
     * An employee's function to add an availability to a specified shift
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with an error message if occurred
     */
    public Response add_availability(LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.add_availability(id, shift_date, shift_type, store));
    }
    /**
     * An employee's function to remove an availability from a specified shift
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with an error message if occurred
     */
    public Response remove_availability(LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.remove_availability(id, shift_date, shift_type, store));
    }

    /**
     * An employee's function to show its availability
     * @return Response object with the employee's availability / an error message if occurred
     */
    public Response get_availability() {
        return new Response(facade.get_availability(id));
    }

    /**
     * An employee's function to show its shifts
     * @return Response object with the employee's shifts / an error message if occurred
     */
    public Response get_shifts() {
        return new Response(facade.get_shifts(id));
    }

    /**
     * An employee's function to log-in into the HR system
     * @param id Employee's id
     * @param password Employee's password
     * @return Response object with an error message if occurred
     */
    public Response login(int id, String password) {
        Response response = new Response(facade.login(id, password));
        if (response.errorOccurred()) {
            return response;
        }
        this.id = id;
        return response;
    }

    /**
     * An employee's function to log-out of the HR system
     * @return Response object with an error message if occurred
     */
    public Response logout() {
        try {
            Response response = new Response(facade.logout(this.id));
            if (response.errorOccurred()) {
                return response;
            }
            this.id = -1;
            return response;
        }
        catch (Exception exception) {
            return new Response("Invalid id");
        }
    }

    /**
     * An HR Manager's function to add a new employee to the system
     * @param employee_id Employee's id
     * @param name Employee's name
     * @param bank_account Employee's bank account number
     * @param salary Employee's salary
     * @param terms_of_employment Employee's terms of employment
     * @param employment_date Employee's date of employment (dd-mm-yyyy)
     * @param family_status Employee's family status (MARRIED, DIVORCED, WIDOWED, SINGLE)
     * @param is_student Employee's student indicator (true, false)
     * @param password Employee's password to HR system
     * @return Response object with an error message if occurred
     */
    public Response add_employee(int employee_id, String name, int bank_account, double salary, String terms_of_employment, LocalDate employment_date, FamilyStatus family_status, boolean is_student, String password) {
        return new Response(facade.add_employee(this.id, employee_id, name, bank_account, salary, terms_of_employment, employment_date, family_status, is_student, password));
    }

    /**
     * An HR Manager's function to remove an existing employee from the system
     * @param employee_id Employee's id
     * @return Response object with an error message if occurred
     */
    public Response remove_employee(int employee_id) {
        return new Response(facade.remove_employee(this.id, employee_id));
    }

    /**
     * An HR Manager's function to certify an employee to a specified role
     * @param employee_id Employee's id
     * @param role Added role
     * @return Response object with an error message if occurred
     */
    public Response certify_role(int employee_id, JobType role) {
        return new Response(facade.certify_role(this.id, employee_id, role));
    }

    /**
     * An HR Manager's function to certify an employee as a driver
     * @param employee_id Employee's id
     * @param phone Employee's phone number
     * @param maxWeight Employee's truck max weight possible
     * @param regularAllowed Employee's regular truck permission
     * @param refrigeratedAllowed Employee's refrigerated truck permission
     * @return Response object with an error message if occurred
     */
    public Response certify_driver(int employee_id, String phone, int maxWeight, boolean regularAllowed, boolean refrigeratedAllowed) {
        return new Response(facade.certify_driver(this.id, employee_id, phone, maxWeight, regularAllowed, refrigeratedAllowed));
    }

    /**
     * An HR Manager's function to remove a role certification from an employee
     * @param employee_id Employee's id
     * @param role Removed role
     * @return Response object with an error message if occurred
     */
    public Response remove_role(int employee_id, JobType role) {
        return new Response(facade.remove_role(this.id, employee_id, role));
    }

    /**
     * An HR Manager's function to assign an employee to a store
     * @param employee_id Employee's id
     * @param store Assigned store's name
     * @return Response object with an error message if occurred
     */
    public Response assign_to_store(int employee_id, String store) {
        return new Response(facade.assign_to_store(this.id, employee_id, store));
    }

    /**
     * An HR Manager's function to remove an employee from a store
     * @param employee_id Employee's id
     * @param store Removed store's name
     * @return Response object with an error message if occurred
     */
    public Response remove_from_store(int employee_id, String store) {
        return new Response(facade.remove_from_store(this.id, employee_id, store));
    }

    /**
     * An HR Manager's function to create a new store
     * @param store Created store's name
     * @return Response object with an error message if occurred
     */
    public Response create_store(String store) {
        return new Response(facade.create_store(this.id, store));
    }

    /**
     * An HR Manager's function to remove an existing store
     * @param store Created store's name
     * @return Response object with an error message if occurred
     */
    public Response remove_store(String store) {
        return new Response(facade.remove_store(this.id, store));
    }

    /**
     * An HR Manager's function to confirm a shift (shift has a shift manager)
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with an error message if occurred
     */
    public Response confirm_shift(LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.confirm_shift(this.id, shift_date, shift_type, store));
    }

    /**
     * An HR Manager's function to create a weekly schedule for a specified store - a 7 days schedule
     * @param week_start_date Schedule's first day's date
     * @param store Store's name
     * @param morning_start_time Morning shift's starting time
     * @param morning_end_time Morning shift's ending time
     * @param evening_start_time Evening shift's starting time
     * @param evening_end_time Evening shift's ending time
     * @return Response object with an error message if occurred
     */
    public Response create_weekly_schedule(LocalDate week_start_date, String store, LocalTime morning_start_time, LocalTime morning_end_time, LocalTime evening_start_time, LocalTime evening_end_time) {
        return new Response(facade.create_weekly_schedule(this.id, week_start_date, store, morning_start_time, morning_end_time, evening_start_time, evening_end_time));
    }

    /**
     * An HR Manager's function to assign an employee to a specified shift
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @param role Assigned role
     * @return Response object with an error message if occurred
     */
    public Response assign_to_shift(int employee_id, LocalDate shift_date, ShiftType shift_type, String store, JobType role) {
        return new Response(facade.assign_to_shift(this.id, employee_id, shift_date, shift_type, store, role));
    }

    /**
     * An HR Manager's function to remove an employee from a specified shift
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @param role Assigned role
     * @return Response object with an error message if occurred
     */
    public Response remove_from_shift(int employee_id, LocalDate shift_date, ShiftType shift_type, String store, JobType role) {
        return new Response(facade.remove_from_shift(this.id, employee_id, shift_date, shift_type, store, role));
    }

    /**
     * An HR Manager's function to limit an employee's work on a specified shift
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with an error message if occurred
     */
    public Response limit_employee(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.limit_employee(this.id, employee_id, shift_date, shift_type, store));
    }

    /**
     * An HR Manager's function to remove an employee's work limit from a specified shift
     * @param employee_id Employee's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with an error message if occurred
     */
    public Response remove_employee_limit(int employee_id, LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.remove_employee_limit(this.id, employee_id, shift_date, shift_type, store));
    }

    /**
     * An HR Manager's function to show a list of shift available employees
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with a list of shift available employees / an error message if occurred
     */
    public Response show_shift_availability(LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.show_shift_availability(this.id, shift_date, shift_type, store));
    }

    /**
     * An HR Manager's function to show a list of shift assigned employees
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with a list of shift assigned employees / an error message if occurred
     */
    public Response show_shift_assigned(LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.show_shift_assigned(this.id, shift_date, shift_type, store));
    }

    public boolean is_logged_in() {
        return id != -1;
    }

    /**
     * An employee's function to change its name
     * @param old_name Employee's old name
     * @param new_name Employee's new name
     * @return Response object with an error message if occurred
     */
    public Response change_name(String old_name, String new_name) {
        return new Response(facade.change_name(this.id, old_name, new_name));
    }

    /**
     * An employee's function to change its bank account number
     * @param old_bank_account Employee's old bank account number
     * @param new_bank_account Employee's new bank account number
     * @return Response object with an error message if occurred
     */
    public Response change_bank_account(int old_bank_account, int new_bank_account) {
        return new Response(facade.change_bank_account(this.id, old_bank_account, new_bank_account));
    }

    /**
     * An employee's function to change its family status
     * @param old_family_status Employee's old bank account number
     * @param new_family_status Employee's new bank account number
     * @return Response object with an error message if occurred
     */
    public Response change_family_status(FamilyStatus old_family_status, FamilyStatus new_family_status) {
        return new Response(facade.change_family_status(this.id, old_family_status, new_family_status));
    }

    /**
     * An employee's function to change its student indicator
     * @param old_student_status Employee's old bank account number
     * @param new_student_status Employee's new bank account number
     * @return Response object with an error message if occurred
     */
    public Response change_student(boolean old_student_status, boolean new_student_status) {
        return new Response(facade.change_student(this.id, old_student_status, new_student_status));
    }

    /**
     * An HR Manager's function to change an employee's salary
     * @param employee_id Employee's id
     * @param old_salary Employee's old salary
     * @param new_salary Employee's new salary
     * @return Response object with an error message if occurred
     */
    public Response change_employee_salary(int employee_id, double old_salary, double new_salary) {
        return new Response(facade.change_employee_salary(this.id, employee_id, old_salary, new_salary));
    }

    /**
     * An HR Manager's function to change an employee's terms of employment
     * @param employee_id Employee's id
     * @param new_terms Employee's new bank account number
     * @return Response object with an error message if occurred
     */
    public Response change_employee_terms(int employee_id, String new_terms) {
        return new Response(facade.change_employee_terms(this.id, employee_id, new_terms));
    }

    /**
     * An HR Manager's function to change an employee's terms of employment
     * @param employee_id Employee's id
     * @param bonus Employee's bonus to the monthly salary
     * @return Response object with an error message if occurred
     */
    public Response confirm_monthly_salary(int employee_id, double bonus) {
        return new Response(facade.confirm_monthly_salary(this.id, employee_id, bonus));
    }

    public boolean is_hr() {
        return facade.is_hr(this.id);
    }

    /**
     * An employee's function to show its personal information
     * @return Response object with the employee's information / an error message if occurred
     */
    public Response show_personal_info() {
        return new Response(facade.show_personal_info(this.id));
    }

    /**
     * An employee's function to show its role certification
     * @return Response object with the employee's role certification / an error message if occurred
     */
    public Response show_role_certifications() {
        return new Response(facade.show_role_certifications(this.id));
    }

    /**
     * An employee's function to show its assigned stores
     * @return Response object with the employee's assigned stores / an error message if occurred
     */
    public Response show_assigned_stores() {
        return new Response(facade.show_assigned_stores(this.id));
    }

    /**
     * An employee's function to show its current salary
     * @return Response object with the employee's current salary / an error message if occurred
     */
    public Response show_current_salary() {
        return new Response(facade.show_current_salary(this.id));
    }

    /**
     * An HR Manager's function to add a new HR Manager to the system
     * @param id Employee's id
     * @param name Employee's name
     * @param bank_account Employee's bank account number
     * @param salary Employee's salary
     * @param terms_of_employment Employee's terms of employment
     * @param employment_date Employee's date of employment (dd-mm-yyyy)
     * @param family_status Employee's family status (MARRIED, DIVORCED, WIDOWED, SINGLE)
     * @param is_student Employee's student indicator (true, false)
     * @param password Employee's password to HR system
     */
    public void add_hr(int id, String name, int bank_account, double salary, String terms_of_employment, LocalDate employment_date, FamilyStatus family_status, boolean is_student, String password) {
        facade.add_hr(id, name, bank_account, salary, terms_of_employment, employment_date, family_status, is_student, password);
    }

    /**
     * An HR Manager's function to show the existing employees
     * @return Response object with the existing employees / an error message if occurred
     */
    public Response show_employees() {
        return new Response(facade.show_employees(this.id));
    }

    /**
     * An HR Manager's function to show the specified employee's information
     * @return Response object with the employee's information / an error message if occurred
     */
    public Response show_employee_info(int id) {
        return new Response(facade.show_employee_info(this.id, id));
    }

    /**
     * A shift manager's function to cancel a product
     * @param product_id Product's id
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with an error message if occurred
     */
    public Response cancel_product(int product_id, LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.cancel_product(this.id, product_id, shift_date, shift_type, store));
    }

    /**
     * A shift manager / storekeeper's function to show a list of scheduled deliveries in a shift
     * @param shift_date Shift's date (dd-mm-yyyy)
     * @param shift_type Shift's type (MORNING/EVENING)
     * @param store Store's name
     * @return Response object with the scheduled deliveries / an error message if occurred
     */
    public Response show_scheduled_deliveries(LocalDate shift_date, ShiftType shift_type, String store) {
        return new Response(facade.show_scheduled_deliveries(this.id, shift_date, shift_type, store));
    }


    public List<ShiftPair> getAssignedShiftsDates(int id, LocalDate week_start) {
        return facade.getAssignedShiftsDates(id, week_start);
    }

    public List<String> getJobs(int id) {
        return facade.getJobs(id);
    }
}
