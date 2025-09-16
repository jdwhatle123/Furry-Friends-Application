// Calendar.java: Manages appointments for pets at facilities (Vet/Shelter).
// Provides methods to add, cancel, and retrieve appointments by various criteria.
package models;

import java.util.*;

public class Calendar {
    // Represents a single appointment
    public static class Appointment {
        public int id; // Unique appointment ID
        public int petId; // ID of the pet for the appointment
        public int facilityId; // ID of the facility (Vet/Shelter)
        public String facilityType; // Type of facility: "Vet" or "Shelter"
        public String slot; // Date and time slot for the appointment
        public String description; // Description of the appointment
        public String ownerUsername; // Username of the pet's owner

        public Appointment(int id, int petId, int facilityId, String facilityType, String slot, String description, String ownerUsername) {
            this.id = id;
            this.petId = petId;
            this.facilityId = facilityId;
            this.facilityType = facilityType;
            this.slot = slot;
            this.description = description;
            this.ownerUsername = ownerUsername;
        }
    }

    private Map<Integer, Appointment> appointments = new HashMap<>(); // Stores all appointments by ID
    private int nextId = 1; // Used to assign unique IDs to appointments

    // Adds a new appointment and returns it
    public synchronized Appointment addAppointment(int petId, int facilityId, String facilityType, String slot, String description, String ownerUsername) {
        Appointment appt = new Appointment(nextId++, petId, facilityId, facilityType, slot, description, ownerUsername);
        appointments.put(appt.id, appt);
        return appt;
    }

    // Cancels an appointment by ID; returns true if successful
    public synchronized boolean cancelAppointment(int appointmentId) {
        return appointments.remove(appointmentId) != null;
    }

    // Returns all appointments for a specific facility
    public synchronized List<Appointment> getAppointmentsForFacility(int facilityId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.facilityId == facilityId) result.add(a);
        }
        return result;
    }

    // Returns all appointments for a specific user
    public synchronized List<Appointment> getAppointmentsForUser(String ownerUsername) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.ownerUsername.equals(ownerUsername)) result.add(a);
        }
        return result;
    }

    // Returns all appointments for a specific pet
    public synchronized List<Appointment> getAppointmentsForPet(int petId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.petId == petId) result.add(a);
        }
        return result;
    }

    // Retrieves an appointment by its ID
    public synchronized Appointment getAppointmentById(int id) {
        return appointments.get(id);
    }

    // Returns a list of all appointments
    public synchronized List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }
}
