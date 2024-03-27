package manager;

import model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    // это должен быть файл сохранения
    private File filepath;

    public FileBackedTaskManager() {
        // файл не дан - создать новый
        // кодировки?
        this.filepath = new File("out/newSave-"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy--HH-mm-ss"))
                + ".csv");

        System.out.println("\nДобро пожаловать в File Backed таск менеджер!");
    }

    public FileBackedTaskManager(File filepath) {
        // проверка на доступность сохранения в файл
        // если нет - создать новый
        if (filepath.canWrite()) {
            this.filepath = filepath;
        } else {
            // кодировки?
            this.filepath = new File("out/newSave-"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy--HH-mm-ss"))
                    + ".csv");
        }

        System.out.println("\nДобро пожаловать в File Backed таск менеджер!");
    }

    private void save() {
        try {
            Writer fileWriter = new FileWriter(filepath);

            if (filepath.canWrite()) {
                fileWriter.write("type,id,status,title,description,startTime,duration,epic\n");

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

                // разделитель и история просмотров
                fileWriter.write("\n");
                fileWriter.write(CSVParser.historyToString(getHistory()));

                fileWriter.close();
            } else {
                throw new ManagerSaveException("Шеф, всё пропало");
            }

        } catch (IOException | ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File filepath){

        FileBackedTaskManager ifbtm = new FileBackedTaskManager(filepath);

        try {
            FileReader fReader = new FileReader(filepath);
            BufferedReader bReader = new BufferedReader(fReader);
            boolean isNextHistoryLine = false;

            while (bReader.ready()) {
                String str = bReader.readLine();
                boolean isFirstLine = str.startsWith("type");
                boolean isEmpty = str.isEmpty();

                if (isEmpty) {
                    isNextHistoryLine = true;
                } else if (!isFirstLine) {
                    if (isNextHistoryLine) {
                        // read the history
                        int[] idArray = CSVParser.historyParser(str);

                        for (int i = idArray.length - 1; i >= 0; i--) {
                            int taskId = idArray[i];
                            Task task = ifbtm.getAnyTaskById(taskId);
                        }
                    } else {
                        // read the task
                        String[] rawData = CSVParser.taskParser(str);

                        TaskType type = TaskType.valueOf(rawData[0]);
                        int id = Integer.parseInt(rawData[1]);
                        Status status = Status.valueOf(rawData[2]);
                        String title = rawData[3];
                        String description = rawData[4];
                        LocalDateTime startTime = LocalDateTime.parse(rawData[5],
                                DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
                        Long duration = Long.valueOf(rawData[6]);

                        if (type == TaskType.TASK) {
                            Task task = ifbtm.createTask(title, description);
                            ifbtm.updateTaskId(task.getId(), id);
                            if (status != Status.NEW) {
                                ifbtm.updateTaskStatus(id, status);
                            }
                            ifbtm.updateTaskStartTime(id, startTime);
                            ifbtm.updateTaskDuration(id, duration);

                        } else if (type == TaskType.EPIC) {
                            Epic epic = ifbtm.createEpic(title, description);
                            ifbtm.updateEpicId(epic.getId(), id);

                        } else {
                            int epicId = Integer.parseInt(rawData[7]);
                            Subtask subtask = ifbtm.createSubtask(title, description, epicId);
                            ifbtm.updateSubtaskId(subtask.getId(), id);
                            if (status != Status.NEW) {
                                ifbtm.updateSubtaskStatus(id, status);
                            }
                            ifbtm.updateSubtaskStartTime(id, startTime);
                            ifbtm.updateSubtaskDuration(id, duration);
                        }

                        ifbtm.save();
                    }
                }
            }

            bReader.close();
            System.out.println("\nВсе задачи восстановлены из файла: " + filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ifbtm;
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

    // создание таски
    @Override
    public Task createTask(String title, String description) {
        Task task = super.createTask(title, description);
        save();
        return task;
    }

    // создание эпика
    @Override
    public Epic createEpic(String title, String description) {
        Epic epic = super.createEpic(title, description);
        save();
        return epic;
    }

    // создание сабтаски
    @Override
    public Subtask createSubtask(String title, String description, int epicId) {
        Subtask subtask = super.createSubtask(title, description, epicId);
        save();
        return subtask;
    }

    // обновление заголовка таски
    @Override
    public void updateTaskTitle(int taskId, String title) {
        super.updateTaskTitle(taskId, title);
        save();
    }

    // обновление описания таски
    @Override
    public void updateTaskDescription(int taskId, String description) {
        super.updateTaskDescription(taskId, description);
        save();
    }

    // обновление статуса таски
    @Override
    public void updateTaskStatus(int taskId, Status status) {
        super.updateTaskStatus(taskId, status);
        save();
    }

    // установка времени старта задачи
    @Override
    public void updateTaskStartTime(int taskId, LocalDateTime startTime) {
        super.updateTaskStartTime(taskId, startTime);
        save();
    }

    // установка длительности задачи
    @Override
    public void updateTaskDuration(int taskId, long durationInMinutes) {
        super.updateTaskDuration(taskId, durationInMinutes);
        save();
    }

    // обновление заголовка эпика
    @Override
    public void updateEpicTitle(int epicId, String title) {
        super.updateEpicTitle(epicId, title);
        save();
    }

    // обновление описания эпика
    @Override
    public void updateEpicDescription(int epicId, String description) {
        super.updateEpicDescription(epicId, description);
        save();
    }

    // обновление заголовка сабтаски
    @Override
    public void updateSubtaskTitle(int subId, String title) {
        super.updateSubtaskTitle(subId, title);
        save();
    }

    // обновление описания сабтаски
    @Override
    public void updateSubtaskDescription(int subId, String description) {
        super.updateSubtaskDescription(subId, description);
        save();
    }

    // обновление статуса сабтаски
    @Override
    public void updateSubtaskStatus(int subId, Status status) {
        super.updateSubtaskStatus(subId, status);
        save();
    }

    // установка времени старта сабтаски
    @Override
    public void updateSubtaskStartTime(int subId, LocalDateTime startTime) {
        super.updateSubtaskStartTime(subId, startTime);
        save();
    }

    // установка длительности сабтаски
    @Override
    public void updateSubtaskDuration(int subId, long durationInMinutes) {
        super.updateSubtaskDuration(subId, durationInMinutes);
        save();
    }

    // перемещение сабтаски в новый эпик
    // сущность в истории просмотров (если была) не затрагивается никак
    @Override
    public void moveSubtask(int subId, int newEpicId) {
        super.moveSubtask(subId, newEpicId);
        save();
    }

    // удаление таски по ID
    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    // удаление всех-всех тасок
    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    // удаление эпика с сабтасками по ID эпика
    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    // удаление всех-всех эпиков с сабтасками
    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    // удаление сабтаски по ID
    @Override
    public void deleteSubtaskFromEpic(int subId) {
        super.deleteSubtaskFromEpic(subId);
        save();
    }

    // удаление всех-всех сабтасок у эпика
    @Override
    public void deleteAllSubtasksFromEpic(int epicId) {
        super.deleteAllSubtasksFromEpic(epicId);
        save();
    }

    // возврат таски по ID
    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    // возврат эпика по ID
    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    // возврат сабтаски по ID
    @Override
    public Subtask getSubtaskById(int subId) {
        Subtask subtask = super.getSubtaskById(subId);
        save();
        return subtask;
    }

    // возврат сущности по id
    @Override
    public Task getAnyTaskById(int id) {
        Task task = super.getAnyTaskById(id);
        save();
        return task;
    }
}
