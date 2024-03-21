import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Task;

import java.io.File;
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

        // test script

        TaskManager fbtm = Managers.getDefaultFileBacked();
        fbtm.createTask("task 1", "Купить автомобиль");
        fbtm.createEpic("new Epic 1", "Новый Эпик");
        fbtm.createSubtask("New Subtask 1", "Подзадача 1", 2);
        fbtm.createSubtask("New Subtask 2", "Подзадача 2", 2);
        fbtm.getTaskById(1);
        fbtm.getEpicById(2);
        fbtm.getSubtaskById(3);

//        эти методы должны выводить список всех сущностей каждого сорта без добавления в историю?
//        мне надо их реализовать?
//        System.out.println(fileManager.getTasks());
//        System.out.println(fileManager.getEpics());
//        System.out.println(fileManager.getSubTasks());

        System.out.println("\nViewing history #1:");
        for (Task task : fbtm.getHistory()) {
            System.out.println(task.toString());
        }

        System.out.println("\n\n" + "new" + "\n\n");
        FileBackedTaskManager fbtmSecond = FileBackedTaskManager.loadFromFile(
                new File("out/newSaveFile.csv"));

//        System.out.println(fileBackedTasksManager.getTasks());
//        System.out.println(fileBackedTasksManager.getEpics());
//        System.out.println(fileBackedTasksManager.getSubTasks());

        System.out.println("\nViewing history #2:");
        for (Task task : fbtmSecond.getHistory()) {
            System.out.println(task.toString());
        }
    }
}
