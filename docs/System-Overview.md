# Furry Friends — System Overview

## Executive Summary
Furry Friends is a JavaFX desktop app supporting Owners, Vet Hospitals, and Shelters. It includes registration/login, facility admin, appointment scheduling, patient medical history, emergency dispatch, shelter room bookings, and notifications.

## How to Run
```powershell
mvn -q -DskipTests=true clean compile
mvn javafx:run
```
DB: `./furryfriends.db` in the project root.

## Architecture
- View (FXML): `src/main/resources/gui/Scene.fxml`
- UI Logic (Controller): `src/main/java/gui/GUI_SceneController.java`
- Backend Controller (DB operations): `src/main/java/controller/Controller.java`
- Entry Point: `src/main/java/FurryFriendsApp.java`

### Key responsibilities
- GUI Controller:
  - Authentication: `handleLogin`, `submitRegister`, `submitForgotPassword`, `handleLogout`
  - Role gating: `showUserMenuPane`
  - Facility admin & slots: `handleAdminSaveFacility`, `handleAddSampleSlots`, `handleListMySlots`
  - Owner: `handleBookAppointment`, `handleMyAppointments`, `handleMyPets`
  - Lookup/History: `handleLookupOwnerAndPets`, `handleShowPetHistory`
  - Emergency: `handleEmergencyDispatch`
  - Shelter rooms: `handleSeedShelterRooms`, `handleBookShelterRoom`
  - Notifications: `showNotification`; reminder scheduling via `NotificationService`

## Data Model (SQLite)
- `users` — roles, profile, hashed passwords (BCrypt)
- `facilities` — facility profile (Vet Hospital, Shelter), linked to admin (`adminAgent`)
- `facility_slots` — availability for Vet Hospitals (booked flag)
- `appointments` — pet visit bookings (links `petId`, `facilityId`, `slotId`)
- `pets` — pet profile (ownerUsername)
- `pet_medications` — medications for pets
- `journal_entries` — clinical notes per pet
- `rooms`, `room_availability`, `room_bookings` — shelter rooms and bookings

## Design-to-Implementation Mapping
- Activity: Shelter Intake & Vet Check
  - Intake/creation: `handleEmergencyDispatch` and booking flows create pets
  - Vet checks: `facility_slots` + `appointments`
  - Findings: `pets.medicalHistory`, `journal_entries`

- Collaboration: Appointment Scheduling
  - Owner books slot → `appointments` + `booked=1` on `facility_slots`
  - Vet views via `handleViewFacilityAppointments`

- Sequence: Routine vs Emergency
  - Routine: `handleBookAppointment`
  - Emergency: `handleEmergencyDispatch`

- Notifications
  - Alerts + toasts; reminders scheduled for upcoming appointments

## Notable UX conveniences
- Owner `My Pets` with Copy Pet ID
- Patient History `Copy ID`
- Facility view grouped by sections (“Appointments”, “Shelter Room Bookings”)

## Quality & Validation
- Build: `mvn -q -DskipTests=true clean compile` (PASS)
- Runtime validated via Demo Script steps
- SQLite persistence; controller methods use try-with-resources
- Passwords stored hashed (BCrypt)

## Future Enhancements (Optional)
- Automated vaccination reminders via a periodic scheduler
- Richer filtering/grouping in dialogs
- Native packaging for easier distribution
