package manager;

import model.Task;

import java.util.LinkedList;

interface HistoryManager {
    // добавление сущности в историю
    void addToHistory(Task task);

    // удаление сущности из истории
    void remove(int id);

    // получение истории - последние 10 просмотренных объектов
    LinkedList<Task> getHistory();
}
