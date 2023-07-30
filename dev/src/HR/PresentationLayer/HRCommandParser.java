package HR.PresentationLayer;

import HR.BusinessLayer.FamilyStatus;
import HR.BusinessLayer.JobType;
import HR.BusinessLayer.ShiftType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HRCommandParser {

    public static ShiftType shift_type_parser(String shift_type) {
        try {
            return ShiftType.valueOf(shift_type.toUpperCase());
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public static FamilyStatus family_status_parser(String family_status) {
        try {
            return FamilyStatus.valueOf(family_status.toUpperCase());
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public static JobType job_type_parser(String job_type) {
        try {
            return JobType.valueOf(job_type.toUpperCase());
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public static Object student_parser(String is_student) {
        if (is_student.equalsIgnoreCase("yes") || is_student.equalsIgnoreCase("true")) {
            return true;
        }

        else if (is_student.equalsIgnoreCase("no") || is_student.equalsIgnoreCase("false")) {
            return false;
        }
        return null;
    }

    public static Integer int_parser(String num) {
        try {
            return Integer.parseInt(num);
        }
        catch (NumberFormatException exception) {
            return null;
        }
    }

    public static Double double_parser(String num) {
        try {
            return Double.parseDouble(num);
        }
        catch (NumberFormatException exception) {
            return null;
        }
    }

    public static LocalDate date_parser(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(date, formatter);
        }
        catch(DateTimeParseException exception) {
            return null;
        }
    }

    public static LocalTime time_parser(String time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(time, formatter);
        }
        catch(DateTimeParseException exception) {
            return null;
        }
    }

    public static boolean name_checker(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }
}
