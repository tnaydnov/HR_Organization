package HR.PresentationLayer.GUI.Model;

import Deliveries.BusinessLayer.DeliveryFormsController;
import Deliveries.BusinessLayer.DeliveryManagerImpl;
import HR.PresentationLayer.GUI.View.ActionFrame;
import HR.PresentationLayer.GUI.View.InformationFrame;
import HR.PresentationLayer.GUI.View.LoginFrame;

public class MainMenuModel extends AbstractModel{

    public MainMenuModel() {
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getActionCommand().equals("Assign an employee to a shift")) {
            new ActionFrame("Assign an employee to a shift", 5, new String[]{"Employee ID", "Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\"", "Role"});
        } else if (e.getActionCommand().equals("Remove an employee from a shift")) {
            new ActionFrame("Remove an employee from a shift", 5, new String[]{"Employee ID", "Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\"", "Role"});
        } else if (e.getActionCommand().equals("Add a job certification to an employee")) {
            new ActionFrame("Add a job certification to an employee", 2, new String[]{"Employee ID", "Role"});
        } else if (e.getActionCommand().equals("Certify a driver")) {
            new ActionFrame("Certify a driver", 5, new String[]{"Employee ID", "Phone", "Available Max Weight", "Regular Truck Permissions", "refrigerated Truck Permission"});
        } else if (e.getActionCommand().equals("Remove a job certification from an employee")) {
            new ActionFrame("Remove a job certification from an employee", 2, new String[]{"Employee ID", "Role"});
        } else if (e.getActionCommand().equals("Assign an employee to a store")) {
            new ActionFrame("Assign an employee to a store", 2, new String[]{"Employee ID", "Store"});
        } else if (e.getActionCommand().equals("Remove an employee from a store")) {
            new ActionFrame("Remove an employee from a store", 2, new String[]{"Employee ID", "Store"});
        } else if (e.getActionCommand().equals("Add an employee")) {
            new ActionFrame("Add an employee", 9, new String[]{"Employee ID", "Employee Name", "Bank Account Number", "Salary", "Terms of Employment", "Employment Date (dd-mm-yyyy)", "Family Status", "Is the employee a student?", "Password"});
        } else if (e.getActionCommand().equals("Remove an existing employee")) {
            new ActionFrame("Remove an existing employee", 1, new String[]{"Employee ID"});
        } else if (e.getActionCommand().equals("Create a new store")) {
            new ActionFrame("Create a new store", 1, new String[]{"Store"});
        } else if (e.getActionCommand().equals("Remove an existing store")) {
            new ActionFrame("Remove an existing store", 1, new String[]{"Store"});
        } else if (e.getActionCommand().equals("Confirm a shift")) {
            new ActionFrame("Confirm a shift", 3, new String[]{"Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\""});
        } else if (e.getActionCommand().equals("Create a store's weekly schedule")) {
            new ActionFrame("Create a store's weekly schedule", 6, new String[]{"Week's First Date (dd-mm-yyyy)", "store / \"drivers\"", "Morning Start Time (hh:mm)", "Morning End Time (hh:mm)", "Evening Start Time (hh:mm)", "Evening End Time (hh:mm)"});
        } else if (e.getActionCommand().equals("Limit an employee's work")) {
            new ActionFrame("Limit an employee's work", 4, new String[]{"Employee ID", "Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\""});
        } else if (e.getActionCommand().equals("Remove an employee's work limitation")) {
            new ActionFrame("Remove an employee's work limitation", 4, new String[]{"Employee ID", "Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\""});
        } else if (e.getActionCommand().equals("Confirm an employee's monthly salary")) {
            new ActionFrame("Confirm an employee's monthly salary", 2, new String[]{"Employee ID", "Bonus"});
        } else if (e.getActionCommand().equals("Change an employee's salary")) {
            new ActionFrame("Change an employee's salary", 3, new String[]{"Employee ID", "Old Salary", "New Salary"});
        } else if (e.getActionCommand().equals("Change an employee's terms of employment")) {
            new ActionFrame("Change an employee's terms of employment", 2, new String[]{"Employee ID", "Terms of Employment"});
        } else if (e.getActionCommand().equals("Show employees list")) {
            new InformationFrame(employeeService.show_employees().getErrorMessage());
        } else if (e.getActionCommand().equals("Show employee's info")) {
            new ActionFrame("Show employee's info", 1, new String[]{"Employee ID"});
        } else if (e.getActionCommand().equals("Show shift's assigned employees")) {
            new ActionFrame("Show shift's assigned employees", 3, new String[]{"Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\""});
        } else if (e.getActionCommand().equals("Logout")) {
            employeeService.logout();
            relatedFrame.clearError();
            relatedFrame.clearInfo();
            relatedFrame.dispose();
            new LoginFrame();
        } else if (e.getActionCommand().equals("Show scheduled deliveries")) {
            new ActionFrame("Show scheduled deliveries", 3, new String[]{"Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\""});
        } else if (e.getActionCommand().equals("Cancel a product")) {
            new ActionFrame("Cancel a product", 4, new String[]{"Product ID", "Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\""});
        } else if (e.getActionCommand().equals("Add an availability")) {
            new ActionFrame("Add an availability", 3, new String[]{"Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\""});
        } else if (e.getActionCommand().equals("Remove an availability")) {
            new ActionFrame("Remove an availability", 3, new String[]{"Shift's Date (dd-mm-yyyy)", "Shift's Type (morning / evening)", "store / \"drivers\""});
        } else if (e.getActionCommand().equals("Show my shifts")) {
            new InformationFrame(employeeService.get_shifts().getErrorMessage());
        } else if (e.getActionCommand().equals("Show my availability")) {
            new InformationFrame(employeeService.get_availability().getErrorMessage());
        } else if (e.getActionCommand().equals("Change name")) {
            new ActionFrame("Change name", 2, new String[]{"Old Name", "New Name"});
        } else if (e.getActionCommand().equals("Change bank account")) {
            new ActionFrame("Change bank account", 2, new String[]{"Old Bank Account", "New Bank Account"});
        } else if (e.getActionCommand().equals("Change family status")) {
            new ActionFrame("Change family status", 2, new String[]{"Old Family Status", "New Family Status"});
        } else if (e.getActionCommand().equals("Change student status")) {
            new ActionFrame("Change student status", 2, new String[]{"Were you a student before? (yes / no)", "Are you a student now? (yes / no)"});
        } else if (e.getActionCommand().equals("Show my role certifications")) {
            new InformationFrame(employeeService.show_role_certifications().getErrorMessage());
        } else if (e.getActionCommand().equals("Show my assigned stores")) {
            new InformationFrame(employeeService.show_assigned_stores().getErrorMessage());
        } else if (e.getActionCommand().equals("Show my personal information")) {
            new InformationFrame(employeeService.show_personal_info().getErrorMessage());
        } else if (e.getActionCommand().equals("Show my current expected salary")) {
            new InformationFrame(employeeService.show_current_salary().getErrorMessage());
        }
    }
}
