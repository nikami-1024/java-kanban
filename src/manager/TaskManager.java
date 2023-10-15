package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.LinkedList;

public interface TaskManager {

    // создание таски
    public Task createTask(String title, String description);

    // создание эпика
    public Epic createEpic(String title, String description);

    // создание сабтаски
    public Subtask createSubtask(String title, String description, int epicId);

    // обновление заголовка таски
    public void updateTaskTitle(int taskId, String title);

    // обновление описания таски
    public void updateTaskDescription(int taskId, String description);

    // обновление статуса таски
    public void updateTaskStatus(int taskId, Status status);

    // обновление заголовка эпика
    public void updateEpicTitle(int epicId, String title);

    // обновление описания эпика
    public void updateEpicDescription(int epicId, String description);

    // обновление заголовка сабтаски
    public void updateSubtaskTitle(int subId, String title);

    // обновление описания сабтаски
    public void updateSubtaskDescription(int subId, String description);

    // обновление статуса сабтаски
    public void updateSubtaskStatus(int subId, Status status);

    // перемещение сабтаски в новый эпик
    public void moveSubtask(int subId, int newEpicId);

    // удаление таски по ID
    public void deleteTask(int taskId);

    // удаление всех-всех тасок
    public void deleteAllTasks();

    // удаление эпика с сабтасками по ID эпика
    public void deleteEpic(int epicId);

    // удаление всех-всех эпиков с сабтасками
    public void deleteAllEpics();

    // удаление сабтаски по ID
    public void deleteSubtaskFromEpic(int subId);

    // удаление всех-всех сабтасок у эпика
    public void deleteAllSubtasksFromEpic(int epicId);

    // возврат таски по ID
    public Task getTaskById(int taskId);

    // возврат эпика по ID
    public Epic getEpicById(int epicId);

    // возврат сабтаски по ID
    public Subtask getSubtaskById(int subId);

    // возврат истории
    public LinkedList<Task> getHistory();
}