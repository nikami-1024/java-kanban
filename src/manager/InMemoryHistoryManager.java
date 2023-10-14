package manager;

import model.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    protected LinkedList<Task> history = new LinkedList<>();

    // добавление сущности в историю
    // свежая добавляется наверх списка
    @Override
    public void addToHistory(Task task) {
        if (history.size() == 10) {
            history.pollLast();
            history.addFirst(task);
        } else {
            history.addFirst(task);
        }
    }

    // получение истории - последние 10 просмотренных объектов
    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }
}
