package models;

public class User {
    public String loginId;
    public String firstName;
    public String lastName;
    public String street;
    public String city;
    public String state;
    public String zip;
    public String phoneNumber;
    public String email;
    public String passwordHash;
    public String role;
    public String adminAgent;

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

    @Override
    public String toString() {
        return loginId + ": " + firstName + " " + lastName + " (" + role + ") - " + street + ", " + city + ", " + state + " " + zip;
    }
}
