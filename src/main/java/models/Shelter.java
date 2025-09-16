package models;

import java.util.*;

public class Shelter {
    private String businessName;
    private String adminAgent;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String email;
    private List<Room> rooms;
    private List<Pets> adoptionAnimals;
    private List<String> services;
    private String businessHours;
    private String licenseNumber;
    private String emergencyContact;
    private String websiteUrl;
    private List<String> socialMediaLinks;

    public static class Room {
        public String name;
        public String size;

        public Room(String name, String size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String toString() { return name + " (" + size + ")"; }
    }

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

    public void addRoom(String name, String size) { rooms.add(new Room(name, size)); }
    public List<Room> getRooms() { return rooms; }
    public void addAdoptionAnimal(Pets pet) { adoptionAnimals.add(pet); }
    public List<Pets> getAdoptionAnimals() { return adoptionAnimals; }
    public List<String> getServices() { return services; }
    public String getBusinessHours() { return businessHours; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getEmergencyContact() { return emergencyContact; }
    public String getWebsiteUrl() { return websiteUrl; }
    public List<String> getSocialMediaLinks() { return socialMediaLinks; }

    @Override
    public String toString() {
        return businessName + " (Admin: " + adminAgent + ") - " + street + ", " + city + ", " + state + " " + zip + ", Phone: " + phoneNumber + ", Email: " + email +
                ", Services: " + services + ", Hours: " + businessHours + ", License: " + licenseNumber + ", Emergency: " + emergencyContact + ", Website: " + websiteUrl + ", Social: " + socialMediaLinks;
    }
}
