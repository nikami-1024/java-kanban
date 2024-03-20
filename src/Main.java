import manager.Managers;
import manager.TaskManager;
import model.Task;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        /* start file example:

        type,id,status,title,description,epic
        TASK,2,DONE,Task 2 title,Task description
        EPIC,3,NEW,Epic 1 title,Description,[6, 7]
        SUBTASK,6,IN_PROGRESS,Subtask 3 title,Sub description,3
        SUBTASK,7,NEW,Subtask 4 title,Sub description,3
        HHHHH
        6,7,3,2

        */

        /* test script - will be actual after realization of magic
        with the separate objects of FileBackedTasksManager

        FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("saveTasks2.csv"));
        fileManager.createTask(new Task("task1", "Купить автомобиль"));
        fileManager.createEpic(new Epic("new Epic1", "Новый Эпик"));
        fileManager.createSubtask(new Subtask("New Subtask", "Подзадача", 2));
        fileManager.createSubtask(new Subtask("New Subtask2", "Подзадача2", 2));
        fileManager.getTask(1);
        fileManager.getEpic(2);
        fileManager.getSubtask(3);
        System.out.println(fileManager.getTasks());
        System.out.println(fileManager.getEpics());
        System.out.println(fileManager.getSubTasks());
        System.out.println(fileManager.getHistory());
        System.out.println("\n\n" + "new" + "\n\n");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("saveTasks2.csv"));
        System.out.println(fileBackedTasksManager.getTasks());
        System.out.println(fileBackedTasksManager.getEpics());
        System.out.println(fileBackedTasksManager.getSubTasks());
        System.out.println(fileBackedTasksManager.getHistory());

         */

        String filepath = "out/taskdata.csv";
        TaskManager fbtm = Managers.getDefaultFileBacked(filepath);

        System.out.println("\nViewing history #1:");
        for (Task task : fbtm.getHistory()) {
            System.out.println(task.toString());
        }

        System.out.println("\nTasks and epics creation:");

        Task taskOne = fbtm.createTask("Task 8 title", "Task 8 description");
        int taskOneId = taskOne.getId();
        Task savedTask1 = fbtm.getTaskById(taskOneId);

        Task taskTwo = fbtm.createTask("Task 9 title", "Task 9 description");
        int taskTwoId = taskTwo.getId();
        Task savedTask2 = fbtm.getTaskById(taskTwoId);


        System.out.println("\nViewing history #2:");
        for (Task task : fbtm.getHistory()) {
            System.out.println(task.toString());
        }
    }
}
