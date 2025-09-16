# Furry Friends — Detailed Implementation Report

Date: 2025-09-16

## Overview
This report documents all substantial changes and deliverables completed for the Furry Friends JavaFX application, including UI/UX updates, feature implementations, fixes, and documentation created to support a class presentation and demo.

## Tech Stack
- Java 17
- JavaFX 21.0.2
- SQLite (xerial sqlite-jdbc)
- Maven build
- BCrypt for password hashing

## Architecture
- Entry: `src/main/java/FurryFriendsApp.java`
- UI: `src/main/resources/gui/Scene.fxml`
- UI Controller: `src/main/java/gui/GUI_SceneController.java`
- Backend Controller (DB/auth): `src/main/java/controller/Controller.java`
- Models: `src/main/java/models/*.java`

## Database Schema (SQLite)
- `users` (roles: Owner, Vet Hospital, Shelter)
- `facilities`, `facility_slots`, `appointments`
- `pets`, `pet_medications`, `journal_entries`
- `rooms`, `room_availability`, `room_bookings`

## Major Features Implemented/Enhanced
1. Patient Medical History
   - UI in lookup panel with Pet ID input, Copy ID, and results area.
   - Controller loads pet basics, medications, and recent journal entries.

2. Emergency Dispatch
   - Simulates intake → route to nearest vet → assign open/emergency slot → create appointment labeled `EMERGENCY DISPATCH`.
   - Displays summary in UI and uses toast notifications.

3. Owner Convenience
   - "My Pets" dialog listing pets with IDs, plus a Copy Pet ID flow.
   - Appointment booking flow across open Vet slots with visit reason.
   - My Appointments view.

4. Facility (Vet/Shelter) Admin
   - Facility profile fields: name, phone, address, email, hours, website.
   - Add custom slot, add sample slots, and list facility slots.

5. Shelter Room Bookings
   - Seed rooms/dates for demo.
   - Vet: Book Shelter Room → reserves first available date/room; confirmation + toast.
   - Shelter: Clicking the same button shows booking list (who booked, which room, when), including pet names and booker role.
   - Facility appointments view groups content into "Appointments" and "Shelter Room Bookings" when viewing from a Shelter.

6. Notifications & Reminders
   - Toast overlay for successes/errors/info.
   - Scheduled reminders 15 minutes before upcoming appointments on login and after booking.

7. Role-based UI Gating
   - Owner vs Vet Hospital vs Shelter menus and panels are selectively visible.

## UX/Copy Changes
- Shelter flow clarified: button text becomes "View Room Bookings" for Shelter users.
- Informational messages improved for missing data (e.g., suggest using "Seed Rooms/Dates").

## Fixes & Stability
- Removed duplicate/legacy classes and ensured correct FXML handler signatures.
- Addressed compile errors (unused imports, variables) and ensured clean builds.
- Recognized SLF4J NOP warnings as harmless.

## Documentation Deliverables
- `docs/System-Overview.md`: Architecture, data model, and feature mapping.
- `docs/Demo-Script.md`: Step-by-step demo plan with expected outcomes.
- `docs/Presentation-Deck.md`: Detailed slide deck with speaker notes, visuals checklist, and troubleshooting.
- `docs/Detailed-Report.md`: This report.

## How to Run
```powershell
mvn -q -DskipTests=true clean compile
mvn -DskipTests=true javafx:run
```
If window fails to render, try software rendering:
```powershell
mvn -DskipTests=true -Dprism.order=sw javafx:run
```

## Validation Checklist
- Build: PASS
- Launch: PASS (JavaFX window shows; SLF4J NOP warning acceptable)
- Owner flows: My Pets / Book Appointment / My Appointments → PASS
- Vet flows: Save Facility / Add/List Slots / View Appointments → PASS
- Shelter flows: Seed Rooms/Dates / View Room Bookings → shows who, room, date with pet name → PASS
- Patient Medical History: Copy ID / Show History → PASS
- Emergency Dispatch: creates EMERGENCY appointment and displays summary → PASS
- Notifications: Toasts on login, booking, and reminders scheduled → PASS

## Requirements Mapping
- "Show patient medical history" → Implemented (Lookup panel)
- "Be clear show emergency dispatch" → Implemented with dispatch flow and labels
- "Remove demo clutter" → Main demo-only UI trimmed; focused sections retained
- "Where to find Pet ID" → My Pets dialog + Copy actions
- "Show shelter room bookings in appointment book" → Facility view extended with grouped sections
- "Group lines by date with headings" → Appointments grouped; Shelter bookings heading
- "Show who booked shelter room and when" → Shelter bookings list includes name/role/date/room (and pet name)
- "Presentation-ready materials" → System Overview, Demo Script, Detailed Deck, this report

## Known Limitations / Next Steps
- Filters and search across lists are minimal (can add date filters and sorting in dialogs).
- Vaccination reminders are basic (expand to periodic schedules and UI management).
- Packaging not included (consider `jlink`/`jpackage`).

## File Changes Summary (Key)
- UI: `Scene.fxml` — added Shelter button `fx:id`, cleaned visuals
- Controller: `GUI_SceneController.java` — added/updated handlers for My Pets, Copy Pet ID, View Facility Appointments (grouped), Emergency Dispatch, Shelter bookings listing with names/roles, dynamic button label
- Service: `NotificationService.java` — reminders and toast routing
- Backend: `Controller.java` — auth and DB plumbing; table ensures
- Entry: `FurryFriendsApp.java` — app bootstrap and DB init

## Closing
The app is demo-ready with clear role-based workflows, traceable shelter bookings, and comprehensive documentation for presentation. This work aligns with project goals and class requirements while leaving a clear path for iterative enhancements.
