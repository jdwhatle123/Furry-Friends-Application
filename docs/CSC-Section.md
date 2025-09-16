# 3.1.1 CSC Detailed Object Model

This section presents the detailed object model for the Furry Friends application. The UML class diagram (Figure 5) shows object identities, attributes, operations, and relationships among core domain classes (User, Pet, Facility, Appointment, Shelter Bookings), supporting tables (Medication, JournalEntry), service classes (NotificationService), and the application controller (Controller).

See also: UML overview (https://www.uml-diagrams.org/), UML 2 Class Diagram (IBM Developer), UML specification (http://uml.org).

## Figure 5 – Class Diagram

The Mermaid source for Figure 5 is provided in `docs/Diagrams/Figure5.mmd`. Export to PNG as `docs/Diagrams/Figure5.png` for inclusion in this document.

## 3.1.1.1 CSCC #1 – User (Identity and Access)
- Purpose: Represents system actors (Owner, Vet Hospital admin, Shelter admin). Used for authentication/authorization and as a linkage owner for pets and bookings.
- Key Attributes: `loginId`, `role`, `firstName`, `lastName`, `email`, `phoneNumber`, `street`, `city`, `state`, `zip`.
- Operations: Auth flows (`login`, `forgotPassword`, `register`) via `Controller`.
- Interfaces: Database `users`; relationships to `Pet` (owns), `Facility` (adminAgent), `RoomBooking` (bookedBy); GUI login/registration.

## 3.1.1.2 CSCC #2 – Pet (Owner’s Animal)
- Purpose: Core domain entity for medical history, medications, appointments, and shelter bookings.
- Key Attributes: `id`, `name`, `breed`, `age`, `diet`, `medicalHistory`, `ownerUsername`.
- Operations: Created/queried via `Controller` (e.g., appointment booking, emergency intake).
- Interfaces: DB `pets`, `pet_medications`, `journal_entries`; relationships to `User`, `Medication`, `JournalEntry`.

## 3.1.1.3 CSCC #3 – Facility (Vet Hospital or Shelter)
- Purpose: Represents a physical/service location—Vet or Shelter—with administration by a `User`.
- Key Attributes: `id`, `name`, `type`, contact and address info, `adminAgent`, `businessHours`, `websiteUrl`.
- Operations: Admin edits (GUI), slot/room seeding.
- Interfaces: DB `facilities`; relationships: `FacilitySlot`, `Room`.

## 3.1.1.4 CSCC #4 – FacilitySlot (Schedulable Timeslot)
- Purpose: Appointment slot at a facility; can be booked by an owner.
- Key Attributes: `id`, `facilityId`, `slot` (LocalDateTime), `booked`.
- Operations: Mark booked/unbooked during book/cancel.
- Interfaces: DB `facility_slots`; relationship to `Appointment`.

## 3.1.1.5 CSCC #5 – Appointment (Owner Booking at Vet)
- Purpose: Connects a `Pet` to a `Facility` at a specific `FacilitySlot` with an owner and a description.
- Key Attributes: `id`, `petId`, `facilityId`, `slotId`, `ownerUsername`, `description`.
- Operations: Create via `addAppointment`; Cancel (Vet) frees slot and deletes record.
- Interfaces: DB `appointments` (joins to `facility_slots`, `facilities`). GUI Owner book → Vet view/cancel.

## 3.1.1.6 CSCC #6 – Room (Shelter Room)
- Purpose: Room in a Shelter for bookings.
- Key Attributes: `id`, `facilityId`, `name`, `size`.
- Interfaces: DB `rooms`; links to `room_availability`, `room_bookings`.

## 3.1.1.7 CSCC #7 – RoomAvailability (Shelter Room Open Dates)
- Purpose: Availability of a `Room` on a given `date`.
- Key Attributes: `id`, `roomId`, `date`, `available`.
- Operations: Updated on booking/cancel.
- Interfaces: DB `room_availability`.

## 3.1.1.8 CSCC #8 – RoomBooking (Shelter Booking)
- Purpose: Occupancy for a `Room` on a `date`, by a `User` and for a `Pet`.
- Key Attributes: `id`, `roomId`, `date`, `bookedBy`, `petId`.
- Operations: Create on booking; Shelter cancel restores availability then deletes booking.
- Interfaces: DB `room_bookings` (joins `rooms`, `pets`, `users`). GUI Shelter view/cancel.

## 3.1.1.9 CSCC #9 – JournalEntry (Pet History)
- Purpose: Pet medical/notes history.
- Key Attributes: `id`, `petId`, `date`, `title`, `note`, `ownerUsername`, `visibility`, `createdAt`.
- Interfaces: DB `journal_entries`. GUI Patient Medical History.

## 3.1.1.10 CSCC #10 – Medication (Pet Medication)
- Purpose: Records prescribed medications.
- Key Attributes: `id`, `petId`, `name`, `dosage`, `frequency`.
- Interfaces: DB `pet_medications`.

## 3.1.1.11 CSCC #11 – NotificationService (Service)
- Purpose: Deliver reminders/toasts to the current `User`.
- Operations: `scheduleReminder`, `addListener`, `cancelAllForUser`, `close`.
- Interfaces: UI toast overlay; time-based scheduling.

## 3.1.1.12 CSCC #12 – Controller (Application Controller)
- Purpose: Integration point for DB and business operations used by the GUI.
- Operations: `getConnection`, `login`, `register`, `forgotPassword`, `getUserByLoginId`, `addAppointment`, CRUD/seeding helpers.
- Interfaces: SQLite (JDBC); invoked by GUI handlers for all flows.
