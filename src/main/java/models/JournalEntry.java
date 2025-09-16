package models;

public class JournalEntry {
    public int id;
    public int petId;
    public String date;
    public String note;
    public String ownerUsername;
    public String title;
    public String visibility;
    public String createdAt;

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

    @Override
    public String toString() {
        return id + ". [" + date + "] (" + visibility + ") " + (title != null && !title.isEmpty() ? title + ": " : "") + note;
    }
}
