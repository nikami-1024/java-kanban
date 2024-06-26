package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;
    protected HistoryManager imhm;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        imhm = Managers.getDefaultHistory();
        System.out.println("\nДобро пожаловать в InMemory таск менеджер!");
    }

    // создание таски
    @Override
    public Task createTask(String title, String description) {
        Task task = new Task(title, description);
        tasks.put(task.getId(), task);
        System.out.println("\nСоздана новая таска:");
        System.out.println("ID-" + task.getId() + " -- " + task.getTitle());
        return task;
    }

    // создание эпика
    @Override
    public Epic createEpic(String title, String description) {
        Epic epic = new Epic(title, description);
        epics.put(epic.getId(), epic);
        System.out.println("\nСоздан новый эпик:");
        System.out.println("ID-" + epic.getId() + " -- " + epic.getTitle());
        return epic;
    }

    // создание сабтаски
    @Override
    public Subtask createSubtask(String title, String description, int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
        Subtask subtask = new Subtask(title, description, epicId);
        int subId = subtask.getId();

        subtasks.put(subId, subtask);
        subtasksIds.add(subId);
        epic.setSubtasksIds(subtasksIds);

        System.out.println("\nДобавлена новая сабтаска в эпик ID-" + epicId + ":");
        System.out.println("ID-" + subId + " -- " + title);
        calculateEpicStatus(epicId);
        return subtask;
    }

    // обновление заголовка таски
    @Override
    public void updateTaskTitle(int taskId, String title) {
        Task task = tasks.get(taskId);
        task.setTitle(title);
        tasks.put(taskId, task);
        System.out.println("\nТаска ID-" + taskId + " обновлена");
    }

    // обновление описания таски
    @Override
    public void updateTaskDescription(int taskId, String description) {
        Task task = tasks.get(taskId);
        task.setDescription(description);
        tasks.put(taskId, task);
        System.out.println("\nТаска ID-" + taskId + " обновлена");
    }

    // обновление статуса таски
    @Override
    public void updateTaskStatus(int taskId, Status status) {
        Task task = tasks.get(taskId);
        task.setStatus(status);
        tasks.put(taskId, task);
        System.out.println("\nТаска ID-" + taskId + " обновлена");
    }

    // обновление заголовка эпика
    @Override
    public void updateEpicTitle(int epicId, String title) {
        Epic epic = epics.get(epicId);
        epic.setTitle(title);
        epics.put(epicId, epic);
        System.out.println("\nЭпик ID-" + epicId + " обновлён");
    }

    // обновление описания эпика
    @Override
    public void updateEpicDescription(int epicId, String description) {
        Epic epic = epics.get(epicId);
        epic.setDescription(description);
        epics.put(epicId, epic);
        System.out.println("\nЭпик ID-" + epicId + " обновлён");
    }

    // расчёт статуса эпика на основе статусов сабтасок
    private void calculateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
        Status oldStatus = epic.getStatus();
        Status newStatus;

        if (subtasksIds.size() == 0) {
            newStatus = Status.NEW;
        } else {
            boolean isExistNew = false;
            boolean isExistDone = false;
            boolean isExistWIP = false;

            for (Integer subId : subtasksIds) {
                Status subStatus = subtasks.get(subId).getStatus();
                if (subStatus == Status.NEW) {
                    isExistNew = true;
                } else if (subStatus == Status.DONE) {
                    isExistDone = true;
                } else if (subStatus == Status.IN_PROGRESS) {
                    isExistWIP = true;
                }
            }

            if (isExistNew && !isExistDone && !isExistWIP) {
                newStatus = Status.NEW;
            } else if (!isExistNew && isExistDone && !isExistWIP) {
                newStatus = Status.DONE;
            } else {
                newStatus = Status.IN_PROGRESS;
            }
        }

        if (oldStatus != newStatus) {
            epic.setStatus(newStatus);
            epics.put(epicId, epic);
        }

        System.out.println("Статус эпика ID-" + epicId + " пересчитан");
    }

    // обновление заголовка сабтаски
    @Override
    public void updateSubtaskTitle(int subId, String title) {
        Subtask subtask = subtasks.get(subId);
        subtask.setTitle(title);
        subtasks.put(subId, subtask);

        System.out.println("\nСабтаска ID-" + subId + " обновлена");
    }

    // обновление описания сабтаски
    @Override
    public void updateSubtaskDescription(int subId, String description) {
        Subtask subtask = subtasks.get(subId);
        subtask.setDescription(description);
        subtasks.put(subId, subtask);

        System.out.println("\nСабтаска ID-" + subId + " обновлена");
    }

    // обновление статуса сабтаски
    @Override
    public void updateSubtaskStatus(int subId, Status status) {
        int epicId = findEpicOfSubtask(subId);
        Subtask subtask = subtasks.get(subId);
        subtask.setStatus(status);
        subtasks.put(subId, subtask);

        System.out.println("\nСабтаска ID-" + subId + " обновлена");
        calculateEpicStatus(epicId);
    }

    // перемещение сабтаски в новый эпик
    // сущность в истории просмотров (если была) не затрагивается никак
    @Override
    public void moveSubtask(int subId, int newEpicId) {
        Subtask subtask = subtasks.get(subId);
        int oldEpicId = findEpicOfSubtask(subId);
        Epic oldEpic = epics.get(oldEpicId);
        Epic newEpic = epics.get(newEpicId);
        ArrayList<Integer> subtasksIds = oldEpic.getSubtasksIds();

        // обновление epicId у сабтаски
        subtask.setEpicId(newEpicId);
        subtasks.put(subId, subtask);

        // удаление subId из старого эпика
        subtasksIds.remove(subtasksIds.indexOf(subId));
        oldEpic.setSubtasksIds(subtasksIds);
        epics.put(oldEpicId, oldEpic);

        // добавление subId в новый эпик
        subtasksIds = newEpic.getSubtasksIds();
        subtasksIds.add(subId);
        newEpic.setSubtasksIds(subtasksIds);
        epics.put(newEpicId, newEpic);

        System.out.println("\nСабтаска ID-" + subId + " перемещена из эпика ID-" +
                oldEpicId + " в эпик ID-" + newEpicId);
        calculateEpicStatus(oldEpicId);
        calculateEpicStatus(newEpicId);
    }

    // удаление таски по id
    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        imhm.remove(taskId);
        System.out.println("\nТаска ID-" + taskId + " удалена");
    }

    // удаление всех-всех тасок
    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            imhm.remove(id);
        }
        tasks.clear();
        System.out.println("\nВсе таски безвозвратно удалены");
    }

    // удаление эпика с сабтасками по id эпика
    @Override
    public void deleteEpic(int epicId) {
        ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        for (Integer subId : subtasksIds) {
            imhm.remove(subId);
            subtasks.remove(subId);
        }
        imhm.remove(epicId);
        epics.remove(epicId);
        System.out.println("\nЭпик ID-" + epicId + " со всеми сабтасками удалён");
    }

    // удаление всех-всех эпиков с сабтасками
    @Override
    public void deleteAllEpics() {
        // удаление всех сабтасок из истории просмотра
        for (Integer subId : subtasks.keySet()) {
            imhm.remove(subId);
        }

        // удаление всех эпиков из истории просмотра
        for (Integer epicId : epics.keySet()) {
            imhm.remove(epicId);
        }

        // очищение мап сабтасок и эпиков
        subtasks.clear();
        epics.clear();
        System.out.println("\nВсе эпики с сабтасками безвозвратно удалены");
    }

    // удаление сабтаски по id
    @Override
    public void deleteSubtaskFromEpic(int subId) {
        int epicId = findEpicOfSubtask(subId);
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();

        subtasks.remove(subId);
        subtasksIds.remove(subtasksIds.indexOf(subId));
        epic.setSubtasksIds(subtasksIds);
        epics.put(epicId, epic);

        imhm.remove(subId);

        System.out.println("\nСабтаска ID-" + subId + " удалена из эпика ID-" + epicId);
        calculateEpicStatus(epicId);
    }

    // удаление всех-всех сабтасок у эпика
    @Override
    public void deleteAllSubtasksFromEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();

        for (Integer subId : subtasksIds) {
            imhm.remove(subId);
            subtasks.remove(subId);
        }

        subtasksIds.clear();
        epic.setSubtasksIds(subtasksIds);
        epics.put(epicId, epic);

        System.out.println("\nСабтаски эпика ID-" + epicId + " очищены сожжением");
        calculateEpicStatus(epicId);
    }

    // найти эпик по id сабтаски
    protected int findEpicOfSubtask(int subId) {
        int epicId = -1;

        for (Integer epicIndx : epics.keySet()) {
            Epic epic = epics.get(epicIndx);
            ArrayList<Integer> subt = epic.getSubtasksIds();
            for (Integer subIndx : subt) {
                epicId = subId == subIndx ? epicIndx : epicId;
            }
        }

        return epicId;
    }

    // возврат таски по id
    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        imhm.addToHistory(task);
        return tasks.get(taskId);
    }

    // возврат эпика по id
    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        imhm.addToHistory(epic);
        return epics.get(epicId);
    }

    // возврат сабтаски по id
    @Override
    public Subtask getSubtaskById(int subId) {
        Subtask subtask = subtasks.get(subId);
        imhm.addToHistory(subtask);
        return subtasks.get(subId);
    }

    // возврат сущности по id
    @Override
    public Task getAnyTaskById(int id) {
        Task task;

        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        } else if (epics.containsKey(id)) {
            task = epics.get(id);
        } else {
            task = subtasks.get(id);
        }

        imhm.addToHistory(task);
        return task;
    }

    // возврат истории
    @Override
    public LinkedList<Task> getHistory() {
        return imhm.getHistory();
    }

    // возврат количества всех существующих сущностей
    @Override
    public int getEntitiesCounter() {
        return tasks.size() + epics.size() + subtasks.size();
    }
}
