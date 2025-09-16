// Shelter.java
// This class represents an animal shelter, including its business details, rooms, adoptable animals, and services.
// It provides methods to manage rooms and animals available for adoption.
package models;

import java.util.*;

public class Shelter {
    // Basic business information
    private String businessName;
    private String adminAgent;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String email;
    // List of rooms in the shelter
    private List<Room> rooms;
    // List of animals available for adoption
    private List<Pets> adoptionAnimals;
    // List of services provided by the shelter
    private List<String> services;
    private String businessHours;
    private String licenseNumber;
    private String emergencyContact;
    private String websiteUrl;
    private List<String> socialMediaLinks;

    // Inner class representing a room in the shelter
    public static class Room {
        public String name;
        public String size; // "Large", "Medium", "Small"

        public Room(String name, String size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String toString() {
            return name + " (" + size + ")";
        }
    }

    // Constructor initializes shelter details and lists
    public Shelter(String businessName, String adminAgent, String street, String city, String state, String zip, String phoneNumber, String email, List<String> services, String businessHours, String licenseNumber, String emergencyContact, String websiteUrl, List<String> socialMediaLinks) {
        this.businessName = businessName;
        this.adminAgent = adminAgent;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.services = services != null ? services : new ArrayList<>();
        this.businessHours = businessHours;
        this.licenseNumber = licenseNumber;
        this.emergencyContact = emergencyContact;
        this.websiteUrl = websiteUrl;
        this.socialMediaLinks = socialMediaLinks != null ? socialMediaLinks : new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.adoptionAnimals = new ArrayList<>();
    }

    // Add a new room to the shelter
    public void addRoom(String name, String size) {
        rooms.add(new Room(name, size));
    }

    // Get the list of rooms
    public List<Room> getRooms() {
        return rooms;
    }

    // Add a new animal available for adoption
    public void addAdoptionAnimal(Pets pet) {
        adoptionAnimals.add(pet);
    }

    // Get the list of animals available for adoption
    public List<Pets> getAdoptionAnimals() {
        return adoptionAnimals;
    }

    // Get the list of services provided
    public List<String> getServices() {
        return services;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public List<String> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    // String representation of the shelter's details
    @Override
    public String toString() {
        return businessName + " (Admin: " + adminAgent + ") - " + street + ", " + city + ", " + state + " " + zip + ", Phone: " + phoneNumber + ", Email: " + email +
                ", Services: " + services + ", Hours: " + businessHours + ", License: " + licenseNumber + ", Emergency: " + emergencyContact + ", Website: " + websiteUrl + ", Social: " + socialMediaLinks;
    }

}
