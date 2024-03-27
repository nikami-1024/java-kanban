package model;

import java.time.format.DateTimeFormatter;

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
        return TaskType.SUBTASK + "," + id + "," + status + "," + title + "," + description + ","
                + getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + ","
                + getDuration() + "," + epicId;
    }
}