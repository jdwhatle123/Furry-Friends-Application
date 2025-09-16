// Vet_Hospital.java
// This class models a veterinary hospital with its business details, services, and contact information.
package models;

import java.util.*;

public class Vet_Hospital {
    // Business name of the veterinary hospital
    private String businessName;
    // Name of the administrative agent
    private String adminAgent;
    // Street address
    private String street;
    // City location
    private String city;
    // State location
    private String state;
    // Zip code
    private String zip;
    // Main phone number
    private String phoneNumber;
    // Contact email
    private String email;
    // List of services offered by the hospital
    private List<String> services;
    // Business operating hours
    private String businessHours;
    // License number for the hospital
    private String licenseNumber;
    // Emergency contact information
    private String emergencyContact;
    // Website URL
    private String websiteUrl;
    // List of social media links
    private List<String> socialMediaLinks;

    // Constructor initializes all fields for the Vet_Hospital
    public Vet_Hospital(String businessName, String adminAgent, String street, String city, String state, String zip, String phoneNumber, String email, List<String> services, String businessHours, String licenseNumber, String emergencyContact, String websiteUrl, List<String> socialMediaLinks) {
        this.businessName = businessName;
        this.adminAgent = adminAgent;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
        // If services list is null, initialize as empty list
        this.services = services != null ? services : new ArrayList<>();
        this.businessHours = businessHours;
        this.licenseNumber = licenseNumber;
        this.emergencyContact = emergencyContact;
        this.websiteUrl = websiteUrl;
        // If social media links list is null, initialize as empty list
        this.socialMediaLinks = socialMediaLinks != null ? socialMediaLinks : new ArrayList<>();
    }

    // Getter for services list
    public List<String> getServices() { return services; }
    // Getter for business hours
    public String getBusinessHours() { return businessHours; }
    // Getter for license number
    public String getLicenseNumber() { return licenseNumber; }
    // Getter for emergency contact
    public String getEmergencyContact() { return emergencyContact; }
    // Getter for website URL
    public String getWebsiteUrl() { return websiteUrl; }
    // Getter for social media links
    public List<String> getSocialMediaLinks() { return socialMediaLinks; }

    // Returns a string representation of the Vet_Hospital object
    @Override
    public String toString() {
        return businessName + " (Admin: " + adminAgent + ") - " + street + ", " + city + ", " + state + " " + zip + ", Phone: " + phoneNumber + ", Email: " + email +
                ", Services: " + services + ", Hours: " + businessHours + ", License: " + licenseNumber + ", Emergency: " + emergencyContact + ", Website: " + websiteUrl + ", Social: " + socialMediaLinks;
    }
}
