import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    protected HashMap<Integer, Task> tasksAndEpics;
    protected int nextID;

    public TaskManager() {
        tasksAndEpics = new HashMap<>();
        nextID = 1;
        System.out.println("Добро пожаловать в таск менеджер!");
    }

    // создание таски
    public void createTask(String title, String description) {
        Task task = new Task(nextID, title, description, Status.NEW);
        nextID++;
        tasksAndEpics.put(task.getID(), task);
        System.out.println("\nНовая таска создана!");
        System.out.println("ID-" + task.getID() + " -- " + task.getTitle());
    }

    // создание эпика
    public void createEpic(String title, String description) {
        Epic epic = new Epic(nextID, title, description, Status.NEW);
        nextID++;
        tasksAndEpics.put(epic.getID(), epic);
        System.out.println("\nНовый эпик создан!");
        System.out.println("ID-" + epic.getID() + " -- " + epic.getTitle());
    }

    // создание сабтаски
    public void createSubtask(String title, String description, int epicID) {
        if (tasksAndEpics.get(epicID).getClass().getName().equals("Epic")) {
            Epic oldEpic = (Epic) tasksAndEpics.get(epicID);
            HashMap<Integer, Subtask> newSubtasks = oldEpic.getSubtasks();

            Subtask subtask = new Subtask(nextID, title, description, Status.NEW);
            nextID++;
            newSubtasks.put(subtask.getID(), subtask);

            Epic newEpic = new Epic(epicID, oldEpic.getTitle(), oldEpic.getDescription(), oldEpic.getStatus(), newSubtasks);
            tasksAndEpics.put(epicID, newEpic);

            System.out.println("\nВ эпик ID-" + epicID + " добавлена новая сабтаска");
            System.out.println("ID-" + subtask.getID() + " -- " + subtask.getTitle());
            calculateEpicStatus(newEpic.getID());
        } else {
            System.out.println("\nСущность ID-" + epicID + " не является эпиком");
            System.out.println("Попытка добавления сабтаски провалилась");
        }
    }

    // обновление заголовка таски
    public void updateTaskTitle(int ID, String title) {
        if (tasksAndEpics.get(ID).getClass().getName().equals("Task")) {
            Task oldTask = tasksAndEpics.get(ID);
            Task newTask = new Task(ID, title, oldTask.getDescription(), oldTask.getStatus());
            tasksAndEpics.put(ID, newTask);
            System.out.println("\nТаска ID-" + ID + " обновлена");
        } else {
            System.out.println("\nСущность ID-" + ID + " не является таской");
        }
    }

    // обновление описания таски
    public void updateTaskDescription(int ID, String description) {
        if (tasksAndEpics.get(ID).getClass().getName().equals("Task")) {
            Task oldTask = tasksAndEpics.get(ID);
            Task newTask = new Task(ID, oldTask.getTitle(), description, oldTask.getStatus());
            tasksAndEpics.put(ID, newTask);
            System.out.println("\nТаска ID-" + ID + " обновлена");
        } else {
            System.out.println("\nСущность ID-" + ID + " не является таской");
        }
    }

    // обновление статуса таски
    public void updateTaskStatus(int ID, Status status) {
        if (tasksAndEpics.get(ID).getClass().getName().equals("Task")) {
            Task oldTask = tasksAndEpics.get(ID);
            Task newTask = new Task(ID, oldTask.getTitle(), oldTask.getDescription(), status);
            tasksAndEpics.put(ID, newTask);
            System.out.println("\nТаска ID-" + ID + " обновлена");
        } else {
            System.out.println("\nСущность ID-" + ID + " не является таской");
        }
    }

    // обновление заголовка эпика
    public void updateEpicTitle(int ID, String title) {
        if (tasksAndEpics.get(ID).getClass().getName().equals("Epic")) {
            Epic oldEpic = (Epic) tasksAndEpics.get(ID);
            Epic newEpic = new Epic(ID, title, oldEpic.getDescription(), oldEpic.getStatus(), oldEpic.getSubtasks());
            tasksAndEpics.put(ID, newEpic);
            System.out.println("\nЭпик ID-" + ID + " обновлён");
        } else {
            System.out.println("\nСущность ID-" + ID + " не является эпиком");
        }
    }

    // обновление описания эпика
    public void updateEpicDescription(int ID, String description) {
        if (tasksAndEpics.get(ID).getClass().getName().equals("Epic")) {
            Epic oldEpic = (Epic) tasksAndEpics.get(ID);
            Epic newEpic = new Epic(ID, oldEpic.getTitle(), description, oldEpic.getStatus(), oldEpic.getSubtasks());
            tasksAndEpics.put(ID, newEpic);
            System.out.println("\nЭпик ID-" + ID + " обновлён");
        } else {
            System.out.println("\nСущность ID-" + ID + " не является эпиком");
        }
    }

    // расчёт статуса эпика на основе статусов сабтасок
    public void calculateEpicStatus(int epicID) {
        Epic oldEpic = (Epic) tasksAndEpics.get(epicID);
        HashMap<Integer, Subtask> subtasks = oldEpic.getSubtasks();
        Status oldStatus = oldEpic.getStatus();
        Status newStatus;

        if (subtasks.size() == 0) {
            newStatus = Status.NEW;
        } else {
            boolean isExistNew = false;
            boolean isExistDone = false;
            boolean isExistWIP = false;

            for (Integer subID : subtasks.keySet()) {
                Status subStatus = subtasks.get(subID).getStatus();
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
            Epic newEpic = new Epic(epicID, oldEpic.getTitle(), oldEpic.getDescription(),
                    newStatus, oldEpic.getSubtasks());
            tasksAndEpics.put(epicID, newEpic);
        }

        System.out.println("Статус эпика ID-" + epicID + " пересчитан");
    }

    // обновление заголовка сабтаски
    public void updateSubtaskTitle(int subID, String title) {
        int epicID = findEpicOfSubtask(subID);
        Epic oldEpic = (Epic) tasksAndEpics.get(epicID);
        HashMap<Integer, Subtask> subtasks = oldEpic.getSubtasks();
        Subtask oldSubtask = subtasks.get(subID);

        Subtask newSubtask = new Subtask(subID, title, oldSubtask.getDescription(), oldSubtask.getStatus());
        subtasks.put(subID, newSubtask);
        Epic newEpic = new Epic(epicID, oldEpic.getTitle(), oldEpic.getDescription(), oldEpic.getStatus(), subtasks);
        tasksAndEpics.put(epicID, newEpic);

        System.out.println("\nСабтаска ID-" + subID + " обновлена");
    }

    // обновление описания сабтаски
    public void updateSubtaskDescription(int subID, String description) {
        int epicID = findEpicOfSubtask(subID);
        Epic oldEpic = (Epic) tasksAndEpics.get(epicID);
        HashMap<Integer, Subtask> subtasks = oldEpic.getSubtasks();
        Subtask oldSubtask = subtasks.get(subID);

        Subtask newSubtask = new Subtask(subID, oldSubtask.getTitle(), description, oldSubtask.getStatus());
        subtasks.put(subID, newSubtask);
        Epic newEpic = new Epic(epicID, oldEpic.getTitle(), oldEpic.getDescription(), oldEpic.getStatus(), subtasks);
        tasksAndEpics.put(epicID, newEpic);

        System.out.println("\nСабтаска ID-" + subID + " обновлена");
    }

    // обновление статуса сабтаски
    public void updateSubtaskStatus(int subID, Status status) {
        int epicID = findEpicOfSubtask(subID);
        Epic oldEpic = (Epic) tasksAndEpics.get(epicID);
        HashMap<Integer, Subtask> subtasks = oldEpic.getSubtasks();
        Subtask oldSubtask = subtasks.get(subID);

        Subtask newSubtask = new Subtask(subID, oldSubtask.getTitle(), oldSubtask.getDescription(), status);
        subtasks.put(subID, newSubtask);
        Epic newEpic = new Epic(epicID, oldEpic.getTitle(), oldEpic.getDescription(), oldEpic.getStatus(), subtasks);
        tasksAndEpics.put(epicID, newEpic);

        System.out.println("\nСабтаска ID-" + subID + " обновлена");
        calculateEpicStatus(epicID);
    }

    // перемещение сабтаски в новый эпик
    public void moveSubtask(int subID, int freshEpicID) {
        if (tasksAndEpics.get(freshEpicID).getClass().getName().equals("Epic")) {
            int obsEpicID = findEpicOfSubtask(subID);

            // удаление сабтаски из старого эпика
            Epic oldEpic = (Epic) tasksAndEpics.get(obsEpicID);
            HashMap<Integer, Subtask> subtasks = oldEpic.getSubtasks();
            Subtask subtask = subtasks.get(subID);
            deleteSubtaskFromEpic(subID);

            // добавление сабтаски в новый эпик
            createSubtask(subtask.getTitle(), subtask.getDescription(), freshEpicID);

            System.out.println("\nСабтаска ID-" + subID + " перемещена из эпика ID-" +
                    obsEpicID + " в эпик ID-" + freshEpicID);
        } else {
            System.out.println("\nСущность ID-" + freshEpicID + " не является эпиком");
            System.out.println("Перемещение сабтаски ID-" + subID + " не состоялось");
        }
    }

    // удаление таски по ID
    public void deleteTask(int ID) {
        if (tasksAndEpics.get(ID).getClass().getName().equals("Task")) {
            tasksAndEpics.remove(ID);
            System.out.println("\nТаска ID-" + ID + " удалена");
        } else {
            System.out.println("\nСущность ID-" + ID + " не является таской");
        }
    }

    // удаление всех-всех тасок
    public void deleteAllTasks() {
        ArrayList<Integer> taskIDs = getAllTasksIDs();
        for (Integer taskID : taskIDs) {
            tasksAndEpics.remove(taskID);
        }
        System.out.println("\nВсе таски безвозвратно удалены");
    }

    // удаление эпика с сабтасками по ID эпика
    public void deleteEpic(int ID) {
        if (tasksAndEpics.get(ID).getClass().getName().equals("Epic")) {
            tasksAndEpics.remove(ID);
            System.out.println("\nЭпик ID-" + ID + " со всеми сабтасками удалён");
        } else {
            System.out.println("\nСущность ID-" + ID + " не является эпиком");
        }
    }

    // удаление всех-всех эпиков с сабтасками
    public void deleteAllEpics() {
        ArrayList<Integer> epicIDs = getAllEpicsIDs();
        for (Integer epicID : epicIDs) {
            tasksAndEpics.remove(epicID);
        }
        System.out.println("\nВсе эпики с сабтасками безвозвратно удалены");
    }

    // удаление сабтаски по ID
    public void deleteSubtaskFromEpic(int subID) {
        int epicID = findEpicOfSubtask(subID);
        Epic oldEpic = (Epic) tasksAndEpics.get(epicID);
        HashMap<Integer, Subtask> subtasks = oldEpic.getSubtasks();

        subtasks.remove(subID);
        Epic newEpic = new Epic(epicID, oldEpic.getTitle(), oldEpic.getDescription(), oldEpic.getStatus(), subtasks);
        tasksAndEpics.put(epicID, newEpic);

        System.out.println("\nСабтаска ID-" + subID + " удалена из эпика ID-" + epicID);
        calculateEpicStatus(epicID);
    }

    // удаление всех-всех сабтасок у эпика
    public void deleteAllSubtasksFromEpic(int epicID) {
        if (tasksAndEpics.get(epicID).getClass().getName().equals("Epic")) {
            Epic oldEpic = (Epic) tasksAndEpics.get(epicID);
            HashMap<Integer, Subtask> emptySubtasks = new HashMap<>();

            Epic newEpic = new Epic(epicID, oldEpic.getTitle(), oldEpic.getDescription(),
                    oldEpic.getStatus(), emptySubtasks);
            tasksAndEpics.put(epicID, newEpic);

            System.out.println("\nСабтаски эпика ID-" + epicID + " очищены сожжением");
            calculateEpicStatus(epicID);
        } else {
            System.out.println("\nСущность ID-" + epicID + " не является эпиком");
        }
    }

    // ⚪🟡🟢
    // вывод информации о таске
    public void printInfoTask(int ID) {
        if (tasksAndEpics.get(ID).getClass().getName().equals("Task")) {
            Task task = tasksAndEpics.get(ID);
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

            System.out.println("\nТаска ID-" + ID + ": ");
            System.out.println(icon + title);
            System.out.println(description);
        } else {
            System.out.println("\nСущность ID-" + ID + " не является таской");
        }
    }

    // ⬜🟨✅
    // вывод информации об эпике
    public void printInfoEpic(int epicID) {
        if (tasksAndEpics.get(epicID).getClass().getName().equals("Epic")) {
            Epic epic = (Epic) tasksAndEpics.get(epicID);
            String title = epic.getTitle();
            String description = epic.getDescription();
            Status status = epic.getStatus();
            int subCounter = epic.getSubtasks().size();
            String icon = "";

            if (status == Status.NEW) {
                icon = "⬜ New -- ";
            } else if (status == Status.DONE) {
                icon = "✅ Done -- ";
            } else if (status == Status.IN_PROGRESS) {
                icon = "\uD83D\uDFE8 In progress -- ";
            }

            System.out.println("\nЭпик ID-" + epicID + ": ");
            System.out.println(icon + title);
            System.out.println(description);
            System.out.println("Сабтасок: " + subCounter);
        } else {
            System.out.println("\nСущность ID-" + epicID + " не является эпиком");
        }
    }

    // 💀😸👽
    // вывод информации о сабтаске
    public void printInfoSubtask(int subID) {
        int epicID = findEpicOfSubtask(subID);
        Epic epic = (Epic) tasksAndEpics.get(epicID);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
        Subtask subtask = subtasks.get(subID);
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

        System.out.println("\nЭпик ID-" + epicID + " -> Сабтаска ID-" + subID + ": ");
        System.out.println(icon + title);
        System.out.println(description);
    }

    // вывод информации о всех сабтасках эпика (если есть)
    public void printInfoAllSubtasksOfEpic(int epicID) {
        if (tasksAndEpics.get(epicID).getClass().getName().equals("Epic")) {
            Epic epic = (Epic) tasksAndEpics.get(epicID);
            HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
            if (subtasks.size() > 0) {
                for (Integer subID : subtasks.keySet()) {
                    printInfoSubtask(subID);
                }
            } else {
                System.out.println("\nЭпик ID-" + epicID + " не содержит сабтасок");
            }
        } else {
            System.out.println("\nСущность ID-" + epicID + " не является эпиком");
        }
    }

    // вывод информации об эпике со всеми его сабтасками
    public void printInfoEpicWithSubtasks(int epicID) {
        printInfoEpic(epicID);
        printInfoAllSubtasksOfEpic(epicID);
    }

    // вывод информации о всех тасках
    public void printInfoAllTasks() {
        ArrayList<Integer> epicIDs = getAllTasksIDs();
        for (Integer ID : epicIDs) {
            printInfoTask(ID);
        }
    }

    // вывод информации о всех эпиках без сабтасок
    public void printInfoAllEpics() {
        ArrayList<Integer> epicIDs = getAllEpicsIDs();
        for (Integer ID : epicIDs) {
            printInfoEpic(ID);
        }
    }

    // вывод информации о всех эпиках с сабтасками
    public void printInfoAllEpicsWithSubtasks() {
        ArrayList<Integer> epicIDs = getAllEpicsIDs();
        for (Integer ID : epicIDs) {
            printInfoEpicWithSubtasks(ID);
        }
    }

    // найти эпик по ID сабтаски
    public int findEpicOfSubtask(int subID) {
        int epicID = -1;

        for (Integer indx : tasksAndEpics.keySet()) {
            if (tasksAndEpics.get(indx).getClass().getName().equals("Epic")) {
                Epic epic = (Epic) tasksAndEpics.get(indx);
                HashMap<Integer, Subtask> subt = epic.getSubtasks();
                for (Integer subIndx : subt.keySet()) {
                    epicID = subID == subIndx ? indx : epicID;
                }
            }
        }

        return epicID;
    }

    // вернуть список ID тасок
    public ArrayList<Integer> getAllTasksIDs() {
        ArrayList<Integer> taskIDs = new ArrayList<>();
        for (Integer ID : tasksAndEpics.keySet()) {
            if (tasksAndEpics.get(ID).getClass().getName().equals("Task")) {
                taskIDs.add(ID);
            }
        }
        return taskIDs;
    }

    // вернуть список ID эпиков
    public ArrayList<Integer> getAllEpicsIDs() {
        ArrayList<Integer> epicIDs = new ArrayList<>();
        for (Integer ID : tasksAndEpics.keySet()) {
            if (tasksAndEpics.get(ID).getClass().getName().equals("Epic")) {
                epicIDs.add(ID);
            }
        }
        return epicIDs;
    }
}
