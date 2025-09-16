// GUI_SceneController.java
// This class manages the main GUI scene transitions and user interactions for the FurryFriends application.
// It handles login, registration, password reset, user menu navigation, and appointment/facility management.

package gui;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.User;
import java.sql.*;
import java.util.*;

public class GUI_SceneController {
    // Reference to backend controller for business logic and DB access
    private Controller backendController;
    // Currently logged-in user
    private User currentUser;

    // Set the backend controller instance
    public void setBackendController(Controller controller) {
        this.backendController = controller;
    }
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private TextField regUsernameField;
    @FXML private PasswordField regPasswordField;
    @FXML private TextField regEmailField;
    @FXML private TextField regFirstNameField;
    @FXML private TextField regLastNameField;
    @FXML private TextField regStreetField;
    @FXML private TextField regCityField;
    @FXML private TextField regStateField;
    @FXML private TextField regZipField;
    @FXML private TextField regPhoneField;
    @FXML private ComboBox<String> regRoleBox;
    @FXML private Label regMessageLabel;
    @FXML private TextField forgotUsernameField;
    @FXML private TextField forgotEmailField;
    @FXML private PasswordField forgotNewPasswordField;
    @FXML private Label forgotMessageLabel;
    @FXML private VBox loginPane;
    @FXML private VBox registerPane;
    @FXML private VBox forgotPane;
    @FXML private VBox userMenuPane;
    @FXML private StackPane rootPane;
    @FXML private VBox userStep1Box;
    @FXML private VBox userStep2Box;
    @FXML private Label welcomeLabel;
    @FXML private Label userInfoLabel;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // Allow window dragging by mouse
        rootPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        rootPane.setOnMouseDragged(event -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    private void showRegisterPane() {
        // Switch to registration pane
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        registerPane.setVisible(true);
        registerPane.setManaged(true);
        forgotPane.setVisible(false);
        forgotPane.setManaged(false);
        userMenuPane.setVisible(false);
        userMenuPane.setManaged(false);
    }

    @FXML
    private void showForgotPane() {
        // Switch to forgot password pane
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        forgotPane.setVisible(true);
        forgotPane.setManaged(true);
        userMenuPane.setVisible(false);
        userMenuPane.setManaged(false);
    }

    @FXML
    private void showLoginPane() {
        // Switch to login pane
        loginPane.setVisible(true);
        loginPane.setManaged(true);
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        forgotPane.setVisible(false);
        forgotPane.setManaged(false);
        userMenuPane.setVisible(false);
        userMenuPane.setManaged(false);
        clearRegisterFields();
    }

    @FXML
    private void showUserMenuPane(String welcome, String userInfo) {
        // Show user menu after successful login
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        forgotPane.setVisible(false);
        forgotPane.setManaged(false);
        userMenuPane.setVisible(true);
        userMenuPane.setManaged(true);
        welcomeLabel.setText(welcome);
        userInfoLabel.setText(userInfo);
    }

    @FXML
    private void handleLogout() {
        // Log out and return to login pane
        showLoginPane();
    }

    @FXML
    private void handleLogin() {
        // Handle user login
        String username = usernameField.getText();
        String password = passwordField.getText();
        String result = backendController.login(username, password);
        if ("success".equals(result)) {
            User user = backendController.getUserByLoginId(username);
            currentUser = user;
            String welcome = "Welcome, " + user.firstName + "!";
            String userInfo = "Role: " + user.role + "\nEmail: " + user.email;
            showUserMenuPane(welcome, userInfo);
        } else {
            messageLabel.setText(result);
        }
    }

    @FXML
    private void handleRegister() {
        // Load registration screen (not used in main flow, kept for FXML navigation)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Register.fxml"));
            Parent registerRoot = loader.load();
            GUI_SceneController registerController = loader.getController();
            registerController.setBackendController(backendController);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(registerRoot));
        } catch (Exception e) {
            messageLabel.setText("Failed to load registration screen.");
        }
    }

    @FXML
    private void handleForgot() {
        // Load forgot password screen (not used in main flow, kept for FXML navigation)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ForgotPassword.fxml"));
            Parent forgotRoot = loader.load();
            GUI_SceneController forgotController = loader.getController();
            forgotController.setBackendController(backendController);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(forgotRoot));
        } catch (Exception e) {
            messageLabel.setText("Failed to load forgot password screen.");
        }
    }

    @FXML
    private void handleExit() {
        // Exit application
        System.exit(0);
    }

    @FXML
    private void submitRegister() {
        // Handle user registration form submission
        String username = regUsernameField.getText();
        String password = regPasswordField.getText();
        String email = regEmailField.getText();
        String firstName = regFirstNameField.getText();
        String lastName = regLastNameField.getText();
        String street = regStreetField.getText();
        String city = regCityField.getText();
        String state = regStateField.getText();
        String zip = regZipField.getText();
        String phone = regPhoneField.getText();
        String role = regRoleBox.getValue();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() ||
            firstName.isEmpty() || lastName.isEmpty() ||
            street.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty() || phone.isEmpty() || role == null) {
            System.out.println("All fields are required and password must be at least 4 characters.");
            return;
        }
        String result = backendController.register(
            regRoleBox.getValue(),           // role
            regUsernameField.getText(),      // loginId
            regFirstNameField.getText(),     // firstName
            regLastNameField.getText(),      // lastName
            "",                             // adminAgent (empty for Owner)
            regStreetField.getText(),        // street
            regCityField.getText(),          // city
            regStateField.getText(),         // state
            regZipField.getText(),           // zip
            regPhoneField.getText(),         // phoneNumber
            regEmailField.getText(),         // email
            regPasswordField.getText()       // passwordHash
        );
        if ("success".equals(result)) {
            System.out.println("Registration successful! Please log in.");
            clearRegisterFields();
        } else {
            System.out.println(result);
        }
    }

    @FXML
    private void submitForgotPassword() {
        // Handle forgot password form submission
        String username = forgotUsernameField.getText();
        String email = forgotEmailField.getText();
        String newPassword = forgotNewPasswordField.getText();
        if (username.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
            forgotMessageLabel.setText("All fields are required.");
            return;
        }
        String result = backendController.forgotPassword(username, email, newPassword);
        if ("success".equals(result)) {
            forgotMessageLabel.setText("Password reset successful! Please log in.");
        } else {
            forgotMessageLabel.setText(result);
        }
    }

    @FXML
    private void showUserStep2() {
        // Move to next registration step if role is selected
        if (regRoleBox.getValue() == null) {
            System.out.println("Please select an account type before continuing.");
            return;
        }
        userStep1Box.setVisible(false);
        userStep1Box.setManaged(false);
        userStep2Box.setVisible(true);
        userStep2Box.setManaged(true);
    }

    // Clear all registration fields and reset registration pane
    private void clearRegisterFields() {
        if (regUsernameField != null) regUsernameField.clear();
        if (regPasswordField != null) regPasswordField.clear();
        if (regEmailField != null) regEmailField.clear();
        if (regFirstNameField != null) regFirstNameField.clear();
        if (regLastNameField != null) regLastNameField.clear();
        if (regStreetField != null) regStreetField.clear();
        if (regCityField != null) regCityField.clear();
        if (regStateField != null) regStateField.clear();
        if (regZipField != null) regZipField.clear();
        if (regPhoneField != null) regPhoneField.clear();
        if (regRoleBox != null && regRoleBox.getSelectionModel() != null) regRoleBox.getSelectionModel().clearSelection();
        if (regMessageLabel != null) regMessageLabel.setText("");
        if (userStep1Box != null) {
            userStep1Box.setVisible(true);
            userStep1Box.setManaged(true);
        }
        if (userStep2Box != null) {
            userStep2Box.setVisible(false);
            userStep2Box.setManaged(false);
        }
    }

    // Get the currently logged-in user
    public User getCurrentUser() {
        return currentUser;
    }

    // Set the current user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    // Retrieve facilities by type from the database
    public List<Map<String, Object>> getFacilitiesByType(String facilityType) {
        List<Map<String, Object>> facilities = new ArrayList<>();
        try {
            Connection conn = backendController.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, name FROM facilities WHERE type = ?"
            );
            ps.setString(1, facilityType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> facility = new HashMap<>();
                facility.put("id", rs.getInt("id"));
                facility.put("name", rs.getString("name"));
                facilities.add(facility);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
        }
        return facilities;
    }

    // Add a new facility to the database
    public String addFacility(String name, String type, String phone, String street, String city, String state, String zip, String email, String businessHours, String licenseNumber, String emergencyContact, String websiteUrl, String adminAgent) {
        try {
            Connection conn = backendController.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO facilities (name, type, phoneNumber, street, city, state, zip, email, businessHours, licenseNumber, emergencyContact, websiteUrl, adminAgent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, phone);
            ps.setString(4, street);
            ps.setString(5, city);
            ps.setString(6, state);
            ps.setString(7, zip);
            ps.setString(8, email);
            ps.setString(9, businessHours);
            ps.setString(10, licenseNumber);
            ps.setString(11, emergencyContact);
            ps.setString(12, websiteUrl);
            ps.setString(13, adminAgent);
            ps.executeUpdate();
            ps.close();
            return "success";
        } catch (SQLException e) {
            return "Error adding facility: " + e.getMessage();
        }
    }

    // Get available slots for a facility
    public List<Map<String, Object>> getAvailableSlots(int facilityId) {
        List<Map<String, Object>> slots = new ArrayList<>();
        try {
            Connection conn = backendController.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, slot FROM facility_slots WHERE facilityId = ? AND booked = 0"
            );
            ps.setInt(1, facilityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> slot = new HashMap<>();
                slot.put("id", rs.getInt("id"));
                slot.put("slot", rs.getString("slot"));
                slots.add(slot);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
        }
        return slots;
    }

    // Add a new appointment and mark slot as booked
    public String addAppointment(int petId, int facilityId, int slotId, String description, String ownerUsername) {
        try {
            Connection conn = backendController.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO appointments (petId, facilityId, slotId, ownerUsername, description) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setInt(1, petId);
            ps.setInt(2, facilityId);
            ps.setInt(3, slotId);
            ps.setString(4, ownerUsername);
            ps.setString(5, description);
            ps.executeUpdate();
            ps.close();
            // Mark slot as booked
            ps = conn.prepareStatement("UPDATE facility_slots SET booked = 1 WHERE id = ?");
            ps.setInt(1, slotId);
            ps.executeUpdate();
            ps.close();
            return "success";
        } catch (SQLException e) {
            return "Error adding appointment: " + e.getMessage();
        }
    }

    // Get all appointments for a user
    public List<Map<String, Object>> getAppointmentsForUser(String username) {
        List<Map<String, Object>> appts = new ArrayList<>();
        try {
            Connection conn = backendController.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT a.id, a.petId, a.facilityId, f.type as facilityType, s.slot, a.description " +
                "FROM appointments a " +
                "JOIN facility_slots s ON a.slotId = s.id " +
                "JOIN facilities f ON a.facilityId = f.id " +
                "WHERE a.ownerUsername = ?"
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> appt = new HashMap<>();
                appt.put("id", rs.getInt("id"));
                appt.put("petId", rs.getInt("petId"));
                appt.put("facilityId", rs.getInt("facilityId"));
                appt.put("facilityType", rs.getString("facilityType"));
                appt.put("slot", rs.getString("slot"));
                appt.put("description", rs.getString("description"));
                appts.add(appt);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
        }
        return appts;
    }

    // Reschedule an appointment to a new slot
    public String rescheduleAppointment(int appointmentId, int newSlotId, int oldSlotId) {
        try {
            Connection conn = backendController.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE appointments SET slotId = ? WHERE id = ?");
            ps.setInt(1, newSlotId);
            ps.setInt(2, appointmentId);
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement("UPDATE facility_slots SET booked = 1 WHERE id = ?");
            ps.setInt(1, newSlotId);
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement("UPDATE facility_slots SET booked = 0 WHERE id = ?");
            ps.setInt(1, oldSlotId);
            ps.executeUpdate();
            ps.close();
            return "success";
        } catch (SQLException e) {
            return "Error rescheduling appointment: " + e.getMessage();
        }
    }
}
