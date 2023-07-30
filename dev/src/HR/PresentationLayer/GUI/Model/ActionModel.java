package HR.PresentationLayer.GUI.Model;

import HR.BusinessLayer.FamilyStatus;
import HR.BusinessLayer.JobType;
import HR.BusinessLayer.ShiftType;
import HR.PresentationLayer.GUI.View.ActionFrame;
import HR.PresentationLayer.HRCommandParser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;

public class ActionModel extends AbstractModel{

    public ActionModel() {

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton sourceButton) {

            if (sourceButton.getText().equals("Done")) {
                String[] answers = new String[((ActionFrame)relatedFrame).get_num_fields()];
                for (int i = 0; i < ((ActionFrame)relatedFrame).get_num_fields(); i++) {
                    answers[i] = ((ActionFrame)relatedFrame).get_field(i);
                }
                String action = ((ActionFrame)relatedFrame).get_title();
                String output = send_action(action, answers);
                if (output.equals("")) {
                    relatedFrame.clearError();
                    relatedFrame.clearInfo();
                    relatedFrame.dispose();
                }
                else {
                    relatedFrame.displayError(output);
                }
            }

            if (sourceButton.getText().equals("Back")) {
                relatedFrame.clearError();
                relatedFrame.clearInfo();
                relatedFrame.dispose();
            }
        }
    }

    private String send_action(String action, String[] answers) {
        return switch (action) {
            case "Assign an employee to a shift" -> assign_employee(answers);
            case "Remove an employee from a shift" -> remove_employee_assignment(answers);
            case "Add a job certification to an employee" -> add_job(answers);
            case "Certify a driver" -> certify_driver(answers);
            case "Remove a job certification from an employee" -> remove_job(answers);
            case "Assign an employee to a store" -> assign_store(answers);
            case "Remove an employee from a store" -> remove_store_assignment(answers);
            case "Add an employee" -> add_employee(answers);
            case "Remove an existing employee" -> remove_employee(answers);
            case "Create a new store" -> create_store(answers);
            case "Remove an existing store" -> remove_store(answers);
            case "Confirm a shift" -> confirm_shift(answers);
            case "Create a store's weekly schedule" -> create_schedule(answers);
            case "Limit an employee's work" -> limit_employee(answers);
            case "Remove an employee's work limitation" -> unlimit_employee(answers);
            case "Confirm an employee's monthly salary" -> confirm_salary(answers);
            case "Change an employee's salary" -> change_employee_salary(answers);
            case "Change an employee's terms of employment" -> change_employee_terms(answers);
            case "Cancel a product" -> cancel_product(answers);
            case "Add an availability" -> add_availability(answers);
            case "Remove an availability" -> remove_availability(answers);
            case "Change name" -> change_name(answers);
            case "Change bank account" -> change_bank_account(answers);
            case "Change family status" -> change_family_status(answers);
            case "Change student status" -> change_student_status(answers);
            case "Show employee's info" -> show_employee_info(answers);
            case "Show shift's assigned employees" -> show_shift_employees(answers);
            case "Show scheduled deliveries" -> show_scheduled_deliveries(answers);
            default -> "";
        };
    }

    private String assign_employee(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[1]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[2]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        JobType role_parsed = HRCommandParser.job_type_parser(answers[4]);
        if (role_parsed == null) {
            error = error + "Invalid job, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.assign_to_shift(id_parsed, date_parsed, shift_parsed, answers[3], role_parsed).getErrorMessage();
    }
    private String remove_employee_assignment(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[1]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[2]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        JobType role_parsed = HRCommandParser.job_type_parser(answers[4]);
        if (role_parsed == null) {
            error = error + "Invalid job, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.remove_from_shift(id_parsed, date_parsed, shift_parsed, answers[3], role_parsed).getErrorMessage();
    }
    private String add_job(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        JobType role_parsed = HRCommandParser.job_type_parser(answers[1]);
        if (role_parsed == null || role_parsed.equals(JobType.DRIVER)) {
            error = error + "Invalid job, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.certify_role(id_parsed, role_parsed).getErrorMessage();
    }

    private String certify_driver(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
            Integer max_weight_parsed = HRCommandParser.int_parser(answers[3]);
            if (max_weight_parsed == null) {
                error = error + "Invalid weight, ";
            }
            Object regular_parsed = HRCommandParser.student_parser(answers[4]);
            if (regular_parsed == null) {
                error = error + "Invalid answer, ";
            }
            Object refrigerated_parsed = HRCommandParser.student_parser(answers[5]);
            if (refrigerated_parsed == null) {
                error = error + "Invalid answer, ";
            }
            if (!error.isEmpty()) {
                return error.substring(0, error.length()-2);
            }
            return employeeService.certify_driver(id_parsed, answers[1], max_weight_parsed, (boolean) regular_parsed, (boolean) refrigerated_parsed).getErrorMessage();

    }
    private String remove_job(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        JobType role_parsed = HRCommandParser.job_type_parser(answers[1]);
        if (role_parsed == null) {
            error = error + "Invalid job, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.remove_role(id_parsed, role_parsed).getErrorMessage();
    }
    private String assign_store(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.assign_to_store(id_parsed, answers[1]).getErrorMessage();
    }
    private String remove_store_assignment(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.remove_from_store(id_parsed, answers[1]).getErrorMessage();
    }
    private String add_employee(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!HRCommandParser.name_checker(answers[1])) {
            error = error + "Invalid name, ";
        }
        Integer bank_parsed = HRCommandParser.int_parser(answers[2]);
        if (bank_parsed == null) {
            error = error + "Invalid bank account number, ";
        }
        Double salary_parsed = HRCommandParser.double_parser(answers[3]);
        if (salary_parsed == null) {
            error = error + "Invalid salary, ";
        }
        LocalDate employment_date_parsed = HRCommandParser.date_parser(answers[5]);
        if (employment_date_parsed == null) {
            error = error + "Invalid employment date, ";
        }
        FamilyStatus family_status_parsed = HRCommandParser.family_status_parser(answers[6]);
        if (family_status_parsed == null) {
            error = error + "Invalid family status, ";
        }
        Object student_parsed = HRCommandParser.student_parser(answers[7]);
        if (student_parsed == null) {
            error = error + "Invalid student status, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.add_employee(id_parsed, answers[1], bank_parsed, salary_parsed, answers[4], employment_date_parsed, family_status_parsed, (boolean)student_parsed, answers[8]).getErrorMessage();
    }
    private String remove_employee(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.remove_employee(id_parsed).getErrorMessage();
    }
    private String create_store(String[] answers) {
        return employeeService.create_store(answers[0]).getErrorMessage();
    }
    private String remove_store(String[] answers) {
        return employeeService.remove_store(answers[0]).getErrorMessage();
    }
    private String confirm_shift(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[0]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[1]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.confirm_shift(date_parsed, shift_parsed, answers[2]).getErrorMessage();
    }
    private String create_schedule(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[0]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        LocalTime time_parsed_1 = HRCommandParser.time_parser(answers[2]);
        if (time_parsed_1 == null) {
            error = error + "Invalid morning start time, ";
        }
        LocalTime time_parsed_2 = HRCommandParser.time_parser(answers[3]);
        if (time_parsed_2 == null) {
            error = error + "Invalid morning end time, ";
        }
        LocalTime time_parsed_3 = HRCommandParser.time_parser(answers[4]);
        if (time_parsed_3 == null) {
            error = error + "Invalid evening start time, ";
        }
        LocalTime time_parsed_4 = HRCommandParser.time_parser(answers[5]);
        if (time_parsed_4 == null) {
            error = error + "Invalid evening end time, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.create_weekly_schedule(date_parsed, answers[1], time_parsed_1, time_parsed_2, time_parsed_3, time_parsed_4).getErrorMessage();
    }
    private String limit_employee(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[1]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[2]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.limit_employee(id_parsed, date_parsed, shift_parsed, answers[3]).getErrorMessage();
    }
    private String unlimit_employee(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[1]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[2]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.remove_employee_limit(id_parsed, date_parsed, shift_parsed, answers[3]).getErrorMessage();
    }
    private String confirm_salary(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        Double bonus = HRCommandParser.double_parser(answers[1]);
        if (bonus == null) {
            error = error + "Invalid bonus, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.confirm_monthly_salary(id_parsed, bonus).getErrorMessage();
    }
    private String change_employee_salary(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        Double old_salary = HRCommandParser.double_parser(answers[1]);
        if (old_salary == null) {
            error = error + "Invalid bonus, ";
        }
        Double new_salary = HRCommandParser.double_parser(answers[2]);
        if (new_salary == null) {
            error = error + "Invalid bonus, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.change_employee_salary(id_parsed, old_salary, new_salary).getErrorMessage();
    }
    private String change_employee_terms(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.change_employee_terms(id_parsed, answers[1]).getErrorMessage();
    }
    private String cancel_product(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[1]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[2]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        Integer product_parsed = HRCommandParser.int_parser(answers[0]);
        if (product_parsed == null) {
            error = error + "Invalid product id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.cancel_product(product_parsed, date_parsed, shift_parsed, answers[3]).getErrorMessage();
    }
    private String add_availability(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[0]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[1]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.add_availability(date_parsed, shift_parsed, answers[2]).getErrorMessage();
    }
    private String remove_availability(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[0]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[1]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.remove_availability(date_parsed, shift_parsed, answers[2]).getErrorMessage();
    }
    private String change_student_status(String[] answers) {
        String error = "";
        Object student_parsed = HRCommandParser.student_parser(answers[0]);
        if (student_parsed == null) {
            error = error + "Invalid old student status, ";
        }
        Object student_parsed1 = HRCommandParser.student_parser(answers[1]);
        if (student_parsed1 == null) {
            error = error + "Invalid new student status, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.change_student((boolean)student_parsed, (boolean)student_parsed1).getErrorMessage();
    }

    private String change_family_status(String[] answers) {
        String error = "";
        FamilyStatus family_status_parsed = HRCommandParser.family_status_parser(answers[0]);
        if (family_status_parsed == null) {
            error = error + "Invalid old family status, ";
        }
        FamilyStatus family_status_parsed1 = HRCommandParser.family_status_parser(answers[1]);
        if (family_status_parsed1 == null) {
            error = error + "Invalid new family status, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.change_family_status(family_status_parsed, family_status_parsed1).getErrorMessage();
    }

    private String change_bank_account(String[] answers) {
        String error = "";
        Integer bank_parsed = HRCommandParser.int_parser(answers[0]);
        if (bank_parsed == null) {
            error = error + "Invalid old bank account number, ";
        }
        Integer bank_parsed1 = HRCommandParser.int_parser(answers[1]);
        if (bank_parsed1 == null) {
            error = error + "Invalid new bank account number, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.change_bank_account(bank_parsed, bank_parsed1).getErrorMessage();
    }

    private String change_name(String[] answers) {
        String error = "";
        if (!HRCommandParser.name_checker(answers[0])) {
            error = error + "Invalid old name, ";
        }
        if (!HRCommandParser.name_checker(answers[1])) {
            error = error + "Invalid new name, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.change_name(answers[0], answers[1]).getErrorMessage();
    }

    private String show_scheduled_deliveries(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[0]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[1]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.show_scheduled_deliveries(date_parsed, shift_parsed, answers[2]).getErrorMessage();
    }

    private String show_shift_employees(String[] answers) {
        String error = "";
        LocalDate date_parsed = HRCommandParser.date_parser(answers[0]);
        if (date_parsed == null) {
            error = error + "Invalid date, ";
        }
        ShiftType shift_parsed = HRCommandParser.shift_type_parser(answers[1]);
        if (shift_parsed == null) {
            error = error +  "Invalid shift type, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.show_shift_assigned(date_parsed, shift_parsed, answers[2]).getErrorMessage();
    }

    private String show_employee_info(String[] answers) {
        String error = "";
        Integer id_parsed = HRCommandParser.int_parser(answers[0]);
        if (id_parsed == null) {
            error = error + "Invalid employee id, ";
        }
        if (!error.isEmpty()) {
            return error.substring(0, error.length()-2);
        }
        return employeeService.show_employee_info(id_parsed).getErrorMessage();
    }
}
