package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * ReminderService identifies upcoming vaccination deadlines and formats reminders.
 * Simplified for demo: uses an in-memory list of due dates.
 */
public class ReminderService {
    public static class VaccinationDue {
        public final int petId; public final String petName; public final LocalDate dueDate; public final String vaccine;
        public VaccinationDue(int petId, String petName, LocalDate dueDate, String vaccine) {
            this.petId = petId; this.petName = petName; this.dueDate = dueDate; this.vaccine = vaccine;
        }
    }

    public List<VaccinationDue> findUpcomingDue(LocalDate withinDays) {
        // Demo data
        List<VaccinationDue> list = new ArrayList<>();
        list.add(new VaccinationDue(1, "Buddy", LocalDate.now().plusDays(3), "Rabies"));
        list.add(new VaccinationDue(2, "Mittens", LocalDate.now().plusDays(7), "FVRCP"));
        return list;
    }

    public String formatReminder(VaccinationDue due) {
        return String.format("Reminder: %s (Pet #%d) needs %s vaccination by %s.", due.petName, due.petId, due.vaccine, due.dueDate);
    }
}
