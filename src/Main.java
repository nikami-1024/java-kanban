import manager.Managers;
import manager.TaskManager;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager imtm = Managers.getDefault();

        System.out.println("\nСоздание тасок:");
        Task taskOne = imtm.createTask("Купить молока", "Банановое, фундучное, по 1л.");
        Task taskTwo = imtm.createTask("Помыть посуду", "Всю-всю, и сковородку тоже.");
        Task taskThree = imtm.createTask("Выспаться", "Во всех позах.");

        Task savedTaskOne = imtm.getTaskById(taskOne.getId());
        Task savedTaskTwo = imtm.getTaskById(taskTwo.getId());
        Task savedTaskThree = imtm.getTaskById(taskThree.getId());

        System.out.println("\nИстория просмотров:");
        for (Task task : imtm.getHistory()) {
            System.out.println(task.toString());
        }
    }
}
