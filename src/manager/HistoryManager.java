package manager;

import model.Task;

import java.util.LinkedList;

public interface HistoryManager {
    // добавление сущности в историю
    public void addToHistory(Task task);

    // удаление сущности из истории
    public void remove(int id);

    // получение истории - последние 10 просмотренных объектов
    public LinkedList<Task> getHistory();
}
