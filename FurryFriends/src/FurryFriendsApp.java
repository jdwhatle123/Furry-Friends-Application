// FurryFriendsApp.java
// Main application entry point for the Furry Friends JavaFX app.
// Handles database connection, controller setup, and GUI initialization.

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import controller.Controller;

public class FurryFriendsApp extends Application {
    private static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML layout for the main scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Scene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.setFill(Color.TRANSPARENT); // Make scene background transparent
        Object controllerObj = loader.getController();
        try {
            // Inject backend controller into the FXML controller
            java.lang.reflect.Method setBackend = controllerObj.getClass().getMethod("setBackendController", Controller.class);
            setBackend.invoke(controllerObj, controller);
        } catch (Exception e) {
            // Ignore if method not found or fails
        }

        primaryStage.setTitle("Furry Friends"); // Set window title
        primaryStage.setScene(scene); // Set the scene to the stage
        primaryStage.setResizable(false); // Prevent window resizing
        primaryStage.initStyle(javafx.stage.StageStyle.TRANSPARENT); // Transparent window style
        primaryStage.show(); // Show the main window
    }

    public static void main(String[] args) {
        try {
            // Connect to SQLite database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:furryfriends.db");
            controller = new Controller(conn); // Create backend controller
            controller.ensureTablesExist(); // Ensure required tables exist
            launch(args); // Launch JavaFX application
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}