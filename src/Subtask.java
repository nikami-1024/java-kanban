public class Subtask extends Task {
    protected int epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Эпик ID-" + epicId + " -> ID-" + id + " -- " + status + " -- " + title + ": " + description;
    }
}