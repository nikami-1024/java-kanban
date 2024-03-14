import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager imtm = Managers.getDefault();

        // пользовательский сценарий -- спринт 6
        System.out.println("\nTasks and epics creation:");
        Task taskOne = imtm.createTask("Task 1 title", "Task description");
        int taskOneId = taskOne.getId();
        Task taskTwo = imtm.createTask("Task 2 title", "Task description");
        int taskTwoId = taskTwo.getId();

        Epic epicOne = imtm.createEpic("Epic 1 title", "Description");
        int epicOneId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Subtask 1 title",
                "Sub description", epicOneId);
        int subtaskOneId = subtaskOne.getId();
        Subtask subtaskTwo = imtm.createSubtask("Subtask 2 title",
                "Sub description", epicOneId);
        int subtaskTwoId = subtaskTwo.getId();
        Subtask subtaskThree = imtm.createSubtask("Subtask 3 title",
                "Sub description", epicOneId);
        int subtaskThreeId = subtaskThree.getId();

        Epic epicTwo = imtm.createEpic("Epic 2 title", "Description");
        int epicTwoId = epicTwo.getId();

        // создание истории просмотров
        Task savedTask = imtm.getTaskById(taskOne.getId());
        savedTask = imtm.getTaskById(taskTwo.getId());
        savedTask = imtm.getTaskById(taskOne.getId());

        // expected history #1:
        // Task 1
        // Task 2

        System.out.println("\nViewing history #1:");
        for (Task task : imtm.getHistory()) {
            System.out.println(task.toString());
        }

        Epic savedEpic = imtm.getEpicById(epicTwoId);
        Subtask savedSubtask = imtm.getSubtaskById(subtaskThreeId);
        savedSubtask = imtm.getSubtaskById(subtaskTwoId);
        savedSubtask = imtm.getSubtaskById(subtaskOneId);
        savedSubtask = imtm.getSubtaskById(subtaskThreeId);
        savedEpic = imtm.getEpicById(epicOneId);
        savedEpic = imtm.getEpicById(epicTwoId);

        // expected history #2:
        // Epic 2
        // Epic 1
        // Subtask 3
        // Subtask 1
        // Subtask 2
        // Task 1
        // Task 2

        System.out.println("\nViewing history #2:");
        for (Task task : imtm.getHistory()) {
            System.out.println(task.toString());
        }

        imtm.deleteTask(taskOneId);

        // expected history #3:
        // Epic 2
        // Epic 1
        // Subtask 3
        // Subtask 1
        // Subtask 2
        // Task 2

        System.out.println("\nViewing history #3:");
        for (Task task : imtm.getHistory()) {
            System.out.println(task.toString());
        }

        imtm.deleteEpic(epicOneId);

        // expected history #4:
        // Epic 2
        // Task 2

        System.out.println("\nViewing history #4:");
        for (Task task : imtm.getHistory()) {
            System.out.println(task.toString());
        }
    }
}
