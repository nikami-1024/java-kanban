import java.util.HashMap;

public class Epic extends Task {
    protected HashMap<Integer, Subtask> subtasks;

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        subtasks = new HashMap<>();
    }

    public Epic(int id, String title, String description, Status status, HashMap<Integer, Subtask> subtasks) {
        super(id, title, description, status);
        this.subtasks = subtasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}
