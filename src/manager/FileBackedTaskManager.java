package manager;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    protected String filepath;

    public FileBackedTaskManager(String filepath) throws IOException {
        // тут надо звать super-конструктор?

        this.filepath = filepath;

        System.out.println("\nДобро пожаловать в File Backed таск менеджер!");
        loadFromFile(this.filepath);
        System.out.println("\nВсе задачи восстановлены из файла: " + this.filepath);
    }

    private void save() throws IOException {
        Writer fileWriter = new FileWriter(filepath);

        fileWriter.write("type,id,status,title,description,epic\n");

        for (Integer taskId : tasks.keySet()) {
            Task task = tasks.get(taskId);
            fileWriter.write(task.toString() + "\n");
        }

        // здесь в файл записывается избыточная информация о сабтасках, но она не мешается
        for (Integer epicId : epics.keySet()) {
            Epic epic = epics.get(epicId);
            fileWriter.write(epic.toString() + "\n");
        }

        for (Integer subtaskId : subtasks.keySet()) {
            Subtask subtask = subtasks.get(subtaskId);
            fileWriter.write(subtask.toString() + "\n");
        }

        fileWriter.write("HHHHH\n");
        fileWriter.write(historyToString());

        fileWriter.close();
    }

    private void loadFromFile(String filepath) throws IOException {
        FileReader fReader = new FileReader(filepath);
        BufferedReader bReader = new BufferedReader(fReader);
        boolean isNextHistoryLine = false;

        while (bReader.ready()) {
            String str = bReader.readLine();
            boolean isFirstLine = str.startsWith("type");
            boolean isEmpty = str.isEmpty();
            boolean isSeparator = str.startsWith("HHHH");

            if (isSeparator) {
                isNextHistoryLine = true;
            }

            if (!isEmpty && !isFirstLine && !isSeparator) {
                if (isNextHistoryLine) {
                    // read the history
                    historyFromString(str);
                } else {
                    // read the task
                    taskFromString(str);
                }
            }
        }

        bReader.close();
    }

    private void taskFromString(String str) throws IOException {
        Task rawTask = CSVParser.taskParser(str);

        String strType = String.valueOf(rawTask.getClass());
        TaskType type;
        int id = rawTask.getId();
        Status status = rawTask.getStatus();
        String title = rawTask.getTitle();
        String description = rawTask.getDescription();

        strType = strType.substring(12).toUpperCase();

        if (strType.equals(TaskType.TASK.toString())) {
            type = TaskType.TASK;
        } else if (strType.equals(TaskType.EPIC.toString())) {
            type = TaskType.EPIC;
        } else {
            type = TaskType.SUBTASK;
        }

        if (type == TaskType.TASK) {
            Task task = super.createTask(title, description);
            updateTaskId(task.getId(), id);
            if (status != Status.NEW) {
                super.updateTaskStatus(id, status);
            }
        } else if (type == TaskType.EPIC) {
            Epic epic = super.createEpic(title, description);
            updateEpicId(epic.getId(), id);
        } else {
            // жуткий костыль
            // но оно не даёт мне взять rawTask.getEpicId()
            int epicId = Integer.parseInt(str.substring(str.length() - 1));
            Subtask subtask = super.createSubtask(title, description, epicId);
            updateSubtaskId(subtask.getId(), id);
            if (status != Status.NEW) {
                super.updateSubtaskStatus(id, status);
            }
        }

        save();
    }

    private void historyFromString(String str) {
        int[] idArray = CSVParser.historyParser(str);

        for (int i = idArray.length - 1; i >= 0; i--) {
            int taskId = idArray[i];

            if (tasks.containsKey(taskId)) {
                Task task = getTaskById(taskId);
            } else if (epics.containsKey(taskId)) {
                Epic epic = getEpicById(taskId);
            } else {
                Subtask subtask = getSubtaskById(taskId);
            }
        }
    }

    private String historyToString() {
        LinkedList<Task> historyList = getHistory();
        StringBuilder historyStr = new StringBuilder();

        for (Task task : historyList) {
            int taskId = task.getId();
            if (historyStr.isEmpty()) {
                historyStr.append(taskId);
            } else {
                historyStr.append(",");
                historyStr.append(taskId);
            }
        }

        return historyStr.toString();
    }

    // обновление id таски
    protected void updateTaskId(int oldId, int newId) {
        Task task = tasks.get(oldId);
        task.setId(newId);
        tasks.remove(oldId);
        tasks.put(newId, task);
    }

    // обновление id эпика
    protected void updateEpicId(int oldId, int newId) {
        Epic epic = epics.get(oldId);
        epic.setId(newId);
        epics.remove(oldId);
        epics.put(newId, epic);
    }

    // обновление id сабтаски
    protected void updateSubtaskId(int oldId, int newId) {
        // взятие сущностей
        Subtask subtask = subtasks.get(oldId);
        int epicId = super.findEpicOfSubtask(oldId);
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();

        // обновление id сабтаски и перезапись в subtasks
        subtask.setId(newId);
        subtasks.remove(oldId);
        subtasks.put(newId, subtask);

        // обновление информации в эпике
        subtasksIds.remove(subtasksIds.indexOf(oldId));
        subtasksIds.add(newId);
        epic.setSubtasksIds(subtasksIds);
        epics.put(epicId, epic);
    }

    // далее в унаследованных методах обернула каждый save() в try-catch
    // иначе throws IOException прорастает в InMemoryTaskManager, где оно не нужно

    // создание таски
    @Override
    public Task createTask(String title, String description) {
        Task task = super.createTask(title, description);

        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    // создание эпика
    @Override
    public Epic createEpic(String title, String description) {
        Epic epic = super.createEpic(title, description);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return epic;
    }

    // создание сабтаски
    @Override
    public Subtask createSubtask(String title, String description, int epicId) {
        Subtask subtask = super.createSubtask(title, description, epicId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return subtask;
    }

    // обновление заголовка таски
    @Override
    public void updateTaskTitle(int taskId, String title) {
        super.updateTaskTitle(taskId, title);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // обновление описания таски
    @Override
    public void updateTaskDescription(int taskId, String description) {
        super.updateTaskDescription(taskId, description);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // обновление статуса таски
    @Override
    public void updateTaskStatus(int taskId, Status status) {
        super.updateTaskStatus(taskId, status);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // обновление заголовка эпика
    @Override
    public void updateEpicTitle(int epicId, String title) {
        super.updateEpicTitle(epicId, title);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // обновление описания эпика
    @Override
    public void updateEpicDescription(int epicId, String description) {
        super.updateEpicDescription(epicId, description);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // обновление заголовка сабтаски
    @Override
    public void updateSubtaskTitle(int subId, String title) {
        super.updateSubtaskTitle(subId, title);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // обновление описания сабтаски
    @Override
    public void updateSubtaskDescription(int subId, String description) {
        super.updateSubtaskDescription(subId, description);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // обновление статуса сабтаски
    @Override
    public void updateSubtaskStatus(int subId, Status status) {
        super.updateSubtaskStatus(subId, status);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // перемещение сабтаски в новый эпик
    // сущность в истории просмотров (если была) не затрагивается никак
    @Override
    public void moveSubtask(int subId, int newEpicId) {
        super.moveSubtask(subId, newEpicId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // удаление таски по ID
    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // удаление всех-всех тасок
    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // удаление эпика с сабтасками по ID эпика
    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // удаление всех-всех эпиков с сабтасками
    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // удаление сабтаски по ID
    @Override
    public void deleteSubtaskFromEpic(int subId) {
        super.deleteSubtaskFromEpic(subId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // удаление всех-всех сабтасок у эпика
    @Override
    public void deleteAllSubtasksFromEpic(int epicId) {
        super.deleteAllSubtasksFromEpic(epicId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // возврат таски по ID
    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    // возврат эпика по ID
    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return epic;
    }

    // возврат сабтаски по ID
    @Override
    public Subtask getSubtaskById(int subId) {
        Subtask subtask = super.getSubtaskById(subId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return subtask;
    }

    // возврат истории
    @Override
    public LinkedList<Task> getHistory() {
        return super.getHistory();
    }

    // возврат количества всех существующих сущностей
    @Override
    public int getEntitiesCounter() {
        return super.getEntitiesCounter();
    }
}
