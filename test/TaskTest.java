import manager.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import model.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void addNewTask() {
        TaskManager imtm = Managers.getDefault();
        Task taskOne = imtm.createTask("Test addNewTask", "Test addNewTask description");

        final int taskId = taskOne.getId();
        final Task savedTask = imtm.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(taskOne, savedTask, "Задачи не совпадают.");
    }

}