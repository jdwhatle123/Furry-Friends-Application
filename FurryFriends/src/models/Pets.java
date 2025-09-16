package models;

import java.util.*;

public class Pets {
    public int id;
    public String name;
    public String breed;
    public int age;
    public String diet;
    public String medicalHistory;
    public String ownerUsername;
    public List<Medication> medications;

    public static class Medication {
        public String name;
        public String dosage;
        public String frequency;

        public Medication(String name, String dosage, String frequency) {
            this.name = name;
            this.dosage = dosage;
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return name + " (" + dosage + ", " + frequency + ")";
        }
    }

    public Pets(int id, String name, String breed, int age, String diet, String medicalHistory, String ownerUsername) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.diet = diet;
        this.medicalHistory = medicalHistory;
        this.ownerUsername = ownerUsername;
        this.medications = new ArrayList<>();
    }

    @Override
    public String toString() {
        return id + ". " + name + " (" + breed + ", " + age + " yrs)" + (medications.isEmpty() ? "" : ", Medications: " + medications);
    }
}
