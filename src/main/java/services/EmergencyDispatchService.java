package services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * EmergencyDispatchService simulates triage and dispatch for emergency cases.
 */
public class EmergencyDispatchService {
    private final Random rnd = new Random();

    public boolean authorizeEmergency(String token, String reason) {
        // Demo: authorize all requests that have a non-empty reason
        return reason != null && !reason.isBlank();
    }

    public LocalDateTime dispatchVet(String location) {
        // Demo: ETA 15-30 minutes
        int minutes = 15 + rnd.nextInt(16);
        return LocalDateTime.now().plusMinutes(minutes);
    }
}
