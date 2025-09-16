package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.User;
import models.Calendar;
import models.JournalEntry;
import models.Pets;
import models.Shelter;
import models.Vet_Hospital;

public class Controller {
    private Connection conn; // Database connection
    private Calendar calendar; // Calendar model instance
    private List<JournalEntry> journalEntries; // List of journal entries
    private List<Pets> pets; // List of pets
    private List<Shelter> shelters; // List of shelters
    private List<Vet_Hospital> vetHospitals; // List of vet hospitals

    // Constructor initializes lists and calendar
    public Controller(Connection conn) {
        this.conn = conn;
        this.calendar = new Calendar();
        this.journalEntries = new ArrayList<>();
        this.pets = new ArrayList<>();
        this.shelters = new ArrayList<>();
        this.vetHospitals = new ArrayList<>();
    }

    public void ensureTablesExist() throws SQLException {
        Statement stmt = conn.createStatement();
        // Enforce referential integrity
        stmt.execute("PRAGMA foreign_keys = ON");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
            "loginId TEXT PRIMARY KEY," +
            "firstName TEXT NOT NULL," +
            "lastName TEXT NOT NULL," +
            "street TEXT NOT NULL," +
            "city TEXT NOT NULL," +
            "state TEXT NOT NULL," +
            "zip TEXT NOT NULL," +
            "phoneNumber TEXT NOT NULL," +
            "email TEXT NOT NULL," +
            "passwordHash TEXT NOT NULL," +
            "role TEXT NOT NULL CHECK(role IN ('Owner', 'Vet Hospital', 'Shelter'))," +
            "adminAgent TEXT" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS facilities (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "type TEXT NOT NULL CHECK(type IN ('Vet Hospital', 'Shelter'))," +
            "phoneNumber TEXT NOT NULL," +
            "street TEXT NOT NULL," +
            "city TEXT NOT NULL," +
            "state TEXT NOT NULL," +
            "zip TEXT NOT NULL," +
            "email TEXT," +
            "businessHours TEXT," +
            "licenseNumber TEXT," +
            "emergencyContact TEXT," +
            "websiteUrl TEXT," +
            "adminAgent TEXT" +
        ");");
        try { stmt.execute("ALTER TABLE facilities ADD COLUMN adminAgent TEXT;"); } catch (SQLException ignore) {}
        stmt.execute("CREATE TABLE IF NOT EXISTS facility_services (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "facilityId INTEGER NOT NULL," +
            "service TEXT NOT NULL," +
            "FOREIGN KEY(facilityId) REFERENCES facilities(id)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS facility_social_media (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "facilityId INTEGER NOT NULL," +
            "url TEXT NOT NULL," +
            "FOREIGN KEY(facilityId) REFERENCES facilities(id)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS facility_slots (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "facilityId INTEGER NOT NULL," +
            "slot TEXT NOT NULL," +
            "booked INTEGER DEFAULT 0," +
            "FOREIGN KEY(facilityId) REFERENCES facilities(id)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS appointments (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "petId INTEGER NOT NULL," +
            "slotId INTEGER NOT NULL," +
            "description TEXT," +
            "ownerUsername TEXT NOT NULL," +
            "facilityId INTEGER NOT NULL," +
            "FOREIGN KEY(petId) REFERENCES pets(id)," +
            "FOREIGN KEY(ownerUsername) REFERENCES users(loginId)," +
            "FOREIGN KEY(facilityId) REFERENCES facilities(id)," +
            "FOREIGN KEY(slotId) REFERENCES facility_slots(id)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS pets (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "breed TEXT NOT NULL," +
            "age INTEGER NOT NULL," +
            "diet TEXT," +
            "medicalHistory TEXT," +
            "ownerUsername TEXT NOT NULL," +
            "FOREIGN KEY(ownerUsername) REFERENCES users(loginId)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS pet_medications (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "petId INTEGER NOT NULL," +
            "name TEXT NOT NULL," +
            "dosage TEXT," +
            "frequency TEXT," +
            "FOREIGN KEY(petId) REFERENCES pets(id)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS journal_entries (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "petId INTEGER NOT NULL," +
            "date TEXT NOT NULL," +
            "note TEXT NOT NULL," +
            "ownerUsername TEXT NOT NULL," +
            "title TEXT," +
            "visibility TEXT," +
            "createdAt TEXT," +
            "FOREIGN KEY(petId) REFERENCES pets(id)," +
            "FOREIGN KEY(ownerUsername) REFERENCES users(loginId)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS rooms (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "facilityId INTEGER NOT NULL," +
            "name TEXT NOT NULL," +
            "size TEXT NOT NULL," +
            "FOREIGN KEY(facilityId) REFERENCES facilities(id)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS room_availability (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "roomId INTEGER NOT NULL," +
            "date TEXT NOT NULL," +
            "available INTEGER DEFAULT 1," +
            "FOREIGN KEY(roomId) REFERENCES rooms(id)" +
        ");");
        stmt.execute("CREATE TABLE IF NOT EXISTS room_bookings (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "roomId INTEGER NOT NULL," +
            "date TEXT NOT NULL," +
            "bookedBy TEXT NOT NULL," +
            "petId INTEGER NOT NULL," +
            "FOREIGN KEY(roomId) REFERENCES rooms(id)," +
            "FOREIGN KEY(bookedBy) REFERENCES users(loginId)," +
            "FOREIGN KEY(petId) REFERENCES pets(id)" +
        ");");

        // Indexes for performance on common query paths
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_users_role ON users(role)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_facilities_admin ON facilities(adminAgent)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_facilities_type ON facilities(type)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_slots_facility_booked ON facility_slots(facilityId, booked)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_appts_owner ON appointments(ownerUsername)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_appts_facility ON appointments(facilityId)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_pets_owner ON pets(ownerUsername)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_roomavail_room ON room_availability(roomId)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_roombook_room ON room_bookings(roomId)");
        stmt.close();
    }

    public String login(String username, String password) {
        User u = getUserByLoginId(username);
        if (u != null && libraries.BCrypt.checkpw(password, u.passwordHash)) {
            return "success";
        } else {
            return "Invalid login. Try again.";
        }
    }

    public String register(String role, String loginId, String firstName, String lastName, String adminAgent, String street, String city, String state, String zip, String phone, String email, String password) {
        if (getUserByLoginId(loginId) != null) {
            return "Login ID already exists.";
        }
        if (loginId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty() || phone.isEmpty() || email.isEmpty() || password.length() < 4) {
            return "All fields are required and password must be at least 4 characters.";
        }
        String hash = libraries.BCrypt.hashpw(password, libraries.BCrypt.gensalt());
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (loginId, firstName, lastName, street, city, state, zip, phoneNumber, email, passwordHash, role, adminAgent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, loginId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, street);
            ps.setString(5, city);
            ps.setString(6, state);
            ps.setString(7, zip);
            ps.setString(8, phone);
            ps.setString(9, email);
            ps.setString(10, hash);
            ps.setString(11, role);
            ps.setString(12, adminAgent);
            ps.executeUpdate();
            ps.close();
            if (role.equals("Vet Hospital") || role.equals("Shelter")) {
                PreparedStatement fps = conn.prepareStatement(
                    "INSERT INTO facilities (name, type, phoneNumber, street, city, state, zip, email, businessHours, licenseNumber, emergencyContact, websiteUrl, adminAgent) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, '', '', '', '', ?)"
                );
                fps.setString(1, firstName);
                fps.setString(2, role);
                fps.setString(3, phone);
                fps.setString(4, street);
                fps.setString(5, city);
                fps.setString(6, state);
                fps.setString(7, zip);
                fps.setString(8, email);
                fps.setString(9, loginId);
                fps.executeUpdate();
                fps.close();
            }
            return "success";
        } catch (SQLException e) {
            return "Registration error: " + e.getMessage();
        }
    }

    public String forgotPassword(String username, String email, String newPassword) {
        User u = getUserByLoginId(username);
        if (u != null && u.email.equals(email)) {
            if (newPassword.length() < 4) {
                return "Password must be at least 4 characters.";
            }
            String hash = libraries.BCrypt.hashpw(newPassword, libraries.BCrypt.gensalt());
            try {
                PreparedStatement ps = conn.prepareStatement("UPDATE users SET passwordHash = ? WHERE loginId = ?");
                ps.setString(1, hash);
                ps.setString(2, username);
                ps.executeUpdate();
                ps.close();
                return "success";
            } catch (SQLException e) {
                return "Error resetting password: " + e.getMessage();
            }
        } else {
            return "Invalid Login ID or Email Address.";
        }
    }

    public User getUserByLoginId(String loginId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE loginId = ?");
            ps.setString(1, loginId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User(
                    rs.getString("loginId"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("street"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("zip"),
                    rs.getString("phoneNumber"),
                    rs.getString("email"),
                    rs.getString("passwordHash"),
                    rs.getString("role"),
                    rs.getString("adminAgent")
                );
                rs.close();
                ps.close();
                return u;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
        }
        return null;
    }

    public Connection getConnection() { return conn; }
    public Calendar getCalendar() { return calendar; }
    public List<JournalEntry> getJournalEntries() { return journalEntries; }
    public List<Pets> getPets() { return pets; }
    public List<Shelter> getShelters() { return shelters; }
    public List<Vet_Hospital> getVetHospitals() { return vetHospitals; }
}
