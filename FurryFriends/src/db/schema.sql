/*
FurryFriends Database Schema
---------------------------
This SQL schema defines the relational database for the FurryFriends application, which manages users (pet owners, vet hospitals, shelters), pets, facilities, appointments, shelter rooms, and related data. The schema is designed to support:
- User profiles for owners, vet hospitals, and shelters
- Facility management (shelters and vet hospitals)
- Pet records, medications, and health journals
- Appointment scheduling between pets and facilities
- Shelter room management and bookings

Key Entities & Relationships:
- users: All user profiles (owners, shelters, vets)
- facilities: Vet hospitals and shelters, with details and services
- pets: Pets owned by users
- appointments: Links pets, facilities, and appointment slots
- rooms/room_bookings: Shelter room scheduling for pets
- Additional tables for services, social media, medications, journals, and availability

Referential integrity is enforced via foreign keys. Roles and types are constrained for data consistency.
*/

-- Users table: stores all user profiles (owners, vets, shelters)
CREATE TABLE IF NOT EXISTS users (
    loginId TEXT PRIMARY KEY, -- unique username or login identifier
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    street TEXT NOT NULL,
    city TEXT NOT NULL,
    state TEXT NOT NULL,
    zip TEXT NOT NULL,
    phoneNumber TEXT NOT NULL,
    email TEXT NOT NULL,
    passwordHash TEXT NOT NULL, -- hashed password for authentication
    role TEXT NOT NULL CHECK(role IN ('Owner', 'Vet Hospital', 'Shelter')), -- user type
    adminAgent TEXT -- optional: admin agent for facilities
);

-- Facilities table: stores shelters and vet hospitals, with all model fields
CREATE TABLE IF NOT EXISTS facilities (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type TEXT NOT NULL CHECK(type IN ('Vet Hospital', 'Shelter')), -- facility type
    phoneNumber TEXT NOT NULL,
    street TEXT NOT NULL,
    city TEXT NOT NULL,
    state TEXT NOT NULL,
    zip TEXT NOT NULL,
    email TEXT, -- optional
    businessHours TEXT, -- optional: JSON or string
    licenseNumber TEXT, -- optional: for vet hospitals
    emergencyContact TEXT, -- optional
    websiteUrl TEXT, -- optional
    adminAgent TEXT -- optional: admin user
);

-- Facility services (many-to-one with facilities)
CREATE TABLE IF NOT EXISTS facility_services (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    facilityId INTEGER NOT NULL, -- references facilities(id)
    service TEXT NOT NULL, -- e.g., 'Vaccination', 'Boarding'
    FOREIGN KEY(facilityId) REFERENCES facilities(id)
);

-- Facility social media links (many-to-one with facilities)
CREATE TABLE IF NOT EXISTS facility_social_media (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    facilityId INTEGER NOT NULL, -- references facilities(id)
    url TEXT NOT NULL, -- social media URL
    FOREIGN KEY(facilityId) REFERENCES facilities(id)
);

-- Facility slots: available appointment slots for each facility
CREATE TABLE IF NOT EXISTS facility_slots (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    facilityId INTEGER NOT NULL, -- references facilities(id)
    slot TEXT NOT NULL, -- e.g., '2025-09-10 08:00' (ISO datetime)
    booked INTEGER DEFAULT 0, -- 0 = available, 1 = booked
    FOREIGN KEY(facilityId) REFERENCES facilities(id)
);

-- Pets table: stores pets, linked to the user (owner)
CREATE TABLE IF NOT EXISTS pets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    breed TEXT NOT NULL,
    age INTEGER NOT NULL,
    diet TEXT, -- optional
    medicalHistory TEXT, -- optional
    ownerUsername TEXT NOT NULL, -- references users(loginId)
    FOREIGN KEY(ownerUsername) REFERENCES users(loginId)
);

-- Pet medications (many-to-one with pets)
CREATE TABLE IF NOT EXISTS pet_medications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    petId INTEGER NOT NULL, -- references pets(id)
    name TEXT NOT NULL, -- medication name
    dosage TEXT, -- optional
    frequency TEXT, -- optional
    FOREIGN KEY(petId) REFERENCES pets(id)
);

-- Appointments table: links a pet, a facility, and a slot
CREATE TABLE IF NOT EXISTS appointments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    petId INTEGER NOT NULL, -- references pets(id)
    slotId INTEGER NOT NULL, -- references facility_slots(id)
    description TEXT, -- optional: reason for appointment
    ownerUsername TEXT NOT NULL, -- references users(loginId)
    facilityId INTEGER NOT NULL, -- references facilities(id)
    FOREIGN KEY(petId) REFERENCES pets(id),
    FOREIGN KEY(ownerUsername) REFERENCES users(loginId),
    FOREIGN KEY(facilityId) REFERENCES facilities(id),
    FOREIGN KEY(slotId) REFERENCES facility_slots(id)
);

-- Journal entries: health journal entries for pets
CREATE TABLE IF NOT EXISTS journal_entries (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    petId INTEGER NOT NULL, -- references pets(id)
    date TEXT NOT NULL, -- ISO date
    note TEXT NOT NULL, -- journal content
    ownerUsername TEXT NOT NULL, -- references users(loginId)
    title TEXT, -- optional
    visibility TEXT, -- optional: e.g., 'private', 'shared'
    createdAt TEXT, -- optional: timestamp
    FOREIGN KEY(petId) REFERENCES pets(id),
    FOREIGN KEY(ownerUsername) REFERENCES users(loginId)
);

-- Shelter rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    facilityId INTEGER NOT NULL, -- references facilities(id)
    name TEXT NOT NULL, -- room name or number
    size TEXT NOT NULL, -- e.g., 'Small', 'Medium', 'Large'
    FOREIGN KEY(facilityId) REFERENCES facilities(id)
);

-- Room availability table
CREATE TABLE IF NOT EXISTS room_availability (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    roomId INTEGER NOT NULL, -- references rooms(id)
    date TEXT NOT NULL, -- ISO date
    available INTEGER DEFAULT 1, -- 1 = available, 0 = not available
    FOREIGN KEY(roomId) REFERENCES rooms(id)
);

-- Room bookings table for shelter hotel room scheduling
CREATE TABLE IF NOT EXISTS room_bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    roomId INTEGER NOT NULL, -- references rooms(id)
    date TEXT NOT NULL, -- ISO date
    bookedBy TEXT NOT NULL, -- user loginId
    petId INTEGER NOT NULL, -- references pets(id)
    FOREIGN KEY(roomId) REFERENCES rooms(id),
    FOREIGN KEY(bookedBy) REFERENCES users(loginId),
    FOREIGN KEY(petId) REFERENCES pets(id)
);