package HR.PresentationLayer;

import java.sql.SQLException;

public class Menu {
    private final HRMenu employeeCLI = new HRMenu();

    public void run() {
        employeeCLI.run();
    }

    private void load_data_example() {
        employeeCLI.load_example_data();
    }

    public static void main(String[] args) throws SQLException {
        //Connect.getInstance().deleteRecordsOfTables();
        Menu m = new Menu();
        //m.load_data_example();
        m.run();
    }
}
