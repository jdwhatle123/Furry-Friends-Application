// JournalEntry.java
// Represents a journal entry for a pet, including details such as date, note, owner, title, visibility, and timestamps.
package models;

public class JournalEntry {
    // Unique identifier for the journal entry
    public int id;
    // ID of the pet this entry is associated with
    public int petId;
    // Date of the journal entry
    public String date;
    // The note or content of the journal entry
    public String note;
    // Username of the pet's owner
    public String ownerUsername;
    // Title of the journal entry
    public String title;
    // Who can view this entry: "Owner", "Vet", "Shelter", or "All"
    public String visibility; // "Owner", "Vet", "Shelter", "All"
    // Timestamp when the entry was created
    public String createdAt;

    // Constructor to initialize all fields
    public JournalEntry(int id, int petId, String date, String note, String ownerUsername, String title, String visibility, String createdAt) {
        this.id = id;
        this.petId = petId;
        this.date = date;
        this.note = note;
        this.ownerUsername = ownerUsername;
        this.title = title;
        this.visibility = visibility;
        this.createdAt = createdAt;
    }

    // Returns a string representation of the journal entry for display
    @Override
    public String toString() {
        return id + ". [" + date + "] (" + visibility + ") " + (title != null && !title.isEmpty() ? title + ": " : "") + note;
    }
}
