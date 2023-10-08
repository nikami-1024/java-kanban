import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    protected ArrayList<Task> history = new ArrayList<>();

    // добавление сущности в историю
    @Override
    public void addToHistory(Task task) {
        if (history.size() == 10) {
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }
    }

    // получение истории - последние 10 просмотренных объектов
    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
