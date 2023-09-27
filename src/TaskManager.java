import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        System.out.println("Добро пожаловать в таск менеджер!");
    }

    // создание таски
    public void createTask(String title, String description) {
        Task task = new Task(title, description);
        tasks.put(task.getId(), task);
        System.out.println("\nНовая таска создана!");
        System.out.println("ID-" + task.getId() + " -- " + task.getTitle());
    }

    // создание эпика
    public void createEpic(String title, String description) {
        Epic epic = new Epic(title, description);
        epics.put(epic.getId(), epic);
        System.out.println("\nНовый эпик создан!");
        System.out.println("ID-" + epic.getId() + " -- " + epic.getTitle());
    }

    // создание сабтаски
    public void createSubtask(String title, String description, int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
        Subtask subtask = new Subtask(title, description, epicId);
        int subId = subtask.getId();

        subtasks.put(subId, subtask);
        subtasksIds.add(subId);
        epic.setSubtasksIds(subtasksIds);

        System.out.println("\nВ эпик ID-" + epicId + " добавлена новая сабтаска");
        System.out.println("ID-" + subId + " -- " + title);
        calculateEpicStatus(epicId);
    }

    // обновление заголовка таски
    public void updateTaskTitle(int taskId, String title) {
        Task task = tasks.get(taskId);
        task.setTitle(title);
        tasks.put(taskId, task);
        System.out.println("\nТаска ID-" + taskId + " обновлена");
    }

    // обновление описания таски
    public void updateTaskDescription(int taskId, String description) {
        Task task = tasks.get(taskId);
        task.setDescription(description);
        tasks.put(taskId, task);
        System.out.println("\nТаска ID-" + taskId + " обновлена");
    }

    // обновление статуса таски
    public void updateTaskStatus(int taskId, Status status) {
        Task task = tasks.get(taskId);
        task.setStatus(status);
        tasks.put(taskId, task);
        System.out.println("\nТаска ID-" + taskId + " обновлена");
    }

    // обновление заголовка эпика
    public void updateEpicTitle(int epicId, String title) {
        Epic epic = epics.get(epicId);
        epic.setTitle(title);
        epics.put(epicId, epic);
        System.out.println("\nЭпик ID-" + epicId + " обновлён");
    }

    // обновление описания эпика
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
    public void updateSubtaskTitle(int subId, String title) {
        Subtask subtask = subtasks.get(subId);
        subtask.setTitle(title);
        subtasks.put(subId, subtask);

        System.out.println("\nСабтаска ID-" + subId + " обновлена");
    }

    // обновление описания сабтаски
    public void updateSubtaskDescription(int subId, String description) {
        Subtask subtask = subtasks.get(subId);
        subtask.setDescription(description);
        subtasks.put(subId, subtask);

        System.out.println("\nСабтаска ID-" + subId + " обновлена");
    }

    // обновление статуса сабтаски
    public void updateSubtaskStatus(int subId, Status status) {
        int epicId = findEpicOfSubtask(subId);
        Subtask subtask = subtasks.get(subId);
        subtask.setStatus(status);
        subtasks.put(subId, subtask);

        System.out.println("\nСабтаска ID-" + subId + " обновлена");
        calculateEpicStatus(epicId);
    }

    // перемещение сабтаски в новый эпик
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

    // удаление таски по ID
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        System.out.println("\nТаска ID-" + taskId + " удалена");
    }

    // удаление всех-всех тасок
    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("\nВсе таски безвозвратно удалены");
    }

    // удаление эпика с сабтасками по ID эпика
    public void deleteEpic(int epicId) {
        ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        for (Integer subId : subtasksIds) {
            subtasks.remove(subId);
        }
        epics.remove(epicId);
        System.out.println("\nЭпик ID-" + epicId + " со всеми сабтасками удалён");
    }

    // удаление всех-всех эпиков с сабтасками
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
        System.out.println("\nВсе эпики с сабтасками безвозвратно удалены");
    }

    // удаление сабтаски по ID
    public void deleteSubtaskFromEpic(int subId) {
        int epicId = findEpicOfSubtask(subId);
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();

        subtasks.remove(subId);
        subtasksIds.remove(subtasksIds.indexOf(subId));
        epic.setSubtasksIds(subtasksIds);
        epics.put(epicId, epic);

        System.out.println("\nСабтаска ID-" + subId + " удалена из эпика ID-" + epicId);
        calculateEpicStatus(epicId);
    }

    // удаление всех-всех сабтасок у эпика
    public void deleteAllSubtasksFromEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();

        for (Integer subId : subtasksIds) {
            subtasks.remove(subId);
        }

        subtasksIds.clear();
        epic.setSubtasksIds(subtasksIds);
        epics.put(epicId, epic);

        System.out.println("\nСабтаски эпика ID-" + epicId + " очищены сожжением");
        calculateEpicStatus(epicId);
    }

    // ⚪🟡🟢
    // вывод информации о таске
    public void printInfoTask(int taskId) {
        Task task = tasks.get(taskId);
        String title = task.getTitle();
        String description = task.getDescription();
        Status status = task.getStatus();
        String icon = "";

        if (status == Status.NEW) {
            icon = "⚪ New -- ";
        } else if (status == Status.DONE) {
            icon = "\uD83D\uDFE2 Done -- ";
        } else if (status == Status.IN_PROGRESS) {
            icon = "\uD83D\uDFE1 In progress -- ";
        }

        System.out.println("\nТаска ID-" + taskId + ": ");
        System.out.println(icon + title);
        System.out.println(description);
    }

    // ⬜🟨✅
    // вывод информации об эпике
    public void printInfoEpic(int epicId) {
        Epic epic = epics.get(epicId);
        String title = epic.getTitle();
        String description = epic.getDescription();
        Status status = epic.getStatus();
        int subCounter = epic.getSubtasksIds().size();
        String icon = "";

        if (status == Status.NEW) {
            icon = "⬜ New -- ";
        } else if (status == Status.DONE) {
            icon = "✅ Done -- ";
        } else if (status == Status.IN_PROGRESS) {
            icon = "\uD83D\uDFE8 In progress -- ";
        }

        System.out.println("\nЭпик ID-" + epicId + ": ");
        System.out.println(icon + title);
        System.out.println(description);
        System.out.println("Сабтасок: " + subCounter);
    }

    // 💀😸👽
    // вывод информации о сабтаске
    private void printInfoSubtask(int subId) {
        int epicId = findEpicOfSubtask(subId);
        Epic epic = epics.get(epicId);
        Subtask subtask = subtasks.get(subId);
        String title = subtask.getTitle();
        String description = subtask.getDescription();
        Status status = subtask.getStatus();
        String icon = "";

        if (status == Status.NEW) {
            icon = "\uD83D\uDC80 New -- ";
        } else if (status == Status.DONE) {
            icon = "\uD83D\uDC7D Done -- ";
        } else if (status == Status.IN_PROGRESS) {
            icon = "\uD83D\uDE38 In progress -- ";
        }

        System.out.println("\nЭпик ID-" + epicId + " -> Сабтаска ID-" + subId + ": ");
        System.out.println(icon + title);
        System.out.println(description);
    }

    // вывод информации о всех сабтасках эпика (если есть)
    private void printInfoAllSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasks = epic.getSubtasksIds();
        if (subtasks.size() > 0) {
            for (Integer subId : subtasks) {
                printInfoSubtask(subId);
            }
        } else {
            System.out.println("\nЭпик ID-" + epicId + " не содержит сабтасок");
        }
    }

    // вывод информации об эпике со всеми его сабтасками
    public void printInfoEpicWithSubtasks(int epicId) {
        printInfoEpic(epicId);
        printInfoAllSubtasksOfEpic(epicId);
    }

    // вывод информации о всех тасках
    public void printInfoAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            printInfoTask(taskId);
        }
    }

    // вывод информации о всех эпиках без сабтасок
    public void printInfoAllEpics() {
        for (Integer epicId : epics.keySet()) {
            printInfoEpic(epicId);
        }
    }

    // вывод информации о всех эпиках с сабтасками
    public void printInfoAllEpicsWithSubtasks() {
        for (Integer epicId : epics.keySet()) {
            printInfoEpicWithSubtasks(epicId);
        }
    }

    // найти эпик по ID сабтаски
    private int findEpicOfSubtask(int subId) {
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
}
