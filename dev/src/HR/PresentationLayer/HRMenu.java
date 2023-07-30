package HR.PresentationLayer;

import Deliveries.BusinessLayer.Driver;
import Deliveries.BusinessLayer.Enums_and_Interfaces.Availability;
import Deliveries.BusinessLayer.License;
import Deliveries.DataAccessLayer.DriverDAO;
import HR.BusinessLayer.*;
import HR.ServiceLayer.EmployeeService;
import HR.ServiceLayer.Response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class HRMenu {

    private final EmployeeService service;

    public HRMenu() {
        service = new EmployeeService();
    }

    public String print_red(String text) {
        return "\033[1;31m" + text + "\u001B[0m";
    }

    public String print_green(String text) {
        return "\033[1;32m" + text + "\u001B[0m";
    }

    public String print_blue(String text) {
        return "\033[34m" + text + "\u001B[0m";
    }

    public void run() {
        //System.out.println(service.load_data().getErrorMessage());
        System.out.println("Welcome to HR system!");
        while(true) {
            while (!service.is_logged_in()) {
                login_screen();
            }
            menu();
        }
    }

    public void login_screen() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter your id: ");
            String id = scanner.nextLine();
            Integer id_parsed = HRCommandParser.int_parser(id);
            if (id_parsed == null) {
                System.out.println("Invalid id. Please try again.");
                continue;
            }
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            Response res = service.login(id_parsed, password);
            if (res.errorOccurred()) {
                System.out.println(print_red(res.getErrorMessage()));
                System.out.println("Please try again.");
                continue;
            }
            break;
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while(service.is_logged_in()) {
            if (service.is_hr()) {
                manager_options();
                String input = scanner.nextLine();
                handle_manager_action(input);
            } else {
                employee_options();
                String input = scanner.nextLine();
                handle_employee_action(input);
            }
        }
    }

    private void handle_employee_action(String input) {
        Scanner scanner = new Scanner(System.in);
        switch (input) {
            case "1" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the availability store / \"drivers\": ");
                String store = scanner.nextLine();
                Response res = service.add_availability(date_parsed, shift_parsed, store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Added availability successfully!"));
                }
                System.out.println("=====================================================================================");
            }
            case "2" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the availability store / \"drivers\": ");
                String store = scanner.nextLine();
                Response res = service.remove_availability(date_parsed, shift_parsed, store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Removed availability successfully!"));
                }
                System.out.println("=====================================================================================");
            }
            case "3" -> {
                System.out.println("=====================================================================================");
                System.out.println("Your shifts:");
                System.out.println(service.get_shifts().getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "4" -> {
                System.out.println("=====================================================================================");
                System.out.println("Your availability:");
                System.out.println(service.get_availability().getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "5" -> {
                System.out.println("=====================================================================================");
                System.out.println(service.show_personal_info().getErrorMessage());
                System.out.println("\nWhich of the following do you wish to change?");
                System.out.println("1. Name");
                System.out.println("2. Bank Account");
                System.out.println("3. Family Status");
                System.out.println("4. Student Status");
                System.out.print("Option: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        System.out.println("=====================================================================================");
                        System.out.print("Please enter your old name: ");
                        String old_name = scanner.nextLine();
                        System.out.print("Please enter your new name: ");
                        String new_name = scanner.nextLine();
                        Response res = service.change_name(old_name, new_name);
                        if (res.errorOccurred()) {
                            System.out.println("\n");
                            System.out.println(print_red(res.getErrorMessage()));
                        } else {
                            System.out.println("\n");
                            System.out.println(print_green("Changed your name successfully"));
                        }
                        System.out.println("=====================================================================================");
                    }
                    case "2" -> {
                        System.out.println("=====================================================================================");
                        System.out.print("Please enter your old bank account: ");
                        String old_bank_account = scanner.nextLine();
                        Integer old_bank_parsed = HRCommandParser.int_parser(old_bank_account);
                        if (old_bank_parsed == null) {
                            System.out.println(print_red("Invalid old bank account number."));
                            break;
                        }
                        System.out.print("Please enter your new bank account: ");
                        String new_bank_account = scanner.nextLine();
                        Integer new_bank_parsed = HRCommandParser.int_parser(new_bank_account);
                        if (new_bank_parsed == null) {
                            System.out.println(print_red("Invalid new bank account number."));
                            break;
                        }
                        Response res = service.change_bank_account(old_bank_parsed, new_bank_parsed);
                        if (res.errorOccurred()) {
                            System.out.println("\n");
                            System.out.println(print_red(res.getErrorMessage()));
                        } else {
                            System.out.println("\n");
                            System.out.println(print_green("Changed your bank account successfully"));
                        }
                        System.out.println("=====================================================================================");
                    }
                    case "3" -> {
                        System.out.println("=====================================================================================");
                        System.out.print("Please enter your old family status: ");
                        String old_family_status = scanner.nextLine();
                        FamilyStatus old_family_status_parsed = HRCommandParser.family_status_parser(old_family_status);
                        if (old_family_status_parsed == null) {
                            System.out.println(print_red("Invalid old family status."));
                            break;
                        }
                        System.out.print("Please enter your new family status: ");
                        String new_family_status = scanner.nextLine();
                        FamilyStatus new_family_status_parsed = HRCommandParser.family_status_parser(new_family_status);
                        if (new_family_status_parsed == null) {
                            System.out.println(print_red("Invalid new family status."));
                            break;
                        }
                        Response res = service.change_family_status(old_family_status_parsed, new_family_status_parsed);
                        if (res.errorOccurred()) {
                            System.out.println("\n");
                            System.out.println(print_red(res.getErrorMessage()));
                        } else {
                            System.out.println("\n");
                            System.out.println(print_green("Changed your family status successfully"));
                        }
                        System.out.println("=====================================================================================");
                    }
                    case "4" -> {
                        System.out.println("=====================================================================================");
                        System.out.print("Were you a student before (yes / no)? ");
                        String old_student_status = scanner.nextLine();
                        Object old_student_parsed = HRCommandParser.student_parser(old_student_status);
                        if (old_student_parsed == null) {
                            System.out.println(print_red("Invalid input."));
                            break;
                        }
                        System.out.print("Are you a student now (yes / no)? ");
                        String new_student_status = scanner.nextLine();
                        Object new_student_parsed = HRCommandParser.student_parser(new_student_status);
                        if (new_student_parsed == null) {
                            System.out.println(print_red("Invalid input."));
                            break;
                        }
                        Response res = service.change_student((boolean) old_student_parsed, (boolean) new_student_parsed);
                        if (res.errorOccurred()) {
                            System.out.println("\n");
                            System.out.println(print_red(res.getErrorMessage()));
                        } else {
                            System.out.println("\n");
                            System.out.println(print_green("Changed your student status successfully"));
                        }
                        System.out.println("=====================================================================================");
                    }
                }
                System.out.println("=====================================================================================");
            }
            case "6" -> {
                System.out.println("=====================================================================================");
                System.out.print("Certified roles: ");
                System.out.println(service.show_role_certifications().getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "7" -> {
                System.out.println("=====================================================================================");
                System.out.print("Assigned stores: ");
                System.out.println(service.show_assigned_stores().getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "8" -> {
                System.out.println("=====================================================================================");
                System.out.println("Personal information:");
                System.out.println(service.show_personal_info().getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "9" -> {
                System.out.println("=====================================================================================");
                System.out.print("Current salary: ");
                System.out.println(service.show_current_salary().getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "10" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the store: ");
                String store = scanner.nextLine();
                System.out.print("Please enter the product id: ");
                String product_id = scanner.nextLine();
                Integer product_parsed = HRCommandParser.int_parser(product_id);
                if (product_parsed == null) {
                    System.out.println(print_red("Invalid product id."));
                    break;
                }
                Response res = service.cancel_product(product_parsed, date_parsed, shift_parsed, store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Cancelled product successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "11" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the store: ");
                String store = scanner.nextLine();
                System.out.print("Please enter the product id: ");
                String product_id = scanner.nextLine();
                Integer product_parsed = HRCommandParser.int_parser(product_id);
                if (product_parsed == null) {
                    System.out.println(print_red("Invalid product id."));
                    break;
                }
                Response res = service.show_scheduled_deliveries(date_parsed, shift_parsed, store);
                System.out.println("Scheduled deliveries for that shift: ");
                System.out.println(res.getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "12" -> {
                System.out.println("=====================================================================================");
                Response res = service.logout();
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                }
                System.out.println("=====================================================================================");
            }
            default -> System.out.println(print_red("You've chosen an invalid option. Please choose again."));
        }
    }

    private void handle_manager_action(String input) {
        Scanner scanner = new Scanner(System.in);
        switch (input) {
            case "1" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the store / \"drivers\": ");
                String store = scanner.nextLine();
                System.out.print("Available employees for this shift: ");
                System.out.println("\n");
                System.out.println(service.show_shift_availability(date_parsed, shift_parsed, store).getErrorMessage());
                System.out.println("\n");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the role: ");
                String role = scanner.nextLine();
                JobType role_parsed = HRCommandParser.job_type_parser(role);
                if (role_parsed == null) {
                    System.out.println(print_red("Invalid job."));
                    break;
                }
                Response res = service.assign_to_shift(id_parsed, date_parsed, shift_parsed, store, role_parsed);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Assigned employee successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "2" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the store / \"drivers\": ");
                String store = scanner.nextLine();
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the role: ");
                String role = scanner.nextLine();
                JobType role_parsed = HRCommandParser.job_type_parser(role);
                if (role_parsed == null) {
                    System.out.println(print_red("Invalid job."));
                    break;
                }
                Response res = service.remove_from_shift(id_parsed, date_parsed, shift_parsed, store, role_parsed);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Removed employee from the shift successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "3" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the role: ");
                String role = scanner.nextLine();
                JobType role_parsed = HRCommandParser.job_type_parser(role);
                if (role_parsed == null) {
                    System.out.println(print_red("Invalid job."));
                    break;
                }
                if (role_parsed.equals(JobType.DRIVER)) {
                    System.out.print("Please enter the employee's phone number: ");
                    String phone = scanner.nextLine();
                    System.out.print("Please enter the employee's max weight in TONS: ");
                    String max_weight = scanner.nextLine();
                    Integer max_weight_parsed = HRCommandParser.int_parser(max_weight);
                    if (max_weight_parsed == null) {
                        System.out.println(print_red("Invalid weight."));
                        break;
                    }
                    System.out.print("Is the employee certified to a regular truck? ");
                    String regular = scanner.nextLine();
                    Object regular_parsed = HRCommandParser.student_parser(regular);
                    if (regular_parsed == null) {
                        System.out.println(print_red("Invalid answer."));
                        break;
                    }
                    System.out.print("Is the employee certified to a refrigerated truck? ");
                    String refrigerated = scanner.nextLine();
                    Object refrigerated_parsed = HRCommandParser.student_parser(refrigerated);
                    if (refrigerated_parsed == null) {
                        System.out.println(print_red("Invalid answer."));
                        break;
                    }
                    Response res = service.certify_driver(id_parsed, phone, max_weight_parsed, (boolean) regular_parsed, (boolean) refrigerated_parsed);
                    if (res.errorOccurred()) {
                        System.out.println("\n");
                        System.out.println(print_red(res.getErrorMessage()));
                    } else {
                        System.out.println("\n");
                        System.out.println(print_green("Added a role certification successfully"));
                    }
                }
                else {
                    Response res = service.certify_role(id_parsed, role_parsed);
                    if (res.errorOccurred()) {
                        System.out.println("\n");
                        System.out.println(print_red(res.getErrorMessage()));
                    } else {
                        System.out.println("\n");
                        System.out.println(print_green("Added a role certification successfully"));
                    }
                }
                System.out.println("=====================================================================================");
            }
            case "4" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the role: ");
                String role = scanner.nextLine();
                JobType role_parsed = HRCommandParser.job_type_parser(role);
                if (role_parsed == null) {
                    System.out.println(print_red("Invalid job."));
                    break;
                }
                Response res = service.remove_role(id_parsed, role_parsed);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Removed a role certification successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "5" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the store: ");
                String store = scanner.nextLine();
                Response res = service.assign_to_store(id_parsed, store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Assigned the employee to a store successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "6" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the store: ");
                String store = scanner.nextLine();
                Response res = service.remove_from_store(id_parsed, store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Removed the employee from a store successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "7" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the employee's name: ");
                String name = scanner.nextLine();
                if (!HRCommandParser.name_checker(name)) {
                    System.out.println(print_red("Invalid name."));
                    break;
                }
                System.out.print("Please enter the employee's bank account: ");
                String bank_account = scanner.nextLine();
                Integer bank_parsed = HRCommandParser.int_parser(bank_account);
                if (bank_parsed == null) {
                    System.out.println(print_red("Invalid bank account number."));
                    break;
                }
                System.out.print("Please enter the employee's salary: ");
                String salary = scanner.nextLine();
                Double salary_parsed = HRCommandParser.double_parser(salary);
                if (salary_parsed == null) {
                    System.out.println(print_red("Invalid salary."));
                    break;
                }
                System.out.print("Please enter the employee's terms of employment: ");
                String terms_of_employment = scanner.nextLine();
                System.out.print("Please enter the employment date (dd-mm-yyyy): ");
                String employment_date = scanner.nextLine();
                LocalDate employment_date_parsed = HRCommandParser.date_parser(employment_date);
                if (employment_date_parsed == null) {
                    System.out.println(print_red("Invalid employment date."));
                    break;
                }
                System.out.print("Please enter the employee's family status: ");
                String family_status = scanner.nextLine();
                FamilyStatus family_status_parsed = HRCommandParser.family_status_parser(family_status);
                if (family_status_parsed == null) {
                    System.out.println(print_red("Invalid family status."));
                    break;
                }
                System.out.print("Is the employee a student? ");
                String is_student = scanner.nextLine();
                Object student_parsed = HRCommandParser.student_parser(is_student);
                if (student_parsed == null) {
                    System.out.println(print_red("Invalid student status."));
                    break;
                }
                System.out.print("Please enter the employee's password: ");
                String password = scanner.nextLine();
                Response res = service.add_employee(id_parsed, name, bank_parsed, salary_parsed, terms_of_employment, employment_date_parsed, family_status_parsed, (boolean) student_parsed, password);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Added the employee successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "8" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                Response res = service.remove_employee(id_parsed);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Removed the employee successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "9" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the store's name: ");
                String store = scanner.nextLine();
                Response res = service.create_store(store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Created the store successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "10" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the store's name: ");
                String store = scanner.nextLine();
                Response res = service.remove_store(store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Removed the store successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "11" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the store's name / \"drivers\": ");
                String store = scanner.nextLine();
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                Response res = service.confirm_shift(date_parsed, shift_parsed, store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Confirmed the shift successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "12" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the week's first day date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the store's name / \"drivers\": ");
                String store = scanner.nextLine();
                System.out.print("Please enter the schedule's morning shifts start time (hh:mm): ");
                String morn_start = scanner.nextLine();
                LocalTime morn_start_parsed = HRCommandParser.time_parser(morn_start);
                if (morn_start_parsed == null) {
                    System.out.println(print_red("Invalid morning start time."));
                    break;
                }
                System.out.print("Please enter the schedule's morning shifts end time (hh:mm): ");
                String morn_end = scanner.nextLine();
                LocalTime morn_end_parsed = HRCommandParser.time_parser(morn_end);
                if (morn_end_parsed == null) {
                    System.out.println(print_red("Invalid morning end time."));
                    break;
                }
                System.out.print("Please enter the schedule's evening shifts start time (hh:mm): ");
                String eve_start = scanner.nextLine();
                LocalTime eve_start_parsed = HRCommandParser.time_parser(eve_start);
                if (eve_start_parsed == null) {
                    System.out.println(print_red("Invalid evening start time."));
                    break;
                }
                System.out.print("Please enter the schedule's evening shifts end time (hh:mm): ");
                String eve_end = scanner.nextLine();
                LocalTime eve_end_parsed = HRCommandParser.time_parser(eve_end);
                if (eve_end_parsed == null) {
                    System.out.println(print_red("Invalid evening end time."));
                    break;
                }
                Response res = service.create_weekly_schedule(date_parsed, store, morn_start_parsed, morn_end_parsed, eve_start_parsed, eve_end_parsed);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Created the schedule successfully"));
                }
                System.out.println("=====================================================================================");
            }
            case "13" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the availability store / \"drivers\": ");
                String store = scanner.nextLine();
                Response res = service.limit_employee(id_parsed, date_parsed, shift_parsed, store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Added employee's limitation successfully!"));
                }
                System.out.println("=====================================================================================");
            }
            case "14" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the availability store / \"drivers\": ");
                String store = scanner.nextLine();
                Response res = service.remove_employee_limit(id_parsed, date_parsed, shift_parsed, store);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Removed employee's limitation successfully!"));
                }
                System.out.println("=====================================================================================");
            }
            case "15" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                System.out.print("Please enter a bonus for the employee / 0 if none: ");
                String bonus = scanner.nextLine();
                Double bonus_parsed = HRCommandParser.double_parser(bonus);
                if (bonus_parsed == null) {
                    System.out.println(print_red("Invalid bonus."));
                    break;
                }
                Response res = service.confirm_monthly_salary(id_parsed, bonus_parsed);
                if (res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red(res.getErrorMessage()));
                } else {
                    System.out.println("\n");
                    System.out.println(print_green("Confirmed the employee's salary successfully!"));
                }
                System.out.println("=====================================================================================");
            }
            case "16" -> {
                System.out.println("=====================================================================================");
                System.out.println("\nWhich of the following do you wish to change?");
                System.out.println("1. Salary");
                System.out.println("2. Terms of Employment");
                System.out.print("Option: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        System.out.println("=====================================================================================");
                        System.out.print("Please enter the employee's id: ");
                        String id = scanner.nextLine();
                        Integer id_parsed = HRCommandParser.int_parser(id);
                        if (id_parsed == null) {
                            System.out.println(print_red("Invalid employee id."));
                            break;
                        }
                        System.out.print("Please enter the employee's old salary: ");
                        String old_salary = scanner.nextLine();
                        Double old_salary_parsed = HRCommandParser.double_parser(old_salary);
                        if (old_salary_parsed == null) {
                            System.out.println(print_red("Invalid old salary."));
                            break;
                        }
                        System.out.print("Please enter the employee's new salary: ");
                        String new_salary = scanner.nextLine();
                        Double new_salary_parsed = HRCommandParser.double_parser(new_salary);
                        if (new_salary_parsed == null) {
                            System.out.println(print_red("Invalid new salary."));
                            break;
                        }
                        Response res = service.change_employee_salary(id_parsed, old_salary_parsed, new_salary_parsed);
                        if (res.errorOccurred()) {
                            System.out.println("\n");
                            System.out.println(print_red(res.getErrorMessage()));
                        } else {
                            System.out.println("\n");
                            System.out.println(print_green("Changed the employee's salary successfully"));
                        }
                        System.out.println("=====================================================================================");
                    }
                    case "2" -> {
                        System.out.println("=====================================================================================");
                        System.out.print("Please enter the employee's id: ");
                        String id = scanner.nextLine();
                        Integer id_parsed = HRCommandParser.int_parser(id);
                        if (id_parsed == null) {
                            System.out.println(print_red("Invalid employee id."));
                            break;
                        }
                        System.out.print("Please enter the employee's new terms of employment account: ");
                        String new_terms = scanner.nextLine();
                        Response res = service.change_employee_terms(id_parsed, new_terms);
                        if (res.errorOccurred()) {
                            System.out.println("\n");
                            System.out.println(print_red(res.getErrorMessage()));
                        } else {
                            System.out.println("\n");
                            System.out.println(print_green("Changed the employee's terms of employment successfully"));
                        }
                        System.out.println("=====================================================================================");
                    }
                }
                System.out.println("=====================================================================================");
            }
            case "17" -> {
                System.out.println("=====================================================================================");
                Response res = service.show_employees();
                if (!res.errorOccurred()) {
                    System.out.println("\n");
                    System.out.println(print_red("No employees!"));
                    break;
                }
                System.out.println(res.getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "18" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the employee's id: ");
                String id = scanner.nextLine();
                Integer id_parsed = HRCommandParser.int_parser(id);
                if (id_parsed == null) {
                    System.out.println(print_red("Invalid employee id."));
                    break;
                }
                Response res = service.show_employee_info(id_parsed);
                System.out.println(print_green("Employee's Information:"));
                System.out.println(res.getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "19" -> {
                System.out.println("=====================================================================================");
                System.out.print("Please enter the shift's date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                LocalDate date_parsed = HRCommandParser.date_parser(date);
                if (date_parsed == null) {
                    System.out.println(print_red("Invalid date."));
                    break;
                }
                System.out.print("Please enter the shift's type (morning / evening): ");
                String shift_type = scanner.nextLine();
                ShiftType shift_parsed = HRCommandParser.shift_type_parser(shift_type);
                if (shift_parsed == null) {
                    System.out.println(print_red("Invalid shift type."));
                    break;
                }
                System.out.print("Please enter the availability store / \"drivers\": ");
                String store = scanner.nextLine();
                Response res = service.show_shift_assigned(date_parsed, shift_parsed, store);
                System.out.println(print_green("Shift's employees:"));
                System.out.println(res.getErrorMessage());
                System.out.println("=====================================================================================");
            }
            case "20" -> {
                System.out.println("=====================================================================================");
                Response res = service.logout();
                if (res.errorOccurred()) {
                    System.out.println(res.getErrorMessage());
                }
                System.out.println("=====================================================================================");
            }
        }
    }

    public void employee_options() {
        System.out.println(print_blue("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"));
        System.out.println(print_blue("Choose one of the options:"));
        System.out.println(print_blue("1. Add an availability"));
        System.out.println(print_blue("2. Remove an availability"));
        System.out.println(print_blue("3. Show my shifts"));
        System.out.println(print_blue("4. Show my availability"));
        System.out.println(print_blue("5. Change personal information"));
        System.out.println(print_blue("6. Show my role certifications"));
        System.out.println(print_blue("7. Show my assigned stores"));
        System.out.println(print_blue("8. Show my personal information"));
        System.out.println(print_blue("9. Show my current expected salary"));
        System.out.println(print_blue("10. Cancel a product"));
        System.out.println(print_blue("11. Show scheduled deliveries"));
        System.out.println(print_blue("12. Logout"));
        System.out.println(print_blue("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"));
        System.out.print(print_blue("Option:"));
    }

    public void manager_options() {
        System.out.println(print_blue("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"));
        System.out.println(print_blue("Choose one of the options:"));
        System.out.println(print_blue("1. Assign an employee to a shift"));
        System.out.println(print_blue("2. Remove an employee from a shift"));
        System.out.println(print_blue("3. Add a job certification to an employee"));
        System.out.println(print_blue("4. Remove a job certification from an employee"));
        System.out.println(print_blue("5. Assign an employee to a store"));
        System.out.println(print_blue("6. Remove an employee from a store"));
        System.out.println(print_blue("7. Add an employee"));
        System.out.println(print_blue("8. Remove an existing employee"));
        System.out.println(print_blue("9. Create a new store"));
        System.out.println(print_blue("10. Remove an existing store"));
        System.out.println(print_blue("11. Confirm a shift"));
        System.out.println(print_blue("12. Create a store's weekly schedule"));
        System.out.println(print_blue("13. Limit an employee's work"));
        System.out.println(print_blue("14. Remove an employee's work limitation"));
        System.out.println(print_blue("15. Confirm an employee's monthly salary"));
        System.out.println(print_blue("16. Change an employee's info"));
        System.out.println(print_blue("17. Show employees list"));
        System.out.println(print_blue("18. Show employee's info"));
        System.out.println(print_blue("19. Show shift's assigned employees"));
        System.out.println(print_blue("20. Logout"));
        System.out.println(print_blue("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"));
        System.out.print(print_blue("Option:"));
    }

    /*public void load_data_example() {
        service.add_hr(111111111, "Tomer Naydnov", 12345678, 70, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, true, "123456");
        service.login(111111111, "123456");
        service.create_store("Tel Aviv");
        service.create_store("Beer Sheva");
        service.create_store("Rishon Le Zion");
        service.create_store("Raanana");
        service.create_store("Haifa");
        service.create_store("Ashkelon");
        service.create_store("Ashdod");
        service.create_store("Eilat");
        service.create_store("drivers");
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Tel Aviv", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Beer Sheva", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Rishon Le Zion", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Raanana", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Haifa", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Ashkelon", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Ashdod", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Eilat", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "drivers", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.add_employee(222222222, "Agni Rudolf", 2222222, 30, "None", LocalDate.of(2023, 1, 1), FamilyStatus.WIDOWED, false, "123456");
        service.assign_to_store(222222222,"Tel Aviv");
        service.assign_to_store(222222222,"Beer Sheva");
        service.assign_to_store(222222222,"Ashdod");
        service.certify_role(222222222, JobType.STOREKEEPER);
        service.certify_role(222222222, JobType.CASHIER);
        service.add_employee(333333333, "Cindi Polly", 3333333, 32, "Signed for a year", LocalDate.of(2023, 1, 1), FamilyStatus.MARRIED, true, "123456");
        service.assign_to_store(333333333,"Tel Aviv");
        service.certify_role(333333333, JobType.SHIFTMANAGER);
        service.certify_role(333333333, JobType.CASHIER);
        service.add_employee(444444444, "Peggy Sara", 4444444, 44, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, true, "123456");
        service.assign_to_store(444444444,"Tel Aviv");
        service.assign_to_store(444444444,"Beer Sheva");
        service.certify_role(444444444, JobType.SHIFTMANAGER);
        service.certify_role(444444444, JobType.GENERAL);
        service.certify_role(444444444, JobType.USHER);
        service.add_employee(555555555, "Harel Jaffe", 5555555, 35, "None", LocalDate.of(2023, 1, 1), FamilyStatus.DIVORCED, false, "123456");
        service.assign_to_store(555555555,"Eilat");
        service.certify_role(555555555, JobType.CLEANING);
        service.add_employee(666666666, "Tyrell Isla", 6666666, 36, "None", LocalDate.of(2023, 1, 1), FamilyStatus.MARRIED, true, "123456");
        service.assign_to_store(666666666,"Haifa");
        service.certify_role(666666666, JobType.CLEANING);
        service.add_employee(777777777, "Kailey Janeka", 7777777, 35, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, true, "123456");
        service.assign_to_store(777777777,"Raanana");
        service.assign_to_store(777777777,"Rishon Le Zion");
        service.certify_role(777777777, JobType.SECURITY);
        service.add_employee(888888888, "Suz Kerena", 8888888, 43, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, true, "123456");
        service.assign_to_store(888888888,"Rishon Le Zion");
        service.certify_role(888888888, JobType.SECURITY);
        service.add_employee(122222222, "Kfir Sara", 1222222, 30, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, false, "123456");
        service.assign_to_store(122222222,"Raanana");
        service.assign_to_store(122222222,"Tel Aviv");
        service.certify_role(122222222, JobType.GENERAL);
        service.certify_role(122222222, JobType.SHIFTMANAGER);
        service.add_employee(166666666, "Roy Marcy", 1666666, 35, "None", LocalDate.of(2023, 1, 1), FamilyStatus.WIDOWED, false, "123456");
        service.assign_to_store(166666666,"Ashkelon");
        service.assign_to_store(166666666,"Ashdod");
        service.certify_role(166666666, JobType.SECURITY);
        service.add_employee(177777777, "Lynton Miriam", 1777777, 36, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, true, "123456");
        service.assign_to_store(177777777,"Ashkelon");
        service.assign_to_store(177777777,"Eilat");
        service.certify_role(177777777, JobType.CLEANING);
        service.add_employee(188888888, "Allycia Beverley", 1888888, 37, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, true, "123456");
        service.assign_to_store(188888888,"Tel Aviv");
        service.assign_to_store(188888888,"Ashdod");
        service.certify_role(188888888, JobType.GENERAL);
        service.add_employee(199999999, "Rickie Lavina", 1999999, 37, "None", LocalDate.of(2023, 1, 1), FamilyStatus.MARRIED, false, "123456");
        service.assign_to_store(199999999,"Haifa");
        service.certify_role(199999999, JobType.GENERAL);
        service.add_employee(211111111, "Rica Marie", 2111111, 34, "None", LocalDate.of(2023, 1, 1), FamilyStatus.DIVORCED, true, "123456");
        service.assign_to_store(211111111,"Ashdod");
        service.certify_role(211111111, JobType.CASHIER);
        service.certify_role(211111111, JobType.STOREKEEPER);
        service.add_employee(233333333, "Maeghan Steve", 2333333, 31, "None", LocalDate.of(2023, 1, 1), FamilyStatus.WIDOWED, true, "123456");
        service.assign_to_store(233333333,"Raanana");
        service.certify_role(233333333, JobType.CLEANING);
        service.add_employee(244444444, "Jaron Ambrose", 2444444, 32, "None", LocalDate.of(2023, 1, 1), FamilyStatus.MARRIED, false, "123456");
        service.assign_to_store(244444444,"Rishon Le Zion");
        service.certify_role(244444444, JobType.STOREKEEPER);
        service.certify_role(244444444, JobType.SHIFTMANAGER);
        service.certify_role(244444444, JobType.CASHIER);
        service.add_employee(266666666, "Alison Clarissa", 2666666, 47, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, false, "123456");
        service.assign_to_store(266666666,"Ashkelon");
        service.certify_role(266666666, JobType.SHIFTMANAGER);
        service.add_employee(288888888, "Terra Minta", 2888888, 46, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, false, "123456");
        service.assign_to_store(288888888,"Beer Sheva");
        service.certify_role(288888888, JobType.SHIFTMANAGER);
        service.add_employee(299999999, "Luanna Mansel", 2999999, 30, "None", LocalDate.of(2023, 1, 1), FamilyStatus.DIVORCED, true, "123456");
        service.assign_to_store(299999999,"Beer Sheva");
        service.certify_role(299999999, JobType.CLEANING);
        service.logout();
        service.login(222222222, "123456");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Beer Sheva");
        service.logout();
        service.login(111111111, "123456");
        service.assign_to_shift(222222222, LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Tel Aviv", JobType.STOREKEEPER);
        service.logout();
        service.login(333333333, "123456");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.MORNING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "Tel Aviv");
        service.logout();
        service.login(444444444, "123456");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.MORNING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.EVENING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "Beer Sheva");
        service.logout();
        service.login(555555555, "123456");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Eilat");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Eilat");
        service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.MORNING, "Eilat");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Eilat");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.EVENING, "Eilat");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "Eilat");
        service.logout();
        service.login(666666666, "123456");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.EVENING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "Haifa");
        service.logout();
        service.login(777777777, "123456");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.MORNING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.EVENING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Rishon Le Zion");
        service.logout();
        service.login(888888888, "123456");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Rishon Le Zion");
        service.logout();
        service.login(122222222, "123456");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Tel Aviv");
        service.logout();
        service.login(166666666, "123456");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.EVENING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Ashdod");
        service.logout();
        service.login(177777777, "123456");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.MORNING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.MORNING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Eilat");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.EVENING, "Eilat");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Eilat");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Eilat");
        service.logout();
        service.login(188888888, "123456");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Tel Aviv");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Tel Aviv");
        service.logout();
        service.login(199999999, "123456");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Haifa");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Haifa");
        service.logout();
        service.login(211111111, "123456");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Ashdod");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Ashdod");
        service.logout();
        service.login(233333333, "123456");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Raanana");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Raanana");
        service.logout();
        service.login(244444444, "123456");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.EVENING, "Rishon Le Zion");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Rishon Le Zion");
        service.logout();
        service.login(266666666, "123456");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Ashkelon");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Ashkelon");
        service.logout();
        service.login(288888888, "123456");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.EVENING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "Beer Sheva");
        service.logout();
        service.login(299999999, "123456");
        service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.EVENING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "Beer Sheva");
        service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.MORNING, "Beer Sheva");
        service.logout();

        //generateDrivers(40);

    }
    */
    public void generateDrivers(int numberOfDrivers) {
        Set<String> usedIds = new HashSet<>();
        Random random = new Random();
        //DriverDAO driverDAO = new DriverDAO();
        //driverDAO.deleteAllDrivers();

        for (int i = 0; i < numberOfDrivers; i++) {
            service.login(111111111, "123456");
            // Generate random driver data

            String[] names = {"Noam", "Avigail", "Eliav", "Talia", "Adi", "Gal", "Lior", "Shira", "Itay", "Tamar", "Omer", "Maya", "Yaniv", "Noa", "Yotam", "Dana", "Yael", "Nir", "Liat", "Eran", "Neta", "Yaron", "Yifat", "Guy", "Nitzan", "Shlomi", "Revital", "Nadav", "Inbar", "Nir", "Shiri", "Erez", "Maayan", "Ori", "Roni", "Ran", "Dikla", "Yonatan", "Mor", "Or", "Sivan", "Ofer", "Yuval", "Rivka", "Shimon", "Alon", "Avi", "Gali", "Amir", "Ofir", "Dor", "Lena", "Yishai", "Adva", "Omri", "Lilach", "Asaf", "Michal", "Idan", "Hadar", "Ido", "Oded", "Rina", "Sharon", "Uri", "Tova", "Yoav", "Shani", "Amit", "Ayelet", "Hila", "Niv", "Orly", "Amos", "Keren", "Tzvika", "Dafna", "Yair", "Limor", "Eitan", "Galit", "Shlomit", "Ziv", "Zohar", "Naama", "Tomer", "Yehuda", "Rachel", "Yarden", "Yehudit", "Eli", "Anat", "Avraham", "Haim", "Yehoshua", "Naomi", "Yair", "Shmuel", "Eran", "Adi", "Nati", "Yael", "Yaniv", "Liat", "Boaz", "Ronit", "Shay", "Miri", "Yitzhak", "Yonit", "Dudu", "Adina", "Ari", "Efrat", "Shaul", "Anat", "Avner", "Moriah", "Galina", "Ariel", "Elior", "Bar", "Aya", "Oded", "Hila", "Nissim", "Nava", "Nimrod", "Shaked", "Nitzan", "Dina", "Ofra", "Haim", "Anat", "Hanan", "Lital", "Natan", "Avital", "Yuval", "Eliana", "Elad", "Dror", "Eitan", "Liora", "Ayelet", "Hadas", "Tuvia", "Rami", "Roni", "Ehud", "Lilach", "Tali", "Tzipora", "Almog", "Moria", "Hodaya"};

            String name = names[random.nextInt(names.length)];
            int bank_account = 1111111;
            int salary = 30;
            LocalDate employment = LocalDate.of(2023, 1, 1);
            String phone = "555-555-" + String.format("%04d", i);

            // Generate unique driver ID
            String id;
            do {
                id = String.format("%09d", random.nextInt(1000000000));
                // add 0 to the left if needed
            } while (usedIds.contains(id) || id.length() != 9);
            usedIds.add(id);
            service.add_employee(Integer.parseInt(id), name, bank_account, salary, "None", employment, FamilyStatus.SINGLE, true, "123456");
            service.assign_to_store(Integer.parseInt(id), "drivers");
            Random random1 = new Random();
            int x = random1.nextInt(2);
            int y = random1.nextInt(2);
            boolean x1 = x != 0;
            boolean x2 = y != 0;
            int weightAllowed = random1.nextInt(4,40);
            service.certify_driver(Integer.parseInt(id), phone, weightAllowed, x1, x2);
            service.logout();
            service.login(Integer.parseInt(id), "123456");
            service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.MORNING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 2), ShiftType.EVENING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.MORNING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 3), ShiftType.EVENING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.MORNING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 4), ShiftType.EVENING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.MORNING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 5), ShiftType.EVENING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.MORNING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 6), ShiftType.EVENING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.MORNING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 7), ShiftType.EVENING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.MORNING, "drivers");
            service.add_availability(LocalDate.of(2023, 7, 8), ShiftType.EVENING, "drivers");
            service.logout();

        }
    }

    public void load_example_data() {
        service.add_hr(111111111, "Tomer Naydnov", 12345678, 70, "None", LocalDate.of(2023, 1, 1), FamilyStatus.SINGLE, true, "123456");
        service.login(111111111, "123456");
        service.create_store("Tel Aviv");
        service.create_store("Beer Sheva");
        service.create_store("Rishon Le Zion");
        service.create_store("Raanana");
        service.create_store("Haifa");
        service.create_store("Ashkelon");
        service.create_store("Ashdod");
        service.create_store("Eilat");
        service.create_store("drivers");
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Tel Aviv", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Beer Sheva", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Rishon Le Zion", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Raanana", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Haifa", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Ashkelon", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Ashdod", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "Eilat", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));
        service.create_weekly_schedule(LocalDate.of(2023,7,2), "drivers", LocalTime.of(8,0), LocalTime.of(14,0), LocalTime.of(14,0), LocalTime.of(22,0));

        generateEmployees(100);
        generateDrivers(60);
        assign_employees();
    }

    private void assign_employees() {
        service.login(111111111, "123456");
        String[] Stores = {"Tel Aviv", "Beer Sheva", "Rishon Le Zion", "Raanana", "Haifa", "Ashkelon", "Ashdod", "Eilat"};
        for (String store: Stores) {
            assignShiftsToEmployees(store, LocalDate.of(2023, 7, 2));
        }
        service.logout();
    }

    private void generateEmployees(int numberOfEmployees) {
        Set<String> usedIds = new HashSet<>();
        Random random = new Random();

        for (int i = 0; i < numberOfEmployees; i++) {
            service.login(111111111, "123456");
            // Generate random driver data
            String name = "Employee " + i;
            int bank_account = 1111111;
            int salary = 30;
            LocalDate employment = LocalDate.of(2023, 1, 1);

            // Generate unique driver ID
            String id;
            do {
                id = String.format("%09d", random.nextInt(1000000000));
                // add 0 to the left if needed
            } while (usedIds.contains(id));
            usedIds.add(id);
            service.add_employee(Integer.parseInt(id), name, bank_account, salary, "None", employment, FamilyStatus.SINGLE, true, "123456");
            service.assign_to_store(Integer.parseInt(id), "drivers");
            String[] Stores = {"Tel Aviv", "Beer Sheva", "Rishon Le Zion", "Raanana", "Haifa", "Ashkelon", "Ashdod", "Eilat"};
            List<JobType> jobs = getRandomJobs(4);
            for (JobType job: jobs) {
                service.certify_role(Integer.parseInt(id), job);
            }
            List<String> stores = getRandomStores(5, Stores);
            for (String store: stores) {
                service.assign_to_store(Integer.parseInt(id), store);
            }
            service.logout();
            service.login(Integer.parseInt(id), "123456");
            for (String store: stores) {
                add_availability(store, LocalDate.of(2023, 7, 2), LocalDate.of(2023, 7, 8));
            }
            service.logout();
        }
    }

    public static List<JobType> getRandomJobs(int num) {
        Random random = new Random();
        List<JobType> jobs = new LinkedList<>();
        JobType[] all_jobs = JobType.values();
        Set<Integer> usedIndexes = new HashSet<>();
        for (int i = 0; i < num; i++) {
            int index;
            do {
                index = random.nextInt(all_jobs.length);
            } while (usedIndexes.contains(index) || all_jobs[index] == JobType.DRIVER || all_jobs[index] == JobType.HRMANAGER);
            jobs.add(all_jobs[index]);
            usedIndexes.add(index);
        }
        return jobs;
    }

    public static List<String> getRandomStores(int num, String[] stores) {
        Random random = new Random();
        List<String> selected_stores = new LinkedList<>();
        Set<Integer> usedIndexes = new HashSet<>();
        for (int i = 0; i < num; i++) {
            int index;
            do {
                index = random.nextInt(stores.length);
                selected_stores.add(stores[index]);
            } while (usedIndexes.contains(index));
            usedIndexes.add(index);
        }
        return selected_stores;
    }
    
    public void add_availability(String store, LocalDate week_start, LocalDate week_end) {
        Random random = new Random();
        ShiftType[] all_shifts = ShiftType.values();
        int count = 0;
        for (int i = week_start.getDayOfMonth(); i < week_end.getDayOfMonth(); i++) {
            int index = random.nextInt(all_shifts.length);
            service.add_availability(week_start.plusDays(count), all_shifts[index], store);
            count++;
        }
    }

    public void assignShiftsToEmployees(String store, LocalDate week_start) {
        Random random = new Random();
        List<ShiftPair> availableShifts = ShiftController.getInstance().get_shifts_pairs(store, week_start);
        for (ShiftPair shift : availableShifts) {
            List<Integer> availableEmployees = ShiftController.getInstance().get_availables(store, shift);

            // Remove employees who have already reached the maximum shift count
            availableEmployees.removeIf(employee -> service.getAssignedShiftsDates(employee, week_start).size() >= 6);

            if (availableEmployees.isEmpty()) {
                // No available employees for this shift
                continue;
            }

            LocalDate shiftDate = shift.getDate();

            // Remove employees who already have a shift on the same day
            availableEmployees.removeIf(employee ->
                    service.getAssignedShiftsDates(employee, week_start).stream()
                            .anyMatch(assignedShift -> assignedShift.getDate().equals(shiftDate))
            );

            if (availableEmployees.isEmpty()) {
                // No available employees for this shift date
                continue;
            }

            int num_of_assigns = random.nextInt(10);
            for (int i = 0; i < num_of_assigns; i++) {
                int employee_index = random.nextInt(availableEmployees.size());
                int employee_id = availableEmployees.get(employee_index);
                List<JobType> jobs = EmployeeController.getInstance().get_certified_roles(employee_id);
                if (jobs.size() == 0) {
                    continue;
                }
                int jobs_index = random.nextInt(jobs.size()); // TODO: throws exception if no jobs
                service.assign_to_shift(employee_id, shift.getDate(), shift.getType(), store, jobs.get(jobs_index));
                availableEmployees.remove(employee_index);
                if (availableEmployees.isEmpty()) {
                    break;
                }
            }
        }
    }


}
