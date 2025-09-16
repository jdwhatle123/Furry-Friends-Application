// This class represents a user in the FurryFriends system, storing user details and providing a string representation.
package models;

public class User {
    // User's unique login identifier
    public String loginId;
    // User's first name
    public String firstName;
    // User's last name
    public String lastName;
    // User's street address
    public String street;
    // User's city
    public String city;
    // User's state
    public String state;
    // User's zip code
    public String zip;
    // User's phone number
    public String phoneNumber;
    // User's email address
    public String email;
    // Hashed password for security
    public String passwordHash;
    // User's role (e.g., admin, agent, user)
    public String role;
    // Admin or agent associated with the user
    public String adminAgent;

    // Constructor initializes all user fields
    public User(String loginId, String firstName, String lastName, String street, String city, String state, String zip, String phoneNumber, String email, String passwordHash, String role, String adminAgent) {
        this.loginId = loginId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.adminAgent = adminAgent;
    }

    // Returns a string summary of the user
    @Override
    public String toString() {
        return loginId + ": " + firstName + " " + lastName + " (" + role + ") - " + street + ", " + city + ", " + state + " " + zip;
    }
}
