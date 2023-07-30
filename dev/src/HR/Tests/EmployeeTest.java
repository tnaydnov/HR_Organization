package HR.Tests;

import HR.BusinessLayer.*;
import HR.DataAccessLayer.Connect;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EmployeeTest {
    private static Facade facade = null;

    @org.junit.jupiter.api.BeforeEach
     void setUp() throws SQLException {
        Connect.getInstance().deleteRecordsOfTables();
        ShiftController.reset_instance();
        EmployeeController.reset_instance();
        facade = new Facade();
        facade.add_hr(111111111, "Tomer Naydnov", 1111111, 70, "None", LocalDate.of(2023,3,1), FamilyStatus.MARRIED, false, "123456");
        facade.login(111111111, "123456");
        facade.add_employee(111111111, 222222222, "Gili Cohen", 2222222, 30, "None", LocalDate.of(2023,2,1), FamilyStatus.SINGLE, true, "123456");
        facade.add_employee(111111111, 333333333, "Guy Cohen", 3333333, 30, "None", LocalDate.of(2023,2,1), FamilyStatus.SINGLE, true, "123456");
        facade.add_employee(111111111, 444444444, "Ohad Banay", 4444444, 35, "None", LocalDate.of(2023,2,1), FamilyStatus.SINGLE, true, "123456");
        facade.add_employee(111111111, 555555555, "Rotem Sela", 5555555, 35, "None", LocalDate.of(2023,2,1), FamilyStatus.SINGLE, true, "123456");
        facade.certify_role(111111111, 222222222, JobType.SHIFTMANAGER);
        facade.create_store(111111111, "Tel Aviv");
        facade.assign_to_store(111111111, 222222222, "Tel Aviv");
    }

    @org.junit.jupiter.api.Test
    void add_availability() {
        facade.create_weekly_schedule(111111111, LocalDate.of(2023,6,4), "Tel Aviv", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        facade.login(222222222, "123456");
        String res = facade.add_availability(222222222, LocalDate.of(2023,6,5), ShiftType.MORNING, "Tel Aviv");
        Assertions.assertEquals("", res);
        res = facade.add_availability(222222222, LocalDate.of(2023,6,5), ShiftType.MORNING, "test");
        Assertions.assertNotEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void remove_availability() {
        facade.create_weekly_schedule(111111111, LocalDate.of(2023,6,4), "Tel Aviv", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        facade.login(222222222, "123456");
        String res = facade.add_availability(222222222, LocalDate.of(2023,6,5), ShiftType.MORNING, "Tel Aviv");
        Assertions.assertEquals("", res);
        res = facade.remove_availability(222222222, LocalDate.of(2023,6,5), ShiftType.MORNING, "Tel Aviv");
        Assertions.assertEquals("", res);
        res = facade.remove_availability(222222222, LocalDate.of(2023,6,5), ShiftType.MORNING, "Tel Aviv");
        Assertions.assertNotEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void change_name() {
        facade.login(333333333, "123456");
        String res = facade.change_name(333333333, "Guy Cohenu", "Guy Cohenn");
        Assertions.assertNotEquals("", res);
        Assertions.assertNotEquals("Guy Cohenn" ,facade.get_employee_name(333333333));
        Assertions.assertEquals("Guy Cohen" ,facade.get_employee_name(333333333));
        res = facade.change_name(333333333, "Guy Cohen", "Guy Cohenn");
        Assertions.assertEquals("", res);
        Assertions.assertEquals("Guy Cohenn" ,facade.get_employee_name(333333333));
    }

    @org.junit.jupiter.api.Test
    void change_bank_account() {
        facade.login(333333333, "123456");
        String res = facade.change_bank_account(333333333, 33333332, 33333331);
        Assertions.assertNotEquals("", res);
        Assertions.assertNotEquals(33333331 ,facade.get_employee_bank_account(333333333));
        Assertions.assertEquals(3333333 ,facade.get_employee_bank_account(333333333));
        res = facade.change_bank_account(333333333, 3333333, 33333331);
        Assertions.assertEquals("", res);
        Assertions.assertEquals(33333331 ,facade.get_employee_bank_account(333333333));
    }

    @org.junit.jupiter.api.Test
    void change_family_status() {
        facade.login(333333333, "123456");
        String res = facade.change_family_status(333333333, FamilyStatus.DIVORCED, FamilyStatus.MARRIED);
        assertNotEquals("", res);
        assertNotEquals(FamilyStatus.MARRIED ,facade.get_employee_family_status(333333333));
        assertEquals(FamilyStatus.SINGLE ,facade.get_employee_family_status(333333333));
        res = facade.change_family_status(333333333, FamilyStatus.SINGLE, FamilyStatus.MARRIED);
        assertEquals("", res);
        assertEquals(FamilyStatus.MARRIED ,facade.get_employee_family_status(333333333));
    }

    @org.junit.jupiter.api.Test
    void change_student_status() {
        facade.login(333333333, "123456");
        String res = facade.change_student(333333333, false, true);
        assertNotEquals("", res);
        res = facade.change_student(333333333, true, false);
        Assertions.assertEquals("", res);
        Assertions.assertFalse(facade.get_employee_student_status(333333333));
    }

    @org.junit.jupiter.api.Test
    void change_salary() {
        String res = facade.change_employee_salary(111111111, 333333333, 31, 35);
        Assertions.assertNotEquals("", res);
        Assertions.assertNotEquals(35 ,facade.get_employee_salary(333333333));
        Assertions.assertEquals(30 ,facade.get_employee_salary(333333333));
        res = facade.change_employee_salary(111111111, 333333333, 30, 35);
        Assertions.assertEquals("", res);
        Assertions.assertEquals(35 ,facade.get_employee_salary(333333333));
    }

    @org.junit.jupiter.api.Test
    void change_terms() {
        String res = facade.change_employee_terms(111111111, 333333333, "test");
        assertEquals("", res);
        assertEquals("test" ,facade.get_employee_terms(333333333));
    }

    @org.junit.jupiter.api.Test
    void assign_employee() {
        String res = facade.assign_to_shift(111111111,222222222, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv", JobType.SHIFTMANAGER);
        assertNotEquals("", res);
        facade.create_weekly_schedule(111111111, LocalDate.of(2023,6,4), "Tel Aviv", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        facade.login(222222222, "123456");
        facade.add_availability(222222222, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv");
        res = facade.assign_to_shift(111111111,222222222, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv", JobType.SHIFTMANAGER);
        assertEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void remove_employee_assignment() {
        facade.create_weekly_schedule(111111111, LocalDate.of(2023,6,4), "Tel Aviv", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        facade.login(222222222, "123456");
        facade.add_availability(222222222, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv");
        String res = facade.assign_to_shift(111111111,222222222, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv", JobType.SHIFTMANAGER);
        assertEquals("", res);
        res = facade.remove_from_shift(111111111,222222222, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv", JobType.SHIFTMANAGER);
        assertEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void add_job_certification() {
        String res = facade.certify_role(111111111, 333333333, JobType.STOREKEEPER);
        assertEquals("", res);
        Assertions.assertTrue(facade.certified_to_role(333333333, JobType.STOREKEEPER));
        res = facade.certify_role(111111111, 333333333, JobType.STOREKEEPER);
        assertNotEquals("", res);
        Assertions.assertTrue(facade.certified_to_role(333333333, JobType.STOREKEEPER));
    }

    @org.junit.jupiter.api.Test
    void remove_job_certification() {
        facade.certify_role(111111111, 333333333, JobType.STOREKEEPER);
        String res = facade.remove_role(111111111, 333333333, JobType.STOREKEEPER);
        assertEquals("", res);
        Assertions.assertFalse(facade.certified_to_role(333333333, JobType.STOREKEEPER));
        res = facade.remove_role(111111111, 333333333, JobType.STOREKEEPER);
        assertNotEquals("", res);
        Assertions.assertFalse(facade.certified_to_role(333333333, JobType.STOREKEEPER));
    }

    @org.junit.jupiter.api.Test
    void add_store_assignment() {
        String res = facade.assign_to_store(111111111, 333333333, "test");
        assertNotEquals("", res);
        Assertions.assertFalse(facade.assigned_to_store(333333333, "test"));
        facade.create_store(111111111, "test");
        res = facade.assign_to_store(111111111, 333333333, "test");
        assertEquals("", res);
        Assertions.assertTrue(facade.assigned_to_store(333333333, "test"));
    }

    @org.junit.jupiter.api.Test
    void remove_store_assignment() {
        facade.create_store(111111111, "test");
        facade.assign_to_store(111111111, 333333333, "test");
        String res = facade.remove_from_store(111111111, 333333333, "test1");
        assertNotEquals("", res);
        Assertions.assertTrue(facade.assigned_to_store(333333333, "test"));
        res = facade.remove_from_store(111111111, 333333333, "test");
        assertEquals("", res);
        Assertions.assertFalse(facade.assigned_to_store(333333333, "test"));
    }

    @org.junit.jupiter.api.Test
    void add_employee() {
        Assertions.assertFalse(facade.employee_exists(666666666));
        String res = facade.add_employee(111111111, 666666666, "Ehud Manor", 6666666, 38, "None", LocalDate.of(2023,2,1), FamilyStatus.SINGLE, true, "123456");
        Assertions.assertTrue(facade.employee_exists(666666666));
        Assertions.assertEquals("", res);
        res = facade.add_employee(111111111, 666666666, "Ehud Manor", 6666666, 38, "None", LocalDate.of(2023,2,1), FamilyStatus.SINGLE, true, "123456");
        Assertions.assertNotEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void remove_employee() {
        facade.add_employee(111111111, 666666666, "Ehud Manor", 6666666, 38, "None", LocalDate.of(2023,2,1), FamilyStatus.SINGLE, true, "123456");
        Assertions.assertTrue(facade.employee_exists(666666666));
        String res = facade.remove_employee(111111111, 666666666);
        Assertions.assertFalse(facade.employee_exists(666666666));
        Assertions.assertEquals("", res);
        res = facade.remove_employee(111111111, 666666666);
        Assertions.assertNotEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void create_store() {
        Assertions.assertFalse(facade.store_exists("Be'er"));
        String res = facade.create_store(111111111, "Be'er");
        Assertions.assertTrue(facade.store_exists("Be'er"));
        Assertions.assertEquals("", res);
        res = facade.create_store(111111111, "Be'er");
        Assertions.assertNotEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void remove_store() {
        facade.create_store(111111111, "Be'er Sheva");
        Assertions.assertTrue(facade.store_exists("Be'er Sheva"));
        String res = facade.remove_store(111111111, "Be'er Sheva");
        Assertions.assertFalse(facade.store_exists("Be'er Sheva"));
        Assertions.assertEquals("", res);
        res = facade.remove_store(111111111, "Be'er Sheva");
        Assertions.assertNotEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void confirm_shift() {
        create_schedule();
        String res = facade.confirm_shift(111111111, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv");
        Assertions.assertNotEquals("", res);
        facade.login(222222222, "123456");
        facade.add_availability(222222222, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv");
        facade.assign_to_shift(111111111,222222222, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv", JobType.SHIFTMANAGER);
        res = facade.confirm_shift(111111111, LocalDate.of(2023,6,4), ShiftType.MORNING, "Tel Aviv");
        Assertions.assertEquals("", res);
    }

    @org.junit.jupiter.api.Test
    void create_schedule() {
        String res = facade.create_weekly_schedule(111111111, LocalDate.of(2023,6,4), "Tel Aviv", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        Assertions.assertEquals("", res);
        Assertions.assertTrue(facade.future_schedule_exists(LocalDate.of(2023,6,4), "Tel Aviv"));
        Assertions.assertFalse(facade.past_schedule_exists(LocalDate.of(2023,6,4), "Tel Aviv"));
        res = facade.create_weekly_schedule(111111111, LocalDate.of(2023,2,5), "Tel Aviv", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        Assertions.assertEquals("", res);
        Assertions.assertFalse(facade.future_schedule_exists(LocalDate.of(2023,2,5), "Tel Aviv"));
        Assertions.assertTrue(facade.past_schedule_exists(LocalDate.of(2023,2,5), "Tel Aviv"));
    }

    @org.junit.jupiter.api.Test
    void limit_work() {
        String res = facade.limit_employee(111111111,444444444, LocalDate.of(2023,6,1), ShiftType.MORNING, "Rishon");
        Assertions.assertNotEquals("", res);
        facade.create_store(111111111, "Rishon");
        res = facade.limit_employee(111111111,444444444, LocalDate.of(2023,6,1), ShiftType.MORNING, "Rishon");
        Assertions.assertNotEquals("", res);
        facade.create_weekly_schedule(111111111, LocalDate.of(2023,5,28), "Rishon", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        facade.assign_to_store(111111111, 444444444, "Rishon");
        res = facade.limit_employee(111111111,444444444, LocalDate.of(2023,6,1), ShiftType.MORNING, "Rishon");
        Assertions.assertEquals("", res);
        Assertions.assertTrue(facade.is_limited(444444444, LocalDate.of(2023,6,1), ShiftType.MORNING, "Rishon"));
    }

    @org.junit.jupiter.api.Test
    void remove_work_limitation() {
        facade.create_store(111111111, "Rishon");
        facade.create_weekly_schedule(111111111, LocalDate.of(2023,5,28), "Rishon", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        facade.assign_to_store(111111111, 444444444, "Rishon");
        facade.limit_employee(111111111,444444444, LocalDate.of(2023,6,1), ShiftType.MORNING, "Rishon");
        Assertions.assertTrue(facade.is_limited(444444444, LocalDate.of(2023,6,1), ShiftType.MORNING, "Rishon"));
        String res = facade.remove_employee_limit(111111111,444444444, LocalDate.of(2023,6,1), ShiftType.MORNING, "Rishon");
        Assertions.assertEquals("", res);
        Assertions.assertFalse(facade.is_limited(444444444, LocalDate.of(2023,6,1), ShiftType.MORNING, "Rishon"));
    }

    @org.junit.jupiter.api.Test
    void login() {
        Assertions.assertFalse(facade.employee_logged_in(222222222));
        String res = facade.login(222222222, "123456");
        Assertions.assertTrue(facade.employee_logged_in(222222222));
        Assertions.assertEquals("", res);
        res = facade.login(222222222, "123456");
        Assertions.assertNotEquals("", res);
        Assertions.assertTrue(facade.employee_logged_in(222222222));
    }

    @org.junit.jupiter.api.Test
    void logout() {
        facade.login(222222222, "123456");
        Assertions.assertTrue(facade.employee_logged_in(222222222));
        String res = facade.logout(222222222);
        Assertions.assertFalse(facade.employee_logged_in(222222222));
        Assertions.assertEquals("", res);
        res = facade.logout(222222222);
        Assertions.assertNotEquals("", res);
        Assertions.assertFalse(facade.employee_logged_in(222222222));
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
        Connect.getInstance().deleteRecordsOfTables();
    }


}