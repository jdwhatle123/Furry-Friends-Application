package gui;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import models.User;
import service.NotificationService;
import java.sql.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;
import javafx.application.Platform;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public class GUI_SceneController {
    private Controller backendController;
    private User currentUser;
    private final NotificationService notificationService = new NotificationService();

    public void setBackendController(Controller controller) { this.backendController = controller; }

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button forgotButton;
    @FXML private Button exitButton;
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
    @FXML private HBox vetShelterActions;
    @FXML private HBox ownerActions;
    @FXML private Button bookShelterRoomButton;
    @FXML private TitledPane adminPanel;
    @FXML private TitledPane lookupPanel;
    @FXML private TextField lookupOwnerField;
    @FXML private TextArea lookupResults;
    @FXML private TextField adminFacilityName;
    @FXML private TextField adminPhone;
    @FXML private TextField adminStreet;
    @FXML private TextField adminCity;
    @FXML private TextField adminState;
    @FXML private TextField adminZip;
    @FXML private TextField adminEmail;
    @FXML private TextField adminHours;
    @FXML private TextField adminWebsite;
    @FXML private TextField adminCustomSlot;
    @FXML private TitledPane emergencyPanel;
    @FXML private TextField emergencyPetNameField;
    @FXML private TextArea emergencyOutput;

    @FXML private TextField petHistoryPetIdField;
    @FXML private TextArea petHistoryResults;

    // Notification overlay
    @FXML private StackPane notificationOverlay;
    @FXML private Label notificationLabel;
    private SequentialTransition notificationAnimation;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // Bridge NotificationService -> UI toast, but only surface messages for the active user
        notificationService.addListener((userLoginId, message, type) -> {
            if (currentUser != null && currentUser.loginId != null && currentUser.loginId.equals(userLoginId)) {
                showNotification(message, type);
            }
        });
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

    @FXML private void showRegisterPane() {
        loginPane.setVisible(false); loginPane.setManaged(false);
        registerPane.setVisible(true); registerPane.setManaged(true);
        forgotPane.setVisible(false); forgotPane.setManaged(false);
        userMenuPane.setVisible(false); userMenuPane.setManaged(false);
    }

    @FXML private void showForgotPane() {
        loginPane.setVisible(false); loginPane.setManaged(false);
        registerPane.setVisible(false); registerPane.setManaged(false);
        forgotPane.setVisible(true); forgotPane.setManaged(true);
        userMenuPane.setVisible(false); userMenuPane.setManaged(false);
    }

    @FXML private void showLoginPane() {
        loginPane.setVisible(true); loginPane.setManaged(true);
        registerPane.setVisible(false); registerPane.setManaged(false);
        forgotPane.setVisible(false); forgotPane.setManaged(false);
        userMenuPane.setVisible(false); userMenuPane.setManaged(false);
        clearRegisterFields();
    }

    // Register: go from Step 1 to Step 2
    @FXML private void showUserStep2() {
        if (userStep1Box != null) { userStep1Box.setVisible(false); userStep1Box.setManaged(false); }
        if (userStep2Box != null) { userStep2Box.setVisible(true); userStep2Box.setManaged(true); }
    }

    // Overload to satisfy FXML event handler resolution expecting an ActionEvent arg
    @FXML private void showUserStep2(ActionEvent e) { showUserStep2(); }

    @FXML private void showUserMenuPane(String welcome, String userInfo) {
        loginPane.setVisible(false); loginPane.setManaged(false);
        registerPane.setVisible(false); registerPane.setManaged(false);
        forgotPane.setVisible(false); forgotPane.setManaged(false);
        userMenuPane.setVisible(true); userMenuPane.setManaged(true);
        welcomeLabel.setText(welcome);
        userInfoLabel.setText(userInfo);

        // Role-based UI gating
        boolean isVetOrShelter = currentUser != null && ("Vet Hospital".equals(currentUser.role) || "Shelter".equals(currentUser.role));
        boolean isOwner = currentUser != null && "Owner".equals(currentUser.role);
    if (vetShelterActions != null) { vetShelterActions.setVisible(isVetOrShelter); vetShelterActions.setManaged(isVetOrShelter); }
    if (ownerActions != null) { ownerActions.setVisible(isOwner); ownerActions.setManaged(isOwner); }
    if (adminPanel != null) { adminPanel.setVisible(isVetOrShelter); adminPanel.setManaged(isVetOrShelter); }
    if (lookupPanel != null) { lookupPanel.setVisible(isVetOrShelter); lookupPanel.setManaged(isVetOrShelter); }
    if (emergencyPanel != null) { emergencyPanel.setVisible(isVetOrShelter); emergencyPanel.setManaged(isVetOrShelter); }

        if (isVetOrShelter) {
            // Preload facility details into admin panel fields
            try {
                Connection conn = backendController.getConnection();
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, name, phoneNumber, street, city, state, zip, email, businessHours, websiteUrl FROM facilities WHERE adminAgent = ? LIMIT 1")) {
                    ps.setString(1, currentUser.loginId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            if (adminFacilityName != null) adminFacilityName.setText(rs.getString("name"));
                            if (adminPhone != null) adminPhone.setText(rs.getString("phoneNumber"));
                            if (adminStreet != null) adminStreet.setText(rs.getString("street"));
                            if (adminCity != null) adminCity.setText(rs.getString("city"));
                            if (adminState != null) adminState.setText(rs.getString("state"));
                            if (adminZip != null) adminZip.setText(rs.getString("zip"));
                            if (adminEmail != null) adminEmail.setText(rs.getString("email"));
                            if (adminHours != null) adminHours.setText(rs.getString("businessHours"));
                            if (adminWebsite != null) adminWebsite.setText(rs.getString("websiteUrl"));
                        }
                    }
                }
            } catch (SQLException ignore) {}
            // Rename shelter button label per role for clarity
            if (bookShelterRoomButton != null) {
                if ("Shelter".equals(currentUser.role)) {
                    bookShelterRoomButton.setText("View Room Bookings");
                } else if ("Vet Hospital".equals(currentUser.role)) {
                    bookShelterRoomButton.setText("Book Shelter Room");
                }
            }
        }
    }

    @FXML private void handleLogout() {
        if (currentUser != null) {
            try { notificationService.cancelAllForUser(currentUser.loginId); } catch (Exception ignore) {}
        }
        currentUser = null;
        showLoginPane();
    }

    @FXML private void handleAddSampleSlots() {
        if (currentUser == null) return;
        try {
            var conn = backendController.getConnection();
            // Find facility owned/managed by this user (adminAgent == loginId)
            Integer facId = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM facilities WHERE adminAgent = ? LIMIT 1")) {
                ps.setString(1, currentUser.loginId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) facId = rs.getInt(1); }
            }
            if (facId == null) {
                new Alert(Alert.AlertType.INFORMATION, "No facility found for this user.").showAndWait();
                return;
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO facility_slots (facilityId, slot, booked) VALUES (?, ?, 0)")) {
                ps.setInt(1, facId); ps.setString(2, java.time.LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).toString()); ps.executeUpdate();
                ps.setInt(1, facId); ps.setString(2, java.time.LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).toString()); ps.executeUpdate();
            }
            new Alert(Alert.AlertType.INFORMATION, "Added two sample slots to facility " + facId).showAndWait();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error adding slots: " + e.getMessage()).showAndWait();
        }
    }

    @FXML private void handleListMySlots() {
        if (currentUser == null) return;
        try {
            var conn = backendController.getConnection();
            Integer facId = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM facilities WHERE adminAgent = ? LIMIT 1")) {
                ps.setString(1, currentUser.loginId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) facId = rs.getInt(1); }
            }
            if (facId == null) {
                new Alert(Alert.AlertType.INFORMATION, "No facility found for this user.").showAndWait();
                return;
            }
            StringBuilder sb = new StringBuilder();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, slot, booked FROM facility_slots WHERE facilityId = ? ORDER BY slot")) {
                ps.setInt(1, facId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        sb.append("[#").append(rs.getInt("id")).append("] ")
                          .append(rs.getString("slot")).append(" — ")
                          .append(rs.getInt("booked") == 1 ? "BOOKED" : "OPEN").append("\n");
                    }
                }
            }
            if (sb.length() == 0) sb.append("No slots yet.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Facility Slots");
            alert.setHeaderText(null);
            alert.setContentText(sb.toString());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error listing slots: " + e.getMessage()).showAndWait();
        }
    }

    @FXML private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String result = backendController.login(username, password);
        if ("success".equals(result)) {
            User user = backendController.getUserByLoginId(username);
            currentUser = user;
            String welcome = "Welcome, " + user.firstName + "!";
            String userInfo = "Role: " + user.role + "\nEmail: " + user.email;
            showUserMenuPane(welcome, userInfo);
            showNotification("Logged in as " + user.role, "success");

            // Schedule gentle reminders for upcoming appointments (15 min before), capped to next 5
            try {
                java.sql.Connection conn = backendController.getConnection();
                try (java.sql.PreparedStatement ps = conn.prepareStatement(
                        "SELECT s.slot, a.description FROM appointments a JOIN facility_slots s ON a.slotId = s.id WHERE a.ownerUsername = ? ORDER BY s.slot LIMIT 5")) {
                    ps.setString(1, user.loginId);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String slotStr = rs.getString(1);
                            String desc = java.util.Optional.ofNullable(rs.getString(2)).orElse("Appointment");
                            try {
                                java.time.LocalDateTime slot = java.time.LocalDateTime.parse(slotStr);
                                java.time.LocalDateTime remindAt = slot.minusMinutes(15);
                                notificationService.scheduleReminder(user.loginId, remindAt, java.time.ZoneId.systemDefault(),
                                        desc + " at " + slotStr + " in 15 minutes", "info");
                            } catch (Exception ignore) { /* bad slot format? skip */ }
                        }
                    }
                }
            } catch (Exception ignore) { }
        } else {
            messageLabel.setText(result);
            showNotification("Login failed: " + result, "error");
        }
    }

    @FXML private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Register.fxml"));
            Parent registerRoot = loader.load();
            GUI_SceneController registerController = loader.getController();
            registerController.setBackendController(backendController);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(registerRoot));
        } catch (Exception e) {
            messageLabel.setText("Failed to load registration screen.");
            showNotification("Failed to load registration screen.", "error");
        }
    }

    @FXML private void handleForgot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ForgotPassword.fxml"));
            Parent forgotRoot = loader.load();
            GUI_SceneController forgotController = loader.getController();
            forgotController.setBackendController(backendController);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(forgotRoot));
        } catch (Exception e) {
            messageLabel.setText("Failed to load forgot password screen.");
            showNotification("Failed to load forgot password screen.", "error");
        }
    }

    @FXML private void handleExit() {
        try { notificationService.close(); } catch (Exception ignore) {}
        System.exit(0);
    }

    // Owner actions
    @FXML private void handleBookAppointment() {
        if (currentUser == null) return;
        try {
            Connection conn = backendController.getConnection();
            // Gather all open slots across Vet Hospitals
            List<Map<String, Object>> slots = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(
                "SELECT s.id as slotId, s.slot as slot, f.id as facilityId, f.name as facilityName " +
                "FROM facility_slots s JOIN facilities f ON s.facilityId = f.id " +
                "WHERE f.type = 'Vet Hospital' AND s.booked = 0 ORDER BY s.slot"
            )) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("slotId", rs.getInt("slotId"));
                        row.put("slot", rs.getString("slot"));
                        row.put("facilityId", rs.getInt("facilityId"));
                        row.put("facilityName", rs.getString("facilityName"));
                        slots.add(row);
                    }
                }
            }
            if (slots.isEmpty()) { new Alert(Alert.AlertType.INFORMATION, "No open vet slots to book.").showAndWait(); return; }

            // Let owner select a slot (string labels mapped to data)
            List<String> labels = new ArrayList<>();
            Map<String, Map<String,Object>> mapByLabel = new LinkedHashMap<>();
            for (Map<String,Object> m : slots) {
                String label = m.get("slot") + " — " + m.get("facilityName");
                labels.add(label);
                mapByLabel.put(label, m);
            }
            ChoiceDialog<String> dialog = new ChoiceDialog<>(labels.get(0), labels);
            dialog.setTitle("Select Vet Slot");
            dialog.setHeaderText("Choose a vet hospital time slot");
            dialog.setContentText("Slot:");
            Optional<String> chosen = dialog.showAndWait();
            if (chosen.isEmpty()) return;
            Map<String,Object> pick = mapByLabel.get(chosen.get());
            int slotId = (int) pick.get("slotId");
            int facilityId = (int) pick.get("facilityId");

            // Ask for vaccination/type or reason
            TextInputDialog reasonDlg = new TextInputDialog("Rabies vaccination");
            reasonDlg.setTitle("Visit Reason");
            reasonDlg.setHeaderText("Enter vaccination/type or visit reason");
            reasonDlg.setContentText("Reason:");
            String reason = reasonDlg.showAndWait().orElse("");

            // Create a demo pet (or pick first existing pet if you want to change later)
            int petId;
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO pets (name, breed, age, ownerUsername) VALUES ('DemoPet','Mixed',1,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, currentUser.loginId); ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); petId = rs.getInt(1); }
            }
            String res = addAppointment(petId, facilityId, slotId, reason.isBlank() ? "General Checkup" : reason, currentUser.loginId);
            if ("success".equals(res)) {
                new Alert(Alert.AlertType.INFORMATION, "Appointment booked!").showAndWait();
                showNotification("Appointment booked!", "success");
                // Schedule a reminder 15 minutes before the slot
                try {
                    String slotStr = (String) pick.get("slot");
                    java.time.LocalDateTime slot = java.time.LocalDateTime.parse(slotStr);
                    java.time.LocalDateTime remindAt = slot.minusMinutes(15);
                    notificationService.scheduleReminder(currentUser.loginId, remindAt, java.time.ZoneId.systemDefault(),
                            "Upcoming visit at " + slotStr + " (15 min)", "info");
                } catch (Exception ignore) { }
            } else {
                new Alert(Alert.AlertType.ERROR, res).showAndWait();
                showNotification(res, "error");
            }
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); showNotification("Booking error: " + e.getMessage(), "error"); }
    }

    @FXML private void handleMyAppointments() {
        if (currentUser == null) return;
        List<Map<String, Object>> appts = getAppointmentsForUser(currentUser.loginId);
        StringBuilder sb = new StringBuilder();
        for (var a : appts) {
            sb.append("[#").append(a.get("id")).append("] pet=").append(a.get("petId")).append(", ")
              .append(a.get("facilityType")).append(" @ ").append(a.get("slot")).append(" — ")
              .append(a.get("description")).append("\n");
        }
        if (sb.length() == 0) sb.append("No appointments yet.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString()); alert.setHeaderText("My Appointments"); alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); alert.showAndWait();
    }

    // Owner: list my pets with IDs
    @FXML private void handleMyPets() {
        if (currentUser == null) return;
        if (!"Owner".equals(currentUser.role)) {
            new Alert(Alert.AlertType.INFORMATION, "Only Owners can view their pets.").showAndWait();
            return;
        }
        try {
            Connection conn = backendController.getConnection();
            StringBuilder sb = new StringBuilder();
            java.util.List<String> labels = new java.util.ArrayList<>();
            java.util.Map<String, Integer> idByLabel = new java.util.LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, name, breed, age FROM pets WHERE ownerUsername = ? ORDER BY id DESC")) {
                ps.setString(1, currentUser.loginId);
                try (ResultSet rs = ps.executeQuery()) {
                    int c = 0;
                    while (rs.next()) {
                        c++;
                        int id = rs.getInt("id");
                        String label = "[#" + id + "] " + rs.getString("name") + " — " +
                                java.util.Optional.ofNullable(rs.getString("breed")).orElse("") + ", age " + rs.getInt("age");
                        labels.add(label);
                        idByLabel.put(label, id);
                        sb.append(label).append("\n");
                    }
                    if (c == 0) sb.append("No pets found.");
                }
            }
            // Show the list; also offer to copy an ID to clipboard if there are entries
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Pets");
            alert.setHeaderText("Your Pets (select to copy ID)");
            alert.setContentText(sb.toString());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            if (!labels.isEmpty()) {
                ButtonType copyBtn = new ButtonType("Copy Pet ID", ButtonBar.ButtonData.OK_DONE);
                ButtonType closeBtn = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(copyBtn, closeBtn);
                java.util.Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get() == copyBtn) {
                    ChoiceDialog<String> chooser = new ChoiceDialog<>(labels.get(0), labels);
                    chooser.setTitle("Copy Pet ID");
                    chooser.setHeaderText("Choose a pet to copy its ID");
                    chooser.setContentText("Pet:");
                    java.util.Optional<String> pick = chooser.showAndWait();
                    if (pick.isPresent()) {
                        Integer id = idByLabel.get(pick.get());
                        if (id != null) {
                            final StringSelection sel = new StringSelection(String.valueOf(id));
                            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
                            showNotification("Copied Pet ID #" + id + " to clipboard", "success");
                        }
                    }
                }
            } else {
                alert.showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading pets: " + e.getMessage()).showAndWait();
        }
    }

    // Vet/Shelter: view scheduled appointments (with vaccination/type description)
    @FXML private void handleViewFacilityAppointments() {
        if (currentUser == null) return;
        boolean isVetOrShelter = "Vet Hospital".equals(currentUser.role) || "Shelter".equals(currentUser.role);
        if (!isVetOrShelter) { new Alert(Alert.AlertType.INFORMATION, "Only Vet/Shelter can view facility appointments.").showAndWait(); return; }
        try {
            Connection conn = backendController.getConnection();
            Integer facId = null;
            String facType = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, type FROM facilities WHERE adminAgent = ? LIMIT 1")) {
                ps.setString(1, currentUser.loginId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { facId = rs.getInt(1); facType = rs.getString(2);} }
            }
            if (facId == null) { new Alert(Alert.AlertType.INFORMATION, "No facility found for this user.").showAndWait(); return; }
            StringBuilder sb = new StringBuilder();
            boolean hadAppts = false;
            // For Vet-side cancellation: map label -> { apptId, slotId }
            List<String> apptLabels = new ArrayList<>();
            Map<String, Map<String,Object>> apptByLabel = new LinkedHashMap<>();

            // Always show standard appointments tied to this facility (for vet hospitals especially)
                        String apptSql = "SELECT a.id, a.description, a.ownerUsername, a.petId, s.slot, s.id as slotId FROM appointments a JOIN facility_slots s ON a.slotId = s.id WHERE a.facilityId = ? ORDER BY s.slot";
            try (PreparedStatement ps = conn.prepareStatement(apptSql)) {
                ps.setInt(1, facId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (!hadAppts) { sb.append("Appointments\n"); hadAppts = true; }
                                                int apptId = rs.getInt("id");
                                                int slotId = rs.getInt("slotId");
                                                String slotStr = rs.getString("slot");
                                                sb.append("[APPT #").append(apptId).append("] ")
                                                    .append(slotStr).append(" — ")
                          .append("pet=").append(rs.getInt("petId")).append(", owner=").append(rs.getString("ownerUsername")).append("\n")
                          .append("  request: ").append(Optional.ofNullable(rs.getString("description")).orElse("(none)")).append("\n");

                                                // Build selectable label for cancellation
                                                String label = "[APPT #" + apptId + "] " + slotStr;
                                                apptLabels.add(label);
                                                Map<String,Object> rec = new HashMap<>();
                                                rec.put("appointmentId", apptId);
                                                rec.put("slotId", slotId);
                                                apptByLabel.put(label, rec);
                    }
                }
            }

            // If this is a Shelter, also show room bookings for context
            boolean hadRooms = false;
            if ("Shelter".equals(facType)) {
                String roomSql = "SELECT rb.id as bookingId, rb.date, rb.bookedBy, rb.petId, r.name as roomName " +
                        "FROM room_bookings rb JOIN rooms r ON rb.roomId = r.id WHERE r.facilityId = ? ORDER BY rb.date";
                try (PreparedStatement ps = conn.prepareStatement(roomSql)) {
                    ps.setInt(1, facId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            if (!hadRooms) { if (sb.length() > 0) sb.append("\n"); sb.append("Shelter Room Bookings\n"); hadRooms = true; }
                            sb.append("[ROOM #").append(rs.getInt("bookingId")).append("] ")
                              .append(rs.getString("date")).append(" — room=")
                              .append(Optional.ofNullable(rs.getString("roomName")).orElse("?"))
                              .append(", pet=").append(rs.getInt("petId"))
                              .append(", bookedBy=").append(Optional.ofNullable(rs.getString("bookedBy")).orElse(""))
                              .append("\n");
                        }
                    }
                }
            }

            if (sb.length() == 0) sb.append("No appointments scheduled.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Facility Appointments");
            alert.setHeaderText("Facility Appointments");
            TextArea content = new TextArea(sb.toString());
            content.setEditable(false);
            content.setWrapText(true);
            content.setPrefWidth(720);
            content.setPrefHeight(420);
            alert.getDialogPane().setContent(content);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

            // For Vet role, allow canceling an appointment to free the slot
            boolean isVet = "Vet Hospital".equals(currentUser.role);
            if (isVet && !apptLabels.isEmpty()) {
                ButtonType cancelBtn = new ButtonType("Cancel Appointment", ButtonBar.ButtonData.OK_DONE);
                ButtonType closeBtn = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(cancelBtn, closeBtn);
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get() == cancelBtn) {
                    ChoiceDialog<String> chooser = new ChoiceDialog<>(apptLabels.get(0), apptLabels);
                    chooser.setTitle("Cancel Appointment");
                    chooser.setHeaderText("Select an appointment to cancel");
                    chooser.setContentText("Appointment:");
                    Optional<String> pick = chooser.showAndWait();
                    if (pick.isPresent()) {
                        Map<String,Object> rec = apptByLabel.get(pick.get());
                        if (rec != null) {
                            int apptId = (int) rec.get("appointmentId");
                            int slotId = (int) rec.get("slotId");
                            try (PreparedStatement up = conn.prepareStatement("UPDATE facility_slots SET booked = 0 WHERE id = ?")) {
                                up.setInt(1, slotId); up.executeUpdate();
                            }
                            try (PreparedStatement del = conn.prepareStatement("DELETE FROM appointments WHERE id = ?")) {
                                del.setInt(1, apptId); del.executeUpdate();
                            }
                            new Alert(Alert.AlertType.INFORMATION, "Appointment #"+apptId+" canceled.").showAndWait();
                            showNotification("Canceled appointment #"+apptId, "success");
                            // Refresh list
                            handleViewFacilityAppointments();
                            return;
                        }
                    }
                }
            } else {
                alert.showAndWait();
            }
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, "Error fetching appointments: " + e.getMessage()).showAndWait(); }
    }

    // Vet Hospital or Shelter: book a shelter room using an available date
    @FXML private void handleBookShelterRoom() {
        if (currentUser == null) return;
        boolean isVet = "Vet Hospital".equals(currentUser.role);
        boolean isShelter = "Shelter".equals(currentUser.role);
        if (!isVet && !isShelter) {
            new Alert(Alert.AlertType.INFORMATION, "Only Vet Hospitals or Shelters can book shelter rooms.").showAndWait();
            return;
        }
        try {
            Connection conn = backendController.getConnection();
            Integer shelterId = null;
            String shelterName = null;
            if (isShelter) {
                // Shelters: show existing bookings (who, room, when) for their own facility
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, name FROM facilities WHERE type = 'Shelter' AND adminAgent = ? LIMIT 1")) {
                    ps.setString(1, currentUser.loginId);
                    try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { shelterId = rs.getInt(1); shelterName = rs.getString(2); } }
                }
                if (shelterId == null) { new Alert(Alert.AlertType.INFORMATION, "No shelter facility found for your account.").showAndWait(); return; }

                // Build list view text and a selectable list for optional cancellation
                StringBuilder sb = new StringBuilder();
                String listSql = "SELECT rb.id as bookingId, rb.date, rb.bookedBy, rb.petId, rb.roomId, r.name as roomName, " +
                                 "coalesce(u.firstName,'') as firstName, coalesce(u.lastName,'') as lastName, coalesce(u.role,'') as role, " +
                                 "coalesce(p.name,'') as petName " +
                                 "FROM room_bookings rb JOIN rooms r ON rb.roomId = r.id " +
                                 "LEFT JOIN users u ON rb.bookedBy = u.loginId " +
                                 "LEFT JOIN pets p ON rb.petId = p.id " +
                                 "WHERE r.facilityId = ? ORDER BY rb.date DESC";

                List<String> labels = new ArrayList<>();
                Map<String, Map<String,Object>> byLabel = new LinkedHashMap<>();

                try (PreparedStatement ps = conn.prepareStatement(listSql)) {
                    ps.setInt(1, shelterId);
                    try (ResultSet rs = ps.executeQuery()) {
                        int c = 0;
                        while (rs.next()) {
                            c++;
                            int bookingId = rs.getInt("bookingId");
                            int roomId = rs.getInt("roomId");
                            String date = rs.getString("date");
                            String who = (rs.getString("firstName") + " " + rs.getString("lastName")).trim();
                            if (who.isBlank()) who = rs.getString("bookedBy");
                            String role = rs.getString("role");
                            String roomName = java.util.Optional.ofNullable(rs.getString("roomName")).orElse("?");
                            String petName = java.util.Optional.ofNullable(rs.getString("petName")).orElse("");

                            // Text output
                            sb.append("[#").append(bookingId).append("] [").append(date).append("] room='")
                              .append(roomName).append("' — booked by ").append(who);
                            if (role != null && !role.isBlank()) sb.append(" [").append(role).append("]");
                            if (!petName.isBlank()) sb.append(", pet='").append(petName).append("'");
                            sb.append("\n");

                            // Selectable label
                            String label = "[#" + bookingId + "] " + date + " — room='" + roomName + "'";
                            labels.add(label);
                            Map<String,Object> rec = new HashMap<>();
                            rec.put("bookingId", bookingId);
                            rec.put("roomId", roomId);
                            rec.put("date", date);
                            byLabel.put(label, rec);
                        }
                        if (c == 0) sb.append("No room bookings yet.");
                    }
                }

                // Show list with optional cancel button
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Shelter Room Bookings");
                alert.setHeaderText("Bookings for " + shelterName);
                TextArea content = new TextArea(sb.toString());
                content.setEditable(false);
                content.setWrapText(true);
                content.setPrefWidth(720);
                content.setPrefHeight(420);
                alert.getDialogPane().setContent(content);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                if (!labels.isEmpty()) {
                    ButtonType cancelBtn = new ButtonType("Cancel Booking", ButtonBar.ButtonData.OK_DONE);
                    ButtonType closeBtn = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(cancelBtn, closeBtn);
                    Optional<ButtonType> res = alert.showAndWait();
                    if (res.isPresent() && res.get() == cancelBtn) {
                        ChoiceDialog<String> chooser = new ChoiceDialog<>(labels.get(0), labels);
                        chooser.setTitle("Cancel Shelter Booking");
                        chooser.setHeaderText("Select a booking to cancel");
                        chooser.setContentText("Booking:");
                        Optional<String> pick = chooser.showAndWait();
                        if (pick.isPresent()) {
                            Map<String,Object> rec = byLabel.get(pick.get());
                            if (rec != null) {
                                int bookingId = (int) rec.get("bookingId");
                                int roomId = (int) rec.get("roomId");
                                String date = (String) rec.get("date");
                                // Restore availability (if row exists) and delete booking
                                try (PreparedStatement up = conn.prepareStatement("UPDATE room_availability SET available = 1 WHERE roomId = ? AND date = ?")) {
                                    up.setInt(1, roomId); up.setString(2, date); up.executeUpdate();
                                }
                                try (PreparedStatement del = conn.prepareStatement("DELETE FROM room_bookings WHERE id = ?")) {
                                    del.setInt(1, bookingId); del.executeUpdate();
                                }
                                new Alert(Alert.AlertType.INFORMATION, "Booking #"+bookingId+" canceled.").showAndWait();
                                showNotification("Canceled room booking #"+bookingId, "success");
                                // Refresh list quickly
                                handleBookShelterRoom();
                                return;
                            }
                        }
                    }
                } else {
                    alert.showAndWait();
                }
                return;
            } else {
                // Vets: pick any shelter (first one) for booking
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, name FROM facilities WHERE type = 'Shelter' LIMIT 1"); ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) { shelterId = rs.getInt(1); shelterName = rs.getString(2); }
                }
                if (shelterId == null) { new Alert(Alert.AlertType.INFORMATION, "No Shelter facility found to book a room.").showAndWait(); return; }
            }
            // Pick any room in shelter
            Integer roomId = null; String roomName = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, name FROM rooms WHERE facilityId = ? LIMIT 1")) {
                ps.setInt(1, shelterId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { roomId = rs.getInt(1); roomName = rs.getString(2); } }
            }
            if (roomId == null) { new Alert(Alert.AlertType.INFORMATION, "No rooms available in shelter '" + (shelterName==null?"":shelterName) + "'. Try 'Seed Rooms/Dates'.").showAndWait(); return; }
            // Find a date with availability
            Integer availId = null; String date = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, date FROM room_availability WHERE roomId = ? AND available = 1 ORDER BY date LIMIT 1")) {
                ps.setInt(1, roomId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { availId = rs.getInt(1); date = rs.getString(2); } }
            }
            if (availId == null) { new Alert(Alert.AlertType.INFORMATION, "No available dates for room '" + roomName + "'. Try 'Seed Rooms/Dates'.").showAndWait(); return; }
            // Book it under the acting user (bookedBy = currentUser.loginId), associate a demo pet
            int petId;
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO pets (name, breed, age, ownerUsername) VALUES ('AdoptionDemo','Mixed',1,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, currentUser.loginId); ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); petId = rs.getInt(1); }
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO room_bookings (roomId, date, bookedBy, petId) VALUES (?, ?, ?, ?); UPDATE room_availability SET available = 0 WHERE id = ?")) {
                // SQLite doesn't support multi-statements by default; split into two
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO room_bookings (roomId, date, bookedBy, petId) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, roomId); ps.setString(2, date); ps.setString(3, currentUser.loginId); ps.setInt(4, petId); ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE room_availability SET available = 0 WHERE id = ?")) {
                ps.setInt(1, availId); ps.executeUpdate();
            }
            new Alert(Alert.AlertType.INFORMATION, (isShelter?"Shelter":"Vet") + " booked room '" + roomName + "' on " + date + ".").showAndWait();
            showNotification("Shelter room booked on " + date, "success");
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, "Room booking error: " + e.getMessage()).showAndWait(); }
    }

    // Vet/Shelter: quickly seed a shelter room and availability for demo/testing
    @FXML private void handleSeedShelterRooms() {
        if (currentUser == null) return;
        boolean isVetOrShelter = "Vet Hospital".equals(currentUser.role) || "Shelter".equals(currentUser.role);
        if (!isVetOrShelter) { new Alert(Alert.AlertType.INFORMATION, "Only Vet/Shelter can seed data.").showAndWait(); return; }
        try {
            Connection conn = backendController.getConnection();
            // Ensure there's at least one shelter
            Integer shelterId = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM facilities WHERE type = 'Shelter' LIMIT 1"); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) shelterId = rs.getInt(1);
            }
            if (shelterId == null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO facilities (name, type, phoneNumber, street, city, state, zip, email, businessHours, licenseNumber, emergencyContact, websiteUrl, adminAgent) " +
                        "VALUES ('Demo Shelter','Shelter','555-0000','1 Main','Town','ST','00000','demo@shelter.org','','','','','')",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) shelterId = rs.getInt(1); }
                }
            }
            if (shelterId == null) { new Alert(Alert.AlertType.ERROR, "Failed to create/find a Shelter.").showAndWait(); return; }
            // Ensure a room exists
            Integer roomId = null; String roomName = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, name FROM rooms WHERE facilityId = ? LIMIT 1")) {
                ps.setInt(1, shelterId); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { roomId = rs.getInt(1); roomName = rs.getString(2);} }
            }
            if (roomId == null) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO rooms (facilityId, name, size) VALUES (?, 'Adoption Room A', 'M')", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, shelterId); ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) { roomId = rs.getInt(1); roomName = "Adoption Room A"; } }
                }
            }
            // Seed a couple of available dates
            java.time.LocalDate today = java.time.LocalDate.now();
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO room_availability (roomId, date, available) VALUES (?, ?, 1)")) {
                ps.setInt(1, roomId); ps.setString(2, today.plusDays(1).toString()); ps.executeUpdate();
                ps.setInt(1, roomId); ps.setString(2, today.plusDays(2).toString()); ps.executeUpdate();
            }
            new Alert(Alert.AlertType.INFORMATION, "Seeded room '" + roomName + "' with availability.").showAndWait();
            showNotification("Seeded room availability", "info");
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, "Seeding error: " + e.getMessage()).showAndWait(); }
    }

    // Seed a demo owner and pet so facilities can show a consistent sample
    @FXML private void handleSeedDemoPet() {
        if (currentUser == null) return;
        try {
            Connection conn = backendController.getConnection();
            // Ensure demo owner exists
            String ownerLogin = "demo.owner";
            if (backendController.getUserByLoginId(ownerLogin) == null) {
                String res = backendController.register(
                    "Owner",
                    ownerLogin,
                    "Demo",
                    "Owner",
                    "",
                    "123 Oak St",
                    "Springfield",
                    "ST",
                    "00001",
                    "555-1111",
                    "demo.owner@example.com",
                    "pass"
                );
                if (!"success".equals(res)) { new Alert(Alert.AlertType.ERROR, "Failed to create demo owner: " + res).showAndWait(); return; }
            }
            // Create a demo pet if not exists
            Integer petId = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM pets WHERE ownerUsername = ? AND name = 'Buddy' LIMIT 1")) {
                ps.setString(1, ownerLogin); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) petId = rs.getInt(1); }
            }
            if (petId == null) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO pets (name, breed, age, diet, medicalHistory, ownerUsername) VALUES ('Buddy','Labrador',4,'Grain-free kibble','Up to date on vaccines',?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, ownerLogin); ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) petId = rs.getInt(1); }
                }
                // Add a medication example
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO pet_medications (petId, name, dosage, frequency) VALUES (?, 'Heartgard', '25mg', 'Monthly')")) {
                    ps.setInt(1, petId); ps.executeUpdate();
                }
                // Add a journal entry example
                String today = java.time.LocalDate.now().toString();
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO journal_entries (petId, date, note, ownerUsername, title, visibility, createdAt) VALUES (?, ?, 'Routine checkup noted.','demo.owner','Wellness','private', ?)")) {
                    ps.setInt(1, petId); ps.setString(2, today); ps.setString(3, java.time.LocalDateTime.now().toString()); ps.executeUpdate();
                }
            }
            new Alert(Alert.AlertType.INFORMATION, "Demo pet 'Buddy' is ready (owner=demo.owner).").showAndWait();
            showNotification("Demo pet seeded", "info");
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, "Seeding demo pet error: " + e.getMessage()).showAndWait(); }
    }

    // Show the demo pet details
    @FXML private void handleShowDemoPet() {
        try {
            Connection conn = backendController.getConnection();
            String ownerLogin = "demo.owner";
            Integer petId = null; String name = null; String breed = null; Integer age = null; String diet = null; String history = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, name, breed, age, diet, medicalHistory FROM pets WHERE ownerUsername = ? AND name = 'Buddy' LIMIT 1")) {
                ps.setString(1, ownerLogin); try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) { petId = rs.getInt("id"); name = rs.getString("name"); breed = rs.getString("breed"); age = rs.getInt("age"); diet = rs.getString("diet"); history = rs.getString("medicalHistory"); }
                }
            }
            if (petId == null) { new Alert(Alert.AlertType.INFORMATION, "Demo pet not found. Click 'Seed Demo Pet' first.").showAndWait(); return; }
            StringBuilder sb = new StringBuilder();
            sb.append("Pet #").append(petId).append(" — ").append(name).append(" (" ).append(breed).append(")\n");
            sb.append("Age: ").append(age).append("\n");
            sb.append("Diet: ").append(Optional.ofNullable(diet).orElse("" )).append("\n");
            sb.append("Medical History: ").append(Optional.ofNullable(history).orElse("" )).append("\n\n");
            sb.append("Medications:\n");
            try (PreparedStatement ps = conn.prepareStatement("SELECT name, dosage, frequency FROM pet_medications WHERE petId = ?")) {
                ps.setInt(1, petId); try (ResultSet rs = ps.executeQuery()) {
                    int c = 0; while (rs.next()) { c++; sb.append(" - ").append(rs.getString("name")).append(" (" ).append(rs.getString("dosage")).append(", ").append(rs.getString("frequency")).append(")\n"); }
                    if (c == 0) sb.append(" (none)\n");
                }
            }
            sb.append("\nJournal Entries:\n");
            try (PreparedStatement ps = conn.prepareStatement("SELECT date, title, note FROM journal_entries WHERE petId = ? ORDER BY date DESC LIMIT 3")) {
                ps.setInt(1, petId); try (ResultSet rs = ps.executeQuery()) {
                    int c = 0; while (rs.next()) { c++; sb.append(" - ").append(rs.getString("date")).append(" — ").append(Optional.ofNullable(rs.getString("title")).orElse("" )).append(": ").append(rs.getString("note")).append("\n"); }
                    if (c == 0) sb.append(" (none)\n");
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Demo Pet Information");
            alert.setHeaderText(null);
            alert.setContentText(sb.toString());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            showNotification("Displayed demo pet info", "info");
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, "Show demo pet error: " + e.getMessage()).showAndWait(); }
    }

    // Admin actions
    @FXML private void handleAdminSaveFacility() {
        if (currentUser == null) return;
        try {
            Connection conn = backendController.getConnection();
            Integer facId = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM facilities WHERE adminAgent = ? LIMIT 1")) {
                ps.setString(1, currentUser.loginId); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) facId = rs.getInt(1); }
            }
            if (facId == null) { new Alert(Alert.AlertType.INFORMATION, "No facility found.").showAndWait(); return; }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE facilities SET name=?, phoneNumber=?, street=?, city=?, state=?, zip=?, email=?, businessHours=?, websiteUrl=? WHERE id=?")) {
                ps.setString(1, textOrEmpty(adminFacilityName));
                ps.setString(2, textOrEmpty(adminPhone));
                ps.setString(3, textOrEmpty(adminStreet));
                ps.setString(4, textOrEmpty(adminCity));
                ps.setString(5, textOrEmpty(adminState));
                ps.setString(6, textOrEmpty(adminZip));
                ps.setString(7, textOrEmpty(adminEmail));
                ps.setString(8, textOrEmpty(adminHours));
                ps.setString(9, textOrEmpty(adminWebsite));
                ps.setInt(10, facId);
                ps.executeUpdate();
            }
            new Alert(Alert.AlertType.INFORMATION, "Facility saved.").showAndWait();
            showNotification("Facility saved", "success");
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); }
    }

    @FXML private void handleAdminAddCustomSlot() {
        if (currentUser == null) return;
        String slot = adminCustomSlot != null ? adminCustomSlot.getText() : null;
        if (slot == null || slot.isBlank()) { new Alert(Alert.AlertType.INFORMATION, "Enter a slot (YYYY-MM-DDTHH:MM)").showAndWait(); return; }
        try {
            Connection conn = backendController.getConnection();
            Integer facId = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM facilities WHERE adminAgent = ? LIMIT 1")) {
                ps.setString(1, currentUser.loginId); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) facId = rs.getInt(1); }
            }
            if (facId == null) { new Alert(Alert.AlertType.INFORMATION, "No facility found.").showAndWait(); return; }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO facility_slots (facilityId, slot, booked) VALUES (?, ?, 0)")) {
                ps.setInt(1, facId); ps.setString(2, slot); ps.executeUpdate();
            }
            new Alert(Alert.AlertType.INFORMATION, "Added slot.").showAndWait();
            showNotification("Added facility slot", "success");
        } catch (SQLException e) { new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); }
    }

    private String textOrEmpty(TextField tf) { return tf == null ? "" : (tf.getText() == null ? "" : tf.getText()); }

    @FXML private void handleLookupOwnerAndPets() {
        if (currentUser == null) return;
        boolean isVetOrShelter = "Vet Hospital".equals(currentUser.role) || "Shelter".equals(currentUser.role);
        if (!isVetOrShelter) { new Alert(Alert.AlertType.INFORMATION, "Only Vet/Shelter can perform lookups.").showAndWait(); return; }
        String ownerLogin = lookupOwnerField != null ? lookupOwnerField.getText().trim() : "";
        if (ownerLogin.isEmpty()) { new Alert(Alert.AlertType.INFORMATION, "Enter an owner username.").showAndWait(); return; }
        try {
            Connection conn = backendController.getConnection();
            // Get owner
            StringBuilder sb = new StringBuilder();
            try (PreparedStatement ps = conn.prepareStatement("SELECT loginId, firstName, lastName, email, phoneNumber, street, city, state, zip FROM users WHERE loginId = ? AND role = 'Owner'")) {
                ps.setString(1, ownerLogin);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        sb.append("Owner: ").append(rs.getString("firstName")).append(" ").append(rs.getString("lastName")).append(" (" ).append(rs.getString("loginId")).append(")\n");
                        sb.append("Email: ").append(rs.getString("email")).append(" | Phone: ").append(rs.getString("phoneNumber")).append("\n");
                        sb.append("Address: ").append(rs.getString("street")).append(", ").append(rs.getString("city")).append(", ").append(rs.getString("state")).append(" ").append(rs.getString("zip")).append("\n\n");
                    } else {
                        if (lookupResults != null) lookupResults.setText("No owner found with username '" + ownerLogin + "'.");
                        return;
                    }
                }
            }
            // Get pets
            sb.append("Pets:\n");
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, name, breed, age, coalesce(medicalHistory,'') as medicalHistory FROM pets WHERE ownerUsername = ? ORDER BY name")) {
                ps.setString(1, ownerLogin);
                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        sb.append(" - ").append(rs.getString("name")).append(" (#").append(rs.getInt("id")).append(") ");
                        sb.append(rs.getString("breed")).append(", age ").append(rs.getInt("age")).append("\n");
                        String mh = rs.getString("medicalHistory");
                        if (mh != null && !mh.isBlank()) sb.append("   history: ").append(mh).append("\n");
                    }
                    if (count == 0) sb.append(" (no pets on file)\n");
                }
            }
            if (lookupResults != null) lookupResults.setText(sb.toString());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Lookup error: " + e.getMessage()).showAndWait();
        }
    }

    @FXML private void submitRegister() {
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
            regRoleBox.getValue(),
            regUsernameField.getText(),
            regFirstNameField.getText(),
            regLastNameField.getText(),
            "",
            regStreetField.getText(),
            regCityField.getText(),
            regStateField.getText(),
            regZipField.getText(),
            regPhoneField.getText(),
            regEmailField.getText(),
            regPasswordField.getText()
        );
        if ("success".equals(result)) {
            System.out.println("Registration successful! Please log in.");
            showNotification("Registration successful! Please log in.", "success");
            clearRegisterFields();
        } else {
            System.out.println(result);
            showNotification(result, "error");
        }
    }

    @FXML private void submitForgotPassword() {
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
            showNotification("Password reset successful!", "success");
        } else {
            forgotMessageLabel.setText(result);
            showNotification(result, "error");
        }
    }

    @FXML private void handleShowPetHistory() {
        if (currentUser == null) return;
        String petIdText = petHistoryPetIdField != null ? petHistoryPetIdField.getText().trim() : "";
        if (petIdText.isEmpty()) { new Alert(Alert.AlertType.INFORMATION, "Enter a Pet ID.").showAndWait(); return; }
        int petId;
        try { petId = Integer.parseInt(petIdText); } catch (NumberFormatException nfe) { new Alert(Alert.AlertType.INFORMATION, "Pet ID must be a number.").showAndWait(); return; }
        try {
            var conn = backendController.getConnection();
            StringBuilder sb = new StringBuilder();
            // Pet basics
            try (var ps = conn.prepareStatement("SELECT p.id, p.name, p.breed, p.age, p.diet, p.medicalHistory, p.ownerUsername, u.firstName, u.lastName FROM pets p JOIN users u ON p.ownerUsername = u.loginId WHERE p.id = ?")) {
                ps.setInt(1, petId);
                try (var rs = ps.executeQuery()) {
                    if (!rs.next()) { if (petHistoryResults!=null) petHistoryResults.setText("No pet found for ID "+petId); return; }
                    sb.append("Pet #").append(rs.getInt("id")).append(" — ")
                      .append(rs.getString("name")).append(" (")
                      .append(rs.getString("breed")).append(")\n")
                      .append("Age: ").append(rs.getInt("age")).append("\n")
                      .append("Owner: ").append(rs.getString("firstName")).append(" ")
                      .append(rs.getString("lastName")).append(" (")
                      .append(rs.getString("ownerUsername")).append(")\n")
                      .append("Diet: ").append(java.util.Optional.ofNullable(rs.getString("diet")).orElse("")).append("\n")
                      .append("Medical History: ").append(java.util.Optional.ofNullable(rs.getString("medicalHistory")).orElse("")).append("\n\n");
                }
            }
            // Medications
            sb.append("Medications:\n");
            try (var ps = conn.prepareStatement("SELECT name, dosage, frequency FROM pet_medications WHERE petId = ?")) {
                ps.setInt(1, petId);
                try (var rs = ps.executeQuery()) {
                    int c=0; while (rs.next()) { c++; sb.append(" - ").append(rs.getString("name")).append(" (")
                        .append(java.util.Optional.ofNullable(rs.getString("dosage")).orElse(""))
                        .append(", ")
                        .append(java.util.Optional.ofNullable(rs.getString("frequency")).orElse(""))
                        .append(")\n"); }
                    if (c==0) sb.append(" (none)\n");
                }
            }
            // Journal entries
            sb.append("\nJournal Entries:\n");
            try (var ps = conn.prepareStatement("SELECT date, title, note FROM journal_entries WHERE petId = ? ORDER BY date DESC LIMIT 10")) {
                ps.setInt(1, petId);
                try (var rs = ps.executeQuery()) {
                    int c=0; while (rs.next()) { c++; sb.append(" - ").append(rs.getString("date")).append(" — ")
                        .append(java.util.Optional.ofNullable(rs.getString("title")).orElse(""))
                        .append(": ")
                        .append(rs.getString("note")).append("\n"); }
                    if (c==0) sb.append(" (none)\n");
                }
            }
            if (petHistoryResults != null) petHistoryResults.setText(sb.toString());
        } catch (Exception e) { new Alert(Alert.AlertType.ERROR, "History error: " + e.getMessage()).showAndWait(); }
    }

    @FXML private void handleCopyPetHistoryId() {
        String txt = petHistoryPetIdField != null ? petHistoryPetIdField.getText() : null;
        if (txt == null || txt.isBlank()) {
            showNotification("Enter a Pet ID first", "error");
            return;
        }
        try {
            Integer.parseInt(txt.trim()); // validate it's a number
            StringSelection sel = new StringSelection(txt.trim());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
            showNotification("Copied Pet ID #" + txt.trim() + " to clipboard", "success");
        } catch (NumberFormatException nfe) {
            showNotification("Pet ID must be a number", "error");
        }
    }

    @FXML private void handleEmergencyDispatch() {
        if (currentUser == null) return;
        boolean isVetOrShelter = "Vet Hospital".equals(currentUser.role) || "Shelter".equals(currentUser.role);
        if (!isVetOrShelter) { new Alert(Alert.AlertType.INFORMATION, "Only Vet/Shelter can dispatch.").showAndWait(); return; }
        String petName = emergencyPetNameField != null ? emergencyPetNameField.getText().trim() : "";
        try {
            var conn = backendController.getConnection();
            // 1) Intake: create a temporary emergency pet linked to dispatcher
            int petId;
            try (var ps = conn.prepareStatement("INSERT INTO pets (name, breed, age, ownerUsername, medicalHistory) VALUES (?, 'Unknown', 0, ?, 'Emergency intake')", java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, petName.isEmpty()?"EmergencyCase":petName);
                ps.setString(2, currentUser.loginId);
                ps.executeUpdate();
                try (var rs = ps.getGeneratedKeys()) { rs.next(); petId = rs.getInt(1); }
            }
            // 2) Route to nearest Vet Hospital: pick first available vet facility
            Integer vetId = null; String vetName = null;
            try (var ps = conn.prepareStatement("SELECT id, name FROM facilities WHERE type='Vet Hospital' ORDER BY id LIMIT 1"); var rs = ps.executeQuery()) {
                if (rs.next()) { vetId = rs.getInt(1); vetName = rs.getString(2); }
            }
            if (vetId == null) { if (emergencyOutput!=null) emergencyOutput.setText("No Vet Hospital available."); return; }
            // 3) Assign earliest open slot today/tomorrow
            Integer slotId = null; String slotStr = null;
            try (var ps = conn.prepareStatement("SELECT id, slot FROM facility_slots WHERE facilityId = ? AND booked = 0 ORDER BY slot LIMIT 1")) {
                ps.setInt(1, vetId); try (var rs = ps.executeQuery()) {
                    if (rs.next()) { slotId = rs.getInt(1); slotStr = rs.getString(2);} }
            }
            if (slotId == null) {
                // Create an emergency slot 1 hour from now
                String emergSlot = java.time.LocalDateTime.now().plusHours(1).withSecond(0).withNano(0).toString();
                try (var ps = conn.prepareStatement("INSERT INTO facility_slots (facilityId, slot, booked) VALUES (?, ?, 0)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, vetId); ps.setString(2, emergSlot); ps.executeUpdate();
                    try (var rs = ps.getGeneratedKeys()) { rs.next(); slotId = rs.getInt(1); slotStr = emergSlot; }
                }
            }
            // 4) Create appointment marked as EMERGENCY
            try (var ps = conn.prepareStatement("INSERT INTO appointments (petId, facilityId, slotId, ownerUsername, description) VALUES (?, ?, ?, ?, 'EMERGENCY DISPATCH')")) {
                ps.setInt(1, petId); ps.setInt(2, vetId); ps.setInt(3, slotId); ps.setString(4, currentUser.loginId); ps.executeUpdate();
            }
            try (var ps = conn.prepareStatement("UPDATE facility_slots SET booked = 1 WHERE id = ?")) { ps.setInt(1, slotId); ps.executeUpdate(); }
            String msg = "Dispatched emergency to '"+vetName+"' at "+slotStr+" (pet #"+petId+")";
            if (emergencyOutput != null) emergencyOutput.setText(msg);
            new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
            showNotification("Emergency dispatched", "success");
        } catch (Exception e) { new Alert(Alert.AlertType.ERROR, "Dispatch error: "+e.getMessage()).showAndWait(); showNotification("Dispatch error: "+e.getMessage(), "error"); }
    }

    // (duplicate helper methods were removed here)

    // ---- Helpers restored ----
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
        if (userStep1Box != null) { userStep1Box.setVisible(true); userStep1Box.setManaged(true); }
        if (userStep2Box != null) { userStep2Box.setVisible(false); userStep2Box.setManaged(false); }
    }

    public String addAppointment(int petId, int facilityId, int slotId, String description, String ownerUsername) {
        try {
            Connection conn = backendController.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO appointments (petId, facilityId, slotId, ownerUsername, description) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, petId); ps.setInt(2, facilityId); ps.setInt(3, slotId); ps.setString(4, ownerUsername); ps.setString(5, description);
            ps.executeUpdate(); ps.close();
            ps = conn.prepareStatement("UPDATE facility_slots SET booked = 1 WHERE id = ?"); ps.setInt(1, slotId); ps.executeUpdate(); ps.close();
            return "success";
        } catch (SQLException e) { return "Error adding appointment: " + e.getMessage(); }
    }

    public List<Map<String, Object>> getAppointmentsForUser(String username) {
        List<Map<String, Object>> appts = new ArrayList<>();
        try {
            Connection conn = backendController.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT a.id, a.petId, a.facilityId, f.type as facilityType, s.slot, a.description FROM appointments a JOIN facility_slots s ON a.slotId = s.id JOIN facilities f ON a.facilityId = f.id WHERE a.ownerUsername = ?"
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
            rs.close(); ps.close();
        } catch (SQLException e) {}
        return appts;
    }

    // Toast-style notification method
    public void showNotification(String message, String type) {
        Platform.runLater(() -> {
            if (notificationOverlay == null || notificationLabel == null) return;
            // Style by type
            String bg;
            switch (type == null ? "info" : type.toLowerCase()) {
                case "error": bg = "-fx-background-color: rgba(200,30,30,0.95);"; break;
                case "success": bg = "-fx-background-color: rgba(30,150,60,0.95);"; break;
                default: bg = "-fx-background-color: rgba(40,40,40,0.95);";
            }
            notificationOverlay.setStyle(bg + " -fx-background-radius: 6; -fx-padding: 8 12 8 12;");
            notificationLabel.setText(message);
            notificationOverlay.setVisible(true);
            notificationOverlay.setManaged(true);

            if (notificationAnimation != null) {
                notificationAnimation.stop();
            }
            FadeTransition fadeIn = new FadeTransition(Duration.millis(180), notificationOverlay);
            fadeIn.setFromValue(Math.max(0.0, notificationOverlay.getOpacity()));
            fadeIn.setToValue(1.0);

            PauseTransition stay = new PauseTransition(Duration.seconds(2.4));

            FadeTransition fadeOut = new FadeTransition(Duration.millis(260), notificationOverlay);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> {
                notificationOverlay.setVisible(false);
                notificationOverlay.setManaged(false);
            });

            notificationAnimation = new SequentialTransition(fadeIn, stay, fadeOut);
            notificationAnimation.play();
        });
    }
}