# Furry Friends — Presentation Deck (Detailed)

Note for presenter: Each slide includes brief speaker notes (in italics) to guide narration.

## 1) Title & Team
- Furry Friends: Role-based Vet & Shelter System
- Technologies: Java 17, JavaFX 21, SQLite, Gson
- Presenter: [Your Name]

_Intro: One desktop app tailored for Owners, Vet Hospitals, and Shelters to streamline care, scheduling, and coordination._

## 2) Problem & Solution
- Problem: Fragmented tools for pet care, appointments, and shelter room logistics; poor cross-role visibility.
- Solution: A unified JavaFX app with role-based features: appointments, patient history, emergency dispatch, and shelter bookings with auditability.

_Bridge pain points to features; emphasize role-based simplicity._

## 3) Architecture Overview
- View (FXML): `src/main/resources/gui/Scene.fxml`
- UI Controller: `src/main/java/gui/GUI_SceneController.java`
- Backend Controller (DB): `src/main/java/controller/Controller.java`
- App Entry: `src/main/java/FurryFriendsApp.java`

_MVC-ish layering: View handles layout; controller wires UI to DB; backend controller encapsulates queries and auth._

## 4) Data Model
- Core tables: `users`, `facilities`, `facility_slots`, `appointments`
- Pet data: `pets`, `pet_medications`, `journal_entries`
- Shelter: `rooms`, `room_availability`, `room_bookings`

_Relational joins drive combined views: e.g., appointments join facilities + slots; bookings join rooms + users._

## 5) Security & Quality
- BCrypt password hashing; inputs validated in controller layer
- Try-with-resources for DB safety; prepared statements throughout
- Local SQLite DB (`furryfriends.db`) for easy demo; no external services required

_Point out practical security measures within the class project scope._

## 6) Feature Tour (Owner)
- Register/Login (multi-step) → role-based menu
- My Pets: list with IDs + clipboard copy
- Book Appointment: choose Vet slot → reason → confirmation + reminder
- My Appointments: consolidated list

_Show how Owners quickly see pet IDs and book a vet visit._

## 7) Feature Tour (Vet Hospital)
- Facility Admin: profile, hours, contact, website
- Add/List Slots: manage availability
- View Appointments: grouped list with descriptions (e.g., vaccinations)
- Book Shelter Room: books first available date/room at a Shelter for coordination

_Highlight operational flow for clinics; clear view of upcoming schedule._

## 8) Feature Tour (Shelter)
- Seed Rooms/Dates: create demo rooms/availability
- Book Shelter Room (Shelter view): shows bookings list
	- Displays: date, room name, who booked (name/role) — satisfies “show who booked to what room and when”
- View Appointments: includes “Shelter Room Bookings” section

_Explain that shelters now get clarity on which vet booked which room/date (audit trail & coordination)._ 

## 9) Patient Medical History
- Lookup owner by username → list pets with IDs
- Input Pet ID → see basics, medications, and latest journal entries
- “Copy ID” convenience to speed workflow

_Demonstrate end-to-end visibility without leaving the app._

## 10) Emergency Dispatch
- Intake temporary pet → route to nearest Vet Hospital
- Auto-create earliest open or ad-hoc emergency slot
- Appointment created as “EMERGENCY DISPATCH”

_Emphasize speed: a complete path from incident to scheduled care._

## 11) Notifications & Reminders
- In-app toast notifications (login success, booking confirmations)
- Gentle reminders 15 minutes before booked appointments

_Non-intrusive feedback keeps users informed during the demo._

## 12) Live Demo Script (Condensed)
1. Login as Vet; Save Facility → Add Sample Slots → View Appointments
2. Login as Owner; My Pets → Book Appointment → My Appointments
3. Login as Vet; View Appointments (see owner booking)
4. Lookup Panel; Patient Medical History (Copy ID → Show History)
5. Emergency Dispatch; View Appointments (shows EMERGENCY)
6. Login as Shelter; Seed Rooms/Dates → Book Shelter Room (see bookings list)

_Time-box to ~5-7 minutes; prioritize smooth narrative over edge cases._

## 13) Screens & Visuals (Placeholders)
- Login screen
- Owner menu & My Pets dialog
- Vet: Facility Admin and List Slots
- Owner: Book Appointment dialog
- Lookup: Patient Medical History output
- Emergency Dispatch panel
- Shelter: Bookings dialog (who/room/when)

_Add screenshots here; ensure readable font size when presenting._

See full gallery in `docs/Screenshots.md` (with a copy script and inline previews). You can add or remove images there without changing the deck.

## 14) Results & Takeaways
- Multi-role workflow validated; data persists; reminders working
- Shelter view improved to show who booked which room/date

_Summarize what stakeholders gain: better coordination and traceability._

## 15) Roadmap
- Add vaccination schedule planner and recurring reminders
- Rich filters/sorting for lists (by date, facility, status)
- Packaging: jlink/jpackage for a one-click installer

_Close with bounded, realistic next steps._

## 16) Run & Troubleshooting
```powershell
mvn -q -DskipTests=true clean compile
mvn -DskipTests=true javafx:run
```
- If no JavaFX window: try software rendering
```powershell
mvn -DskipTests=true -Dprism.order=sw javafx:run
```
- SLF4J NOP warnings are expected and harmless for this demo.

---

Appendix: File Map
- FXML: `src/main/resources/gui/Scene.fxml`
- UI Controller: `src/main/java/gui/GUI_SceneController.java`
- Backend: `src/main/java/controller/Controller.java`
- Models: `src/main/java/models/*`
- Entry: `src/main/java/FurryFriendsApp.java`
