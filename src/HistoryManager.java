import java.util.ArrayList;

public interface HistoryManager {
    // добавление сущности в историю
    public void addToHistory(Task task);

    // получение истории - последние 10 просмотренных объектов
    public ArrayList<Task> getHistory();
}
