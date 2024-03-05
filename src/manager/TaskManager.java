package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.LinkedList;

public interface TaskManager {

    // создание таски
    Task createTask(String title, String description);

    // создание эпика
    Epic createEpic(String title, String description);

    // создание сабтаски
    Subtask createSubtask(String title, String description, int epicId);

    // обновление заголовка таски
    void updateTaskTitle(int taskId, String title);

    // обновление описания таски
    void updateTaskDescription(int taskId, String description);

    // обновление статуса таски
    void updateTaskStatus(int taskId, Status status);

    // обновление заголовка эпика
    void updateEpicTitle(int epicId, String title);

    // обновление описания эпика
    void updateEpicDescription(int epicId, String description);

    // обновление заголовка сабтаски
    void updateSubtaskTitle(int subId, String title);

    // обновление описания сабтаски
    void updateSubtaskDescription(int subId, String description);

    // обновление статуса сабтаски
    void updateSubtaskStatus(int subId, Status status);

    // перемещение сабтаски в новый эпик
    void moveSubtask(int subId, int newEpicId);

    // удаление таски по ID
    void deleteTask(int taskId);

    // удаление всех-всех тасок
    void deleteAllTasks();

    // удаление эпика с сабтасками по ID эпика
    void deleteEpic(int epicId);

    // удаление всех-всех эпиков с сабтасками
    void deleteAllEpics();

    // удаление сабтаски по ID
    void deleteSubtaskFromEpic(int subId);

    // удаление всех-всех сабтасок у эпика
    void deleteAllSubtasksFromEpic(int epicId);

    // возврат таски по ID
    Task getTaskById(int taskId);

    // возврат эпика по ID
    Epic getEpicById(int epicId);

    // возврат сабтаски по ID
    Subtask getSubtaskById(int subId);

    // возврат истории
    LinkedList<Task> getHistory();

    // возврат количества всех существующих сущностей
    int getEntitiesCounter();
}