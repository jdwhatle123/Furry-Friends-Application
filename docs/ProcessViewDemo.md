# Process View Demonstration (Module 2)

This document maps implemented features to the Process View UML figures.

## Figure 1 — Activity Diagram: Shelter Intake & Vet Check
Workflow steps covered by demo runner:
1. Intake initiated; intake form captured (simulated).
2. Temporary profile assigned.
3. System schedules a veterinary check.
4. Decision: stable vs. unstable.
   - If unstable: emergency care logged; findings recorded.
   - If stable: standard vet check; findings recorded.
5. Health data saved; notifications issued; activity logged.

Demo implementation:
- Method: `ProcessViewDemoRunner.runShelterIntakeAndVetCheck()`
- Uses: `controller.Controller` for persistence; prints notifications to console.

## Figure 2 — Sequence Diagram: Vaccination Reminder Workflow
Workflow steps:
- Scheduler queries pet health/vaccination data; formats and sends reminders.

Demo implementation:
- Method: `ProcessViewDemoRunner.runVaccinationReminderWorkflow()`
- Service: `services.ReminderService` (identifies upcoming vaccinations and formats messages).

## Figure 3 — Collaboration Diagram: Appointment Scheduling
Workflow steps:
- Scheduling component talks to availability service, profile DB, staff assignment; returns appointment details.

Demo implementation:
- Method: `ProcessViewDemoRunner.runAppointmentScheduling()`
- Uses DB tables created by `controller.Controller.ensureTablesExist()`; simulates availability and staff assignment.

## Figure 4 — Sequence Diagram: Appointment Scheduling & Emergency Dispatch
Two workflows:
1) Routine scheduling via API gateway (simulated), authentication (omitted), scheduling, confirmation via notification.
2) Emergency dispatch path: authorization -> vet dispatch -> ETA notification.

Demo implementation:
- Method: `ProcessViewDemoRunner.runAppointmentScheduling()` (routine)
- Method: `ProcessViewDemoRunner.runEmergencyDispatch()` (emergency)
- Service: `services.EmergencyDispatchService`

## Registration Use Case — Vet & Shelter

Workflow steps:
- Vet Hospital and Shelter actors register via `Controller.register(...)`.
- System creates corresponding `facilities` records.
- Sample `facility_slots` are added for the Vet facility to support scheduling demos.

Demo implementation:
- Method: `ProcessViewDemoRunner.runVetAndShelterRegistrationUseCase()`

## How to Run the Demo

```powershell
mvn exec:java
```

Observe the console output; the registration section prints first, followed by Figures 1–4.
