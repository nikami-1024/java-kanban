import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        /* start file example - out/taskdata.csv:

        type,id,status,title,description,startTime,duration,epic
        TASK,2,DONE,Task 2 title,Task description,13:00 01-01-2005,30
        EPIC,3,IN_PROGRESS,Epic 1 title,Description,13:00 01-01-2005,60,[6, 7]
        SUBTASK,6,IN_PROGRESS,Subtask 3 title,Sub description,13:00 01-01-2005,30,3
        SUBTASK,7,NEW,Subtask 4 title,Sub description,13:00 01-01-2005,30,3

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

        System.out.println("\nViewing history #1:");
        for (Task task : fbtm.getHistory()) {
            System.out.println(task.toString());
        }

        System.out.println("\n\n" + "new" + "\n\n");
        FileBackedTaskManager fbtmSecond = FileBackedTaskManager.loadFromFile(
                new File("out/taskdata.csv"));

        System.out.println("\nViewing history #2:");
        for (Task task : fbtmSecond.getHistory()) {
            System.out.println(task.toString());
        }
    }
}
