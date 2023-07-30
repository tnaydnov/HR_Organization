package HR.PresentationLayer.GUI.Model;

import HR.PresentationLayer.GUI.View.LoginFrame;
import HR.PresentationLayer.GUI.View.MainMenuFrame;
import HR.ServiceLayer.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static HR.PresentationLayer.HRCommandParser.int_parser;

public class LoginModel extends AbstractModel {

    public LoginModel() {
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton sourceButton) {

            if (sourceButton.getText().equals("Login")) {
                String username = ((LoginFrame)relatedFrame).getUsername();
                String password = ((LoginFrame)relatedFrame).getPassword();

                String login = isValidCredentials(username, password);
                if (login.equals("")) {
                    relatedFrame.clearError();
                    relatedFrame.clearInfo();
                    relatedFrame.dispose();
                    List<String> jobs = employeeService.getJobs(int_parser(username));
                    new MainMenuFrame(jobs);
                }
                else {
                    relatedFrame.displayError(login);
                }
            }
        }
    }

    private String isValidCredentials(String username, String password) {
        Integer id = int_parser(username);
        if (id != null) {
            Response login = employeeService.login(id, password);
            return login.getErrorMessage();
        }
        else { //Invalid id
            return "Invalid id";
        }
    }
}
