package HR.BusinessLayer;

import HR.DataAccessLayer.ShiftDAO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Schedule {
    private final String store;
    private final Map<ShiftPair, Shift> shifts;
    private final ShiftDAO shiftDAO;


    public Schedule(String store, LocalDate first_day, LocalTime morning_start, LocalTime morning_end, LocalTime evening_start, LocalTime evening_end, ShiftDAO shiftDAO) {
        this.store = store;
        shifts = new HashMap<>();
        this.shiftDAO = shiftDAO;
        initialize_week_shifts(first_day, morning_start, morning_end, evening_start, evening_end);
    }

    public Schedule(String store, Map<ShiftPair, Shift> shifts, ShiftDAO shiftDAO) {
        this.store = store;
        this.shifts = shifts;
        this.shiftDAO = shiftDAO;
    }
    private void initialize_week_shifts(LocalDate week_start_date, LocalTime morning_start_time, LocalTime morning_end_time, LocalTime evening_start_time, LocalTime evening_end_time) {
        for (int i = 0; i < 7; i++) {
            shifts.put(new ShiftPair(week_start_date, ShiftType.MORNING), new Shift(store, morning_start_time, morning_end_time));
            shifts.put(new ShiftPair(week_start_date, ShiftType.EVENING), new Shift(store,  evening_start_time, evening_end_time));
            shiftDAO.create_shift(week_start_date, ShiftType.MORNING, morning_start_time, morning_end_time, store);
            shiftDAO.create_shift(week_start_date, ShiftType.EVENING, evening_start_time, evening_end_time, store);
            week_start_date = week_start_date.plusDays(1);
        }
        for (ShiftPair pair : shifts.keySet()) {
            scheduled_confirmation_check(shifts.get(pair).get_start(), pair,  shifts.get(pair)); // subtract 24 hours from the date
        }
    }

    private void scheduled_confirmation_check(LocalTime checkTime, ShiftPair pair, Shift shift) {
        LocalDateTime dateTime1 = LocalDateTime.of(pair.getDate().minusDays(1), checkTime); // combine first LocalDate and LocalTime into a LocalDateTime
        LocalDateTime dateTime2 = LocalDateTime.now(); // combine second LocalDate and LocalTime into a LocalDateTime

        Duration duration = Duration.between(dateTime2, dateTime1); // calculate duration between the two LocalDateTime objects
        long delay = duration.getSeconds();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            try {
                send_mail(pair, shift);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }, delay, TimeUnit.SECONDS);
    }

    private void send_mail(ShiftPair pair, Shift shift) throws MessagingException {
        if (!shift.is_confirmed()) {
            // Set the email properties
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            // Set the email account credentials
            String senderEmail = "206494015.322527375.hrmanager@gmail.com";
            //String password = "bengurion111";

            // Create a mail session with the email account credentials
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, "mwjrfkjonbnyfzmo");
                }
            });

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = pair.getDate().format(formatter);

            // Create a new email message
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(senderEmail));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse("206494015.322527375.hrmanager@gmail.com"));
            emailMessage.setSubject("Shift not confirmed alert");
            emailMessage.setText("ALERT: " + formattedDate + ", " + pair.getType().toString() + " shift in " + shift.get_store() + " is 24 hours or less from now and still isn't confirmed!");
            // Send the email message
            Transport.send(emailMessage);
        }
    }

    public String add_availability(Integer employee_id, LocalDate shift_date, ShiftType shift_type) {
        ShiftPair shiftPair = get_shift(shift_date, shift_type);
        if (shiftPair != null) {
            String res = shiftDAO.add_availability(employee_id, shift_date, shift_type, store);
            if (res.equals("")) {
                return shifts.get(shiftPair).add_availability(employee_id);
            }
            return res;
        }
        return "Shift doesn't exists";
    }

    private ShiftPair get_shift(LocalDate shift_date, ShiftType shift_type) {
        for (ShiftPair shift_pair: shifts.keySet()) {
            if (shift_pair.equals(shift_date, shift_type)) {
                return shift_pair;
            }
        }
        return null;
    }

    public String remove_availability(Integer employee_id, LocalDate shift_date, ShiftType shift_type) {
        ShiftPair shiftPair = get_shift(shift_date, shift_type);
        if (shiftPair != null) {
            String res = shiftDAO.remove_availability(employee_id, shift_date, shift_type, store);
            if (res.equals("")) {
                return shifts.get(shiftPair).remove_availability(employee_id);
            }
            return res;
        }
        return "Shift doesn't exists";
    }

    public String get_availability(Integer employee_id) {
        StringBuilder availability = new StringBuilder();
        for (ShiftPair pair: shifts.keySet()) {
            if (shifts.get(pair).is_available(employee_id)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDate = pair.getDate().format(formatter);
                availability.append(formattedDate).append(" - ").append(pair.getType().toString()).append("\n");
            }
        }
        return availability.toString();
    }

    public String get_shifts(Integer employee_id) {
        StringBuilder shifts_list = new StringBuilder();
        for (ShiftPair pair: shifts.keySet()) {
            String job = shifts.get(pair).is_assigned(employee_id);
            if (!job.equals("")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDate = pair.getDate().format(formatter);
                shifts_list.append(formattedDate).append(" - ").append(pair.getType().toString()).append(", as ").append(job).append("\n");
            }
        }
        return shifts_list.toString();
    }

    public String confirm_shift(LocalDate shift_date, ShiftType shift) {
        String res = shiftDAO.confirm_shift(shift_date, shift, store);
        if (res.equals("")) {
            return shifts.get(get_shift(shift_date, shift)).confirm_shift();
        }
        return res;
    }

    public String assign_to_shift(int employee_id, LocalDate shift_date, ShiftType shift_type, JobType role) {
        String res = shifts.get(get_shift(shift_date, shift_type)).assign_to_shift(employee_id, role);
        if (res.equals("")) {
            return shiftDAO.assign_to_shift(employee_id, shift_date, shift_type, role, store);
        }
        return res;
    }

    public String remove_from_shift(int employee_id, LocalDate shift_date, ShiftType shift_type, JobType job) {
        String res = shiftDAO.remove_from_shift(employee_id, shift_date, shift_type, job, store);
        if (res.equals("")) {
            return shifts.get(get_shift(shift_date, shift_type)).remove_from_shift(employee_id, job);
        }
        return res;
    }

    public String limit_employee(int employee_id, LocalDate shift_date, ShiftType shift_type) {
        String res = shiftDAO.limit_employee(employee_id, shift_date, shift_type, store);
        if (res.equals("")) {
            return shifts.get(get_shift(shift_date, shift_type)).limit_employee(employee_id);
        }
        return res;
    }

    public String remove_employee_limit(int employee_id, LocalDate shift_date, ShiftType shift_type) {
        String res = shiftDAO.remove_employee_limit(employee_id, shift_date, shift_type, store);
        if (res.equals("")) {
            return shifts.get(get_shift(shift_date, shift_type)).remove_employee_limit(employee_id);
        }
        return res;
    }

    public List<Integer> show_shift_availability(LocalDate shift_date, ShiftType shift_type) {
        return shifts.get(get_shift(shift_date, shift_type)).show_shift_availability();
    }

    public int shifts_limit(int employee_id, LocalDate shift_date) {
        int counter = 0;
        for (ShiftPair pair: shifts.keySet()) {
            if (!shifts.get(pair).is_assigned(employee_id).equals("")) {
                if (pair.getDate() == shift_date) {
                    return -1;
                }
                counter = counter + 1;
            }
        }
        return counter;
    }

    public boolean has_future_shifts(LocalDate date, int employee_id) {
        for (ShiftPair pair: shifts.keySet()) {
            if (pair.getDate().isAfter(date) || pair.getDate().equals(date)) {
                if (!shifts.get(pair).is_assigned(employee_id).equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    public double get_hours(LocalDate shift_date, ShiftType shift_type) {
        return shifts.get(get_shift(shift_date, shift_type)).get_length();
    }

    public boolean check_availability(Timestamp arrivalTime) {
        LocalDateTime datetime = arrivalTime.toLocalDateTime();
        LocalDate date = datetime.toLocalDate();
        for (ShiftPair pair: shifts.keySet()) {
            if (pair.getDate().equals(date) && (shifts.get(pair).get_start().isBefore(datetime.toLocalTime()) && shifts.get(pair).get_end().isAfter(datetime.toLocalTime()))) {
                return shifts.get(pair).check_availability();
            }
        }
        return false;
    }

    public List<String> get_available_drivers(Timestamp startTime, Timestamp endTime) {
        LocalDateTime datetime1 = startTime.toLocalDateTime();
        LocalDate start = datetime1.toLocalDate();
        LocalDateTime datetime2 = endTime.toLocalDateTime();
        LocalDate end = datetime2.toLocalDate();
        for (ShiftPair pair: shifts.keySet()) {
            if (pair.getDate().equals(start) && pair.getDate().equals(end) && (shifts.get(pair).get_start().isBefore(datetime1.toLocalTime()) && shifts.get(pair).get_end().isAfter(datetime1.toLocalTime())) && (shifts.get(pair).get_start().isBefore(datetime2.toLocalTime()) && shifts.get(pair).get_end().isAfter(datetime2.toLocalTime()))) {
                return shifts.get(pair).get_available_drivers();
            }
        }
        return new ArrayList<>();
    }

    public boolean assign_drivers(int employee_id, Timestamp startTime, Timestamp endTime) {
        LocalDateTime datetime1 = startTime.toLocalDateTime();
        LocalDate start = datetime1.toLocalDate();
        LocalDateTime datetime2 = endTime.toLocalDateTime();
        LocalDate end = datetime2.toLocalDate();
        for (ShiftPair pair: shifts.keySet()) {
            if (pair.getDate().equals(start) && pair.getDate().equals(end) && (shifts.get(pair).get_start().isBefore(datetime1.toLocalTime()) && shifts.get(pair).get_end().isAfter(datetime1.toLocalTime())) && (shifts.get(pair).get_start().isBefore(datetime2.toLocalTime()) && shifts.get(pair).get_end().isAfter(datetime2.toLocalTime()))) {
                String res = shifts.get(pair).assign_to_shift(employee_id, JobType.DRIVER);
                return res.equals("");
            }
        }
        return false;
    }

    public boolean has_future_shifts_role(LocalDate date, JobType role, int employee_id) {
        for (ShiftPair pair: shifts.keySet()) {
            if (pair.getDate().isAfter(date) || pair.getDate().equals(date)) {
                if (shifts.get(pair).is_assigned_to_role(employee_id, role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean current_or_future_week(int day) {
        Map.Entry<ShiftPair, Shift> firstEntry = shifts.entrySet().iterator().next();
        ShiftPair key = firstEntry.getKey();
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1); // week starts on Sunday
        int week_day = key.getDate().get(weekFields.weekOfWeekBasedYear());
        return week_day >= day;
    }

    public String cancel_product(int employee_id, int product_id, LocalDate shift_date, ShiftType shift_type) {
        String res = shiftDAO.cancel_product(employee_id, product_id, shift_date, shift_type, store);
        if (res.equals("")) {
            return shifts.get(get_shift(shift_date, shift_type)).cancel_product(employee_id, product_id);
        }
        return res;
    }

    public Map<JobType, List<Integer>> show_shift_assigned(LocalDate shift_date, ShiftType shift_type) {
        return shifts.get(get_shift(shift_date, shift_type)).show_shift_assigned();
    }

    public boolean is_limited(int employee_id, LocalDate shift_date, ShiftType shift_type) {
        return shifts.get(get_shift(shift_date, shift_type)).is_limited(employee_id);
    }

    public int get_week() {
        Map.Entry<ShiftPair, Shift> firstEntry = shifts.entrySet().iterator().next();
        ShiftPair key = firstEntry.getKey();
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1); // week starts on Sunday
        return key.getDate().get(weekFields.weekOfWeekBasedYear());
    }

    public String show_scheduled_deliveries(LocalDate shift_date, ShiftType shift_type) {
        return shifts.get(get_shift(shift_date, shift_type)).show_scheduled_deliveries(shift_date);
    }

    public List<ShiftPair> get_shifts_pairs() {
        return new LinkedList<>(shifts.keySet());
    }

    public List<Integer> get_availables(ShiftPair shift) {
        return shifts.get(shift).get_availables();
    }

    public List<ShiftPair> getAssignedShiftsDates(Integer employee_id) {
        List<ShiftPair> output = new LinkedList<>();
        for (ShiftPair pair: shifts.keySet()) {
            String job = shifts.get(pair).is_assigned(employee_id);
            if (!job.equals("")) {
                output.add(pair);
            }
        }
        return output;
    }
}
