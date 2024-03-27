package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksIds;
    protected LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return TaskType.EPIC + "," + id + "," + status + "," + title + "," + description + ","
                + getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + ","
                + getDuration() + "," + subtasksIds.toString();
    }
}
