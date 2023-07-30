package HR.PresentationLayer.GUI.View;

import HR.PresentationLayer.GUI.Model.MainMenuModel;

import java.util.List;

public class MainMenuFrame extends AbstractFrame{
    public MainMenuFrame(List<String> jobs) {
        super(4, new MainMenuModel());
        if (jobs.contains("HRMANAGER")) {
            addButton("Assign an employee to a shift");
            addButton("Remove an employee from a shift");
            addButton("Add a job certification to an employee");
            addButton("Remove a job certification from an employee");
            addButton("Assign an employee to a store");
            addButton("Remove an employee from a store");
            addButton("Add an employee");
            addButton("Remove an existing employee");
            addButton("Create a new store");
            addButton("Remove an existing store");
            addButton("Confirm a shift");
            addButton("Create a store's weekly schedule");
            addButton("Limit an employee's work");
            addButton("Remove an employee's work limitation");
            addButton("Confirm an employee's monthly salary");
            addButton("Change an employee's salary");
            addButton("Change an employee's terms of employment");
            addButton("Show employees list");
            addButton("Show employee's info");
            addButton("Show shift's assigned employees");
            addButton("Logout");
        }
        else {
            if (jobs.contains("STOREKEEPER")) {
                addButton("Show scheduled deliveries");
            }

            if (jobs.contains("SHIFTMANAGER")) {
                addButton("Cancel a product");
            }

            addButton("Add an availability");
            addButton("Remove an availability");
            addButton("Show my shifts");
            addButton("Show my availability");
            addButton("Show my role certifications");
            addButton("Show my assigned stores");
            addButton("Show my personal information");
            addButton("Show my current expected salary");
            addButton("Change name");
            addButton("Change bank account");
            addButton("Change family status");
            addButton("Change student status");
            addButton("Logout");

        }
    }
}
