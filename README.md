# Furry-Friends-Application

Software Engineering CSC 605

— Furry Friends — Module 2 Demonstration —

This repo contains a JavaFX application (Pet Health Management System) and a CLI demo runner to showcase the Process View (runtime behavior) from Module 2.

## Recent UI/UX Updates (Sept 2025)

- Clearer button labels and tooltips:
  - Vet/Shelter: `View Open Slots` (was `List My Slots`) with tooltip clarifying it shows OPEN/BOOKED time slots.
  - Owner: `See Open Vet Slots & Book` (was `Book Appointment`).
  - Tooltips added to `View Appointments`, `Book Shelter Room`, `Seed Rooms/Dates`, and others for quick guidance.
- Scrollable dialogs for long lists:
  - Facility Appointments and Shelter Room Bookings now render in scrollable, read‑only views with word wrap.
- Shelter Cancel Booking (no schema changes):
  - Shelters can select a booking and cancel it; the corresponding room/date is marked available again.

Usage quickstart (where to click):
- Vet/Shelter users:
  - `View Open Slots` → inspect your facility’s time slots (open/booked).
  - `View Appointments` → see scheduled appointments (Shelters also see room bookings).
  - `Book Shelter Room` → Vet: book an available shelter date; Shelter: view current bookings.
  - `Seed Rooms/Dates` → create a demo shelter room with a couple of available dates.
- Owner users:
  - `See Open Vet Slots & Book` → browse open vet hospital slots and book.
  - `My Appointments` → view your scheduled appointments.

## How to Run

- Run JavaFX UI (requires Java 17+):

```powershell
mvn clean javafx:run
```

- Run CLI process-view demo (console workflows for Figures 1–4):

```powershell
mvn exec:java
```

### Run without Maven (portable bundle)

If you have a folder like `FurryFriends-Portable-NoMaven` that contains:

- `FurryFriends-1.0-SNAPSHOT.jar`
- `lib/` with `javafx.base.jar`, `javafx.controls.jar`, `javafx.fxml.jar`, `javafx.graphics.jar`, and `sqlite-jdbc-3.50.3.0.jar`
- Scripts: `Run-FurryFriends-NoMaven.ps1` and `Run-FurryFriends-NoMaven.cmd`

Run one of the scripts from that folder:

```powershell
./Run-FurryFriends-NoMaven.ps1
```

or double-click `Run-FurryFriends-NoMaven.cmd` in File Explorer.

Notes:
- Requires Java 17+ installed and available on PATH (or `JAVA_HOME` set).
- On first run, PowerShell may block scripts. Temporarily allow local scripts for this session:
  ```powershell
  Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
  ./Run-FurryFriends-NoMaven.ps1
  ```
- During launch you may see `SLF4J: Defaulting to no-operation (NOP) logger implementation` — this is harmless for this app.

## What’s Demonstrated

- Figure 1 – Activity: Shelter Intake & Vet Check
  - Deterministic workflow that shows intake, stability check, vet check, record persistence, and notifications.
- Figure 2 – Sequence: Vaccination Reminder Workflow
  - Scheduling component identifies upcoming deadlines; ReminderService formats messages; shows follow-up scheduling.
- Figure 3 – Collaboration: Appointment Scheduling
  - Demonstrates interactions among scheduling, availability, profile (medical history), staff assignment, and the requester.
- Figure 4 – Sequence: Appointment Scheduling & Emergency Dispatch
  - Two flows shown: routine scheduling and emergency dispatch with ETA via EmergencyDispatchService.

## Code Map

- UI Entry: `FurryFriendsApp` (JavaFX)
- Controller (DB + models): `controller.Controller`
- Models: `models.*`
- GUI Controller + FXML: `gui.GUI_SceneController`, `src/main/resources/gui/Scene.fxml`
- Services (demo logic): `services.ReminderService`, `services.EmergencyDispatchService`
- CLI Demo Runner: `demo.ProcessViewDemoRunner`

## Notes

- SQLite DB is used; the CLI demo uses an in-memory DB so it does not modify files.
- Image resources: `gui/images/Cover1.png` are included in the runtime classpath so the cover should appear in the UI.
- JSON: Gson dependency is available for future data interchange if needed.

## Architecture Docs

- Systems Overview (User, Logical, Process, Physical Views): `docs/SystemsOverview.md`
