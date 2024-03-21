import manager.TaskManager;
import manager.Managers;
import model.Status;
import org.junit.jupiter.api.Test;
import model.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private static final TaskManager imtm = Managers.getDefault();

    @Test
    void testAddNewTask() {
        Task taskOne = imtm.createTask("Test createTask()", "Test description");
        final int taskId = taskOne.getId();
        final Task savedTask = imtm.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(taskOne, savedTask, "Задачи не совпадают.");
    }

    @Test
    void testUpdateTaskTitle() {
        Task taskOne = imtm.createTask("Test updateTaskTitle()", "Test description");
        final int taskId = taskOne.getId();
        String newTitle = "Updated title";

        imtm.updateTaskTitle(taskId, newTitle);
        final Task savedTask = imtm.getTaskById(taskId);

        assertEquals(newTitle, savedTask.getTitle(), "Заголовки не совпадают.");
    }

    @Test
    void testUpdateTaskDescription() {
        Task taskOne = imtm.createTask("Test updateTaskDescription()",
                "Test description");
        final int taskId = taskOne.getId();
        String newDescription = "Updated description";

        imtm.updateTaskDescription(taskId, newDescription);
        final Task savedTask = imtm.getTaskById(taskId);

        assertEquals(newDescription, savedTask.getDescription(), "Описания не совпадают.");
    }

    @Test
    void testUpdateTaskStatus() {
        Task taskOne = imtm.createTask("Test updateTaskStatus()",
                "Test description");
        final int taskId = taskOne.getId();

        imtm.updateTaskStatus(taskId, Status.IN_PROGRESS);
        final Task savedTask = imtm.getTaskById(taskId);

        assertEquals(Status.IN_PROGRESS, savedTask.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void testTaskDeletion() {
        final int initCounter = imtm.getEntitiesCounter();
        Task taskOne = imtm.createTask("Test deleteTask()", "Test description");
        final int taskId = taskOne.getId();

        imtm.deleteTask(taskId);
        final int finalCounter = imtm.getEntitiesCounter();

        assertEquals(initCounter, finalCounter, "Задача не удалена.");
    }

    @Test
    void testAllTasksDeletion() {
        Task taskOne = imtm.createTask("Test deleteAllTasks() 1",
                "Test description 1");
        Task taskTwo = imtm.createTask("Test deleteAllTasks() 2",
                "Test description 2");

        imtm.deleteAllTasks();
        final int counter = imtm.getEntitiesCounter();

        assertEquals(0, counter, "Задачи не удалены.");
    }

    @Test
    void testTaskOutput() {
        Task taskOne = imtm.createTask("Test title", "Test description");
        final int taskId = taskOne.getId();

        String expectedOutput = "TASK," + taskId + ",NEW,Test title,Test description";
        String actualOutput = taskOne.toString();

        assertEquals(expectedOutput, actualOutput, "Задачи не удалены.");
    }
}