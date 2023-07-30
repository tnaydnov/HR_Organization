package HR.DataAccessLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author sqlitetutorial.net
 */
public class Connect {
    public Connection conn;
    public final String url = "jdbc:sqlite:HR_Deliveries_Database.db";
    private static Connect instance;

    public static Connect getInstance() {
        if (instance == null) {
            instance = new Connect();
        }
        return instance;
    }

    private Connect() {
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            createTables();

        } catch (SQLException ignored) {
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    public void createTables() throws SQLException {
        try (Statement statement = createStatement()) {
            String query = """
                    CREATE TABLE IF NOT EXISTS Employees (
                    id INTEGER,
                    name VARCHAR(200),
                    password VARCHAR(200),
                    salary REAL,
                    termsOfEmployment VARCHAR(255),
                    familyStatus VARCHAR(15),
                    isStudent INTEGER,
                    bankAccount INTEGER,
                    employmentDate DATE,
                    currentSalary REAL,
                    PRIMARY KEY (id)
                    );""";
            statement.execute(query);
            query = """
                    CREATE TABLE IF NOT EXISTS EmployeeRoles (
                    id INTEGER,
                    jobType VARCHAR(30),
                    FOREIGN KEY (id) REFERENCES Employees(id) ON DELETE CASCADE);""";
            statement.execute(query);
            query = """
                    CREATE TABLE IF NOT EXISTS EmployeeStores (
                    id INTEGER,
                    store VARCHAR(30),
                    FOREIGN KEY (id) REFERENCES Employees(id) ON DELETE CASCADE);""";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Shifts (" +
                    "store VARCHAR(20)," +
                    "shiftType VARCHAR(8)," + //MORNING/EVENING
                    "day VARCHAR(2)," +
                    "month VARCHAR(2)," +
                    "year VARCHAR(4)," +
                    "start VARCHAR(6)," +
                    "end VARCHAR(6)," +
                    "confirmed INTEGER," +
                    "PRIMARY KEY (store, shiftType, day, month, year)" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS EmployeeShifts (" +
                    "store VARCHAR(20)," +
                    "shiftType VARCHAR(8)," + //MORNING/EVENING
                    "day VARCHAR(2)," +
                    "month VARCHAR(2)," +
                    "year VARCHAR(4)," +
                    "start VARCHAR(6)," +
                    "end VARCHAR(6)," +
                    "confirmed INTEGER," +
                    "PRIMARY KEY (store, shiftType, day, month, year)" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS EmployeesInShift(" +
                    "store VARCHAR(20)," +
                    "shiftType VARCHAR(8)," + // MORNING/EVENING
                    "day VARCHAR(2)," +
                    "month VARCHAR(2)," +
                    "year VARCHAR(4)," +
                    "employeeID INTEGER," +
                    "job VARCHAR(30)," +
                    "PRIMARY KEY (employeeID, store, shiftType, day, month, year)," +
                    "FOREIGN KEY (store, shiftType, day, month, year) REFERENCES Shifts(store, shiftType, day, month, year)" +
                    " ON DELETE CASCADE," +
                    "FOREIGN KEY (employeeID) REFERENCES Employees(id) ON DELETE CASCADE" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Availability(" +
                    "store VARCHAR(20)," +
                    "shiftType VARCHAR(8)," + // MORNING/EVENING
                    "day VARCHAR(2)," +
                    "month VARCHAR(2)," +
                    "year VARCHAR(4)," +
                    "employeeID INTEGER," +
                    "availabilityOrConstraint INTEGER," + //Indicator of 1 - availability, 0 - manager constraint
                    "PRIMARY KEY (store, shiftType, day, month, year, employeeID)," +
                    "FOREIGN KEY (store, shiftType, day, month, year) REFERENCES Shifts(store, shiftType, day, month, year)" +
                    " ON DELETE CASCADE," +
                    "FOREIGN KEY (employeeID) REFERENCES Employees(id) ON DELETE CASCADE" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS ShiftEvents(" +
                    "store VARCHAR(20)," +
                    "shiftType VARCHAR(8)," + // MORNING/EVENING
                    "day VARCHAR(2)," +
                    "month VARCHAR(2)," +
                    "year VARCHAR(4)," +
                    "employeeId INTEGER," +
                    "productId INTEGER," +
                    "FOREIGN KEY (store, shiftType, day, month, year) REFERENCES Shifts(store, shiftType, day, month, year)" +
                    " ON DELETE CASCADE" +
                    ")";
            statement.execute(query);
            query = """
                    CREATE TABLE IF NOT EXISTS "Drivers" (
                    	"driver_id"	TEXT NOT NULL UNIQUE,
                    	"driver_name"	TEXT NOT NULL,
                    	"phone"	TEXT,
                    	PRIMARY KEY("driver_id"),
                    	FOREIGN KEY("driver_id") REFERENCES "Drivers"("driver_id")
                    )""";
            statement.execute(query);

            query = """
                    CREATE TABLE IF NOT EXISTS "DeliveryForms" (
                        "form_id"	TEXT UNIQUE,
                        "driver_id"	TEXT,
                        "truck_license_plate"	TEXT,
                        "dispatch_time"	TEXT,
                        "termination_time"	TEXT,
                        "status"	TEXT,
                        "origin"	TEXT NOT NULL,
                        FOREIGN KEY("truck_license_plate") REFERENCES "Trucks"("license_plate"),
                        FOREIGN KEY("driver_id") REFERENCES "Drivers"("driver_id"),
                        PRIMARY KEY("form_id")
                    )""";
            statement.execute(query);

            query = """
                    CREATE TABLE IF NOT EXISTS "DeliveryStops" (
                    	"stop_id"	INTEGER NOT NULL,
                    	"origin_name"	TEXT,
                    	"destination_name"	TEXT,
                    	"truck_type"	TEXT,
                    	"status"	TEXT,
                    	"form_id"	INTEGER,
                    	FOREIGN KEY("origin_name") REFERENCES "Sites"("name"),
                    	FOREIGN KEY("destination_name") REFERENCES "Sites"("name"),
                    	FOREIGN KEY("form_id") REFERENCES "DeliveryForms"("form_id"),
                    	PRIMARY KEY("stop_id")
                    )""";
            statement.execute(query);

            query = """
                    CREATE TABLE IF NOT EXISTS "DriverLicenses" (
                        "driver_id"	INTEGER NOT NULL,
                        "weight_allowed_tons"	INTEGER NOT NULL,
                        "regular_allowed"	INTEGER NOT NULL CHECK("regular_allowed" IN (0, 1)),
                        "refrigerated_allowed"	INTEGER NOT NULL CHECK("refrigerated_allowed" IN (0, 1)),
                        UNIQUE("driver_id"),
                        FOREIGN KEY("driver_id") REFERENCES "Drivers"("driver_id"),
                        PRIMARY KEY("driver_id")
                    )
                    """;
            statement.execute(query);

            query = """
                    CREATE TABLE IF NOT EXISTS "Items" (
                    	"stop_id"	INTEGER NOT NULL,
                    	"item_name"	TEXT NOT NULL,
                    	"quantity"	INTEGER NOT NULL,
                    	PRIMARY KEY("stop_id","item_name")
                    )""";
            statement.execute(query);

            query = """
                    CREATE TABLE IF NOT EXISTS "Sites" (
                    	"name"	TEXT,
                    	"address"	TEXT,
                    	"contact_name"	TEXT,
                    	"contact_phone"	TEXT,
                    	"delivery_zone"	INTEGER,
                    	PRIMARY KEY("name")
                    )""";
            statement.execute(query);

            query = """
                    CREATE TABLE IF NOT EXISTS "Trucks" (
                    	"license_plate"	TEXT NOT NULL,
                    	"max_weight_tons"	INTEGER NOT NULL,
                    	"truck_type"	TEXT NOT NULL,
                    	"model"	TEXT NOT NULL,
                    	"net_weight_tons"	INTEGER NOT NULL,
                    	PRIMARY KEY("license_plate")
                    )""";
            statement.execute(query);

        } finally {
            closeConnect();
        }
    }

    public Statement createStatement() throws SQLException {
        conn = DriverManager.getConnection(url);
        return conn.createStatement();
    }

    public void closeConnect() throws SQLException {
        conn.close();
    }


    public List<HashMap<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String, Object>> list = new ArrayList<>();

        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    public List<HashMap<String, Object>> executeQuery(String query, Object... params) throws SQLException {
        try {
            createStatement();
            PreparedStatement statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++)
                statement.setObject(i + 1, params[i]);
            ResultSet rs = statement.executeQuery();
            return convertResultSetToList((rs));
        } finally {
            closeConnect();
        }
    }


    public void executeUpdate(String query, Object... params) throws SQLException {
        try {
            createStatement();
            PreparedStatement statement = conn.prepareStatement("PRAGMA foreign_keys = ON");
            statement.executeUpdate();
            statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++)
                statement.setObject(i + 1, params[i]);
            statement.executeUpdate();
        } finally {
            closeConnect();
        }
    }

    public void deleteRecordsOfTables() throws SQLException {
        try (Statement stmt = createStatement()) {

            String query = "DROP TABLE IF EXISTS Employees";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS EmployeesInShift";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS EmployeeRoles";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Availability";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Shifts";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS EmployeeStores";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS ShiftEvents";
            stmt.execute(query);
            createTables();


        } finally {
            closeConnect();
        }

    }
}
