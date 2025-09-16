package models;

import java.util.*;

public class Calendar {
    public static class Appointment {
        public int id;
        public int petId;
        public int facilityId;
        public String facilityType;
        public String slot;
        public String description;
        public String ownerUsername;

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

    private Map<Integer, Appointment> appointments = new HashMap<>();
    private int nextId = 1;

    public synchronized Appointment addAppointment(int petId, int facilityId, String facilityType, String slot, String description, String ownerUsername) {
        Appointment appt = new Appointment(nextId++, petId, facilityId, facilityType, slot, description, ownerUsername);
        appointments.put(appt.id, appt);
        return appt;
    }

    public synchronized boolean cancelAppointment(int appointmentId) {
        return appointments.remove(appointmentId) != null;
    }

    public synchronized List<Appointment> getAppointmentsForFacility(int facilityId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.facilityId == facilityId) result.add(a);
        }
        return result;
    }

    public synchronized List<Appointment> getAppointmentsForUser(String ownerUsername) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.ownerUsername.equals(ownerUsername)) result.add(a);
        }
        return result;
    }

    public synchronized List<Appointment> getAppointmentsForPet(int petId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.petId == petId) result.add(a);
        }
        return result;
    }

    public synchronized Appointment getAppointmentById(int id) {
        return appointments.get(id);
    }

    public synchronized List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }
}
