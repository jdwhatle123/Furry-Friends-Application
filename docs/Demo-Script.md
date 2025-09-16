# Furry Friends — Demo Script

This script walks you through running and demonstrating the app in 8–12 minutes.

## 0) Run the app
- Build
```powershell
mvn -q -DskipTests=true clean compile
```
- Run
```powershell
mvn javafx:run
```
Notes: Close the window to exit; avoid Ctrl+C. DB file is `./furryfriends.db`.

## 1) Register users (auth + role model)
- Click `Register` → Fill Step 1 → click `Next` → Fill Step 2 → submit
- Create 3 accounts:
  - Vet Hospital admin: `vet1`
  - Shelter admin: `shelter1`
  - Owner: `owner1`
- Expect success toasts. Vet/Shelter get facility records.

## 2) Vet: Facility admin + slots
- Login as `vet1`
- Facility Admin → edit a few fields → `Save Facility` (toast)
- `Add Sample Slots` → `List My Slots` to verify availability

## 3) Owner: My Pets, book, view
- Logout; login as `owner1`
- `My Pets` → see entries like `[#12] Buddy — Labrador, age 4`
  - Click `Copy Pet ID` → paste somewhere to confirm
- `Book Appointment` → choose a vet slot → enter reason → confirm (alert + toast)
- `My Appointments` → `[#id] pet=<petId>, Vet Hospital @ <slot> — <reason>`

## 4) Vet: Facility appointments
- Logout; login as `vet1`
- `View Appointments` → check the “Appointments” section

## 5) Clinical context
- Login as `vet1` (or `shelter1`)
- `Owner & Pets Lookup` → enter `owner1` → `Lookup`
  - See owner profile and pets like `- Buddy (#12)`
- Patient Medical History → enter a Pet ID → `Show History`
  - Optionally use `Copy ID` to copy the Pet ID field

## 6) Emergency dispatch
- As `vet1`, expand `Emergency Dispatch`
- Optionally enter a pet name → `Dispatch Now`
- Confirm dialog includes `(pet #<id>)`
- `View Appointments` shows an `EMERGENCY DISPATCH` appointment

## 7) Shelter room workflow
- Login as `vet1` or `shelter1`
- `Seed Rooms/Dates` to ensure shelter data exists
- As `vet1` → `Book Shelter Room` (books earliest room/date)
- As `shelter1` → `View Appointments`
  - See “Shelter Room Bookings” section like `[ROOM #7] 2025-09-21 — room=Adoption Room A, pet=34, bookedBy=vet1`

## 8) Forgot password
- At login, click `Forgot Password or UserID`
- Provide username + email + new password → `Reset Password` → login with new password

## Notifications
- Alerts confirm actions; toasts show quick feedback
- Owner reminders are scheduled near appointment time and appear as toasts

## Screenshot checklist (for slides)
- Login
- Register Step 1 → Step 2
- Owner menu: `My Pets`/`My Appointments`
- Vet/Shelter menu: Facility Admin, Lookup, Emergency
- Facility Admin (Save)
- List My Slots
- Booking dialog + success alert
- My Appointments (shows Pet ID)
- Lookup results (shows `(#<id>)`)
- Patient Medical History
- Emergency Dispatch confirmation
- Shelter Room Bookings in `View Appointments`
