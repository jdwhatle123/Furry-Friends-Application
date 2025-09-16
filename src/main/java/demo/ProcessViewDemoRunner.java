package demo;

import controller.Controller;
import services.ReminderService;
import services.EmergencyDispatchService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProcessViewDemoRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Furry Friends — Process View Demo ===");
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:")) {
            Controller c = new Controller(conn);
            c.ensureTablesExist();

            runVetAndShelterRegistrationUseCase(c);
            runShelterIntakeAndVetCheck(c, true);
            runVaccinationReminderWorkflow();
            runAppointmentScheduling(c);
            runEmergencyDispatch();
        }
    }

    // Registration Use Case: Vet Hospital and Shelter actors register, facilities created, and slots added
    public static void runVetAndShelterRegistrationUseCase(Controller c) throws Exception {
        System.out.println("\n[Registration Use Case] Vet Hospital & Shelter Registration");

        String vetRes = c.register(
                "Vet Hospital",
                "vet1",
                "Green Valley",
                "Veterinary",
                "Dr. Smith",
                "100 Clinic Rd",
                "Greenville",
                "SC",
                "29601",
                "864-555-1000",
                "vet1@greenvalleyvet.com",
                "pass1234");
        System.out.println("- Vet Hospital registration: " + vetRes);

        String shelterRes = c.register(
                "Shelter",
                "shelter1",
                "Second",
                "Chance",
                "Jane Admin",
                "200 Shelter Ave",
                "Greenville",
                "SC",
                "29601",
                "864-555-2000",
                "contact@secondchance.org",
                "pass1234");
        System.out.println("- Shelter registration: " + shelterRes);

        try (var stmt = c.getConnection().createStatement()) {
            var rs = stmt.executeQuery("SELECT id, name, type FROM facilities ORDER BY id");
            System.out.println("- Facilities created:");
            while (rs.next()) {
                System.out.println("  • [" + rs.getInt("id") + "] " + rs.getString("name") + " (" + rs.getString("type") + ")");
            }
            rs.close();
        }

        // Add a couple of open slots for the vet facility to support later scheduling demos
        Integer vetFacilityId = null;
        try (var ps = c.getConnection().prepareStatement("SELECT id FROM facilities WHERE type = 'Vet Hospital' LIMIT 1");
             var rs = ps.executeQuery()) {
            if (rs.next()) vetFacilityId = rs.getInt(1);
        }
        if (vetFacilityId != null) {
            try (var ps = c.getConnection().prepareStatement("INSERT INTO facility_slots (facilityId, slot, booked) VALUES (?, ?, 0)")) {
                ps.setInt(1, vetFacilityId); ps.setString(2, "2025-09-16T09:00"); ps.executeUpdate();
                ps.setInt(1, vetFacilityId); ps.setString(2, "2025-09-16T10:00"); ps.executeUpdate();
            }
            System.out.println("- Added sample open slots for Vet facility " + vetFacilityId);
        }
    }

    // Figure 1: Activity Diagram — Shelter Intake & Vet Check
    public static void runShelterIntakeAndVetCheck(Controller c, boolean stable) throws Exception {
        System.out.println("\n[Figure 1] Shelter Intake & Vet Check");
        System.out.println("- Intake started; temporary profile created.");
        System.out.println("- Scheduling initial vet check...");
        if (!stable) {
            System.out.println("- Decision: UNSTABLE -> Emergency triage and treatment.");
            System.out.println("- Findings recorded; notifications sent; activity logged.");
        } else {
            System.out.println("- Decision: STABLE -> Standard vet check.");
            System.out.println("- Findings recorded; notifications sent; activity logged.");
        }
    }

    // Figure 2: Sequence Diagram — Vaccination Reminder Workflow
    public static void runVaccinationReminderWorkflow() {
        System.out.println("\n[Figure 2] Vaccination Reminder Workflow");
        ReminderService svc = new ReminderService();
        for (ReminderService.VaccinationDue due : svc.findUpcomingDue(LocalDate.now().plusDays(14))) {
            String msg = svc.formatReminder(due);
            System.out.println("- " + msg + " (sent to dashboard)");
        }
    }

    // Figure 3: Collaboration Diagram — Appointment Scheduling
    public static void runAppointmentScheduling(Controller c) {
        System.out.println("\n[Figure 3] Appointment Scheduling (Collaboration)");
        System.out.println("- Checking availability service for open slots...");
        System.out.println("- Attaching medical history from profile DB...");
        System.out.println("- Confirming staff assignment...");
        System.out.println("- Appointment scheduled and returned to requester.");
    }

    // Figure 4: Sequence Diagram — Routine + Emergency
    public static void runEmergencyDispatch() {
        System.out.println("\n[Figure 4] Appointment Scheduling & Emergency Dispatch");
        System.out.println("- Routine path: request -> gateway -> scheduling -> confirmation -> notification.");
        EmergencyDispatchService ed = new EmergencyDispatchService();
        boolean ok = ed.authorizeEmergency("token-123", "Bleeding");
        if (ok) {
            LocalDateTime eta = ed.dispatchVet("Shelter Bay 1");
            System.out.println("- Emergency path: authorized -> dispatch -> ETA " + eta);
        } else {
            System.out.println("- Emergency path: authorization failed.");
        }
    }
}
