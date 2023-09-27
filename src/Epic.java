import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description);
        subtasksIds = new ArrayList<>();
    }

    protected ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }
}
