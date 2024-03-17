package model;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description);
        subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    @Override
    public String toString() {
        return TaskType.EPIC + "," + id + "," + status + "," + title + "," + description + "," +
                subtasksIds.toString();
    }
}
