import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EpicTest {

    private static final TaskManager imtm = Managers.getDefault();

    @Test
    void testAddNewEpic() {
        Epic epicOne = imtm.createEpic("Test createEpic()", "Test description");
        final int epicId = epicOne.getId();
        final Epic savedEpic = imtm.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epicOne, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void testUpdateEpicTitle() {
        Epic epicOne = imtm.createEpic("Test updateEpicTitle()", "Test description");
        final int epicId = epicOne.getId();
        String newTitle = "Updated title";

        imtm.updateEpicTitle(epicId, newTitle);
        final Epic savedEpic = imtm.getEpicById(epicId);

        assertEquals(newTitle, savedEpic.getTitle(), "Заголовки не совпадают.");
    }

    @Test
    void testUpdateEpicDescription() {
        Epic epicOne = imtm.createEpic("Test updateEpicDescription()",
                "Test description");
        final int epicId = epicOne.getId();
        String newDescription = "Updated description";

        imtm.updateEpicDescription(epicId, newDescription);
        final Epic savedEpic = imtm.getEpicById(epicId);

        assertEquals(newDescription, savedEpic.getDescription(), "Описания не совпадают.");
    }

    @Test
    void testEpicStatusWhenEmpty() {
        Epic epicOne = imtm.createEpic("Test epic status when empty",
                "Test description");
        final Status epicStatus = epicOne.getStatus();

        assertEquals(Status.NEW, epicStatus, "Статусы не совпадают.");
    }

    @Test
    void testEpicStatusWithNewSubtasks() {
        Epic epicOne = imtm.createEpic("Test epic status with new subtasks",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Subtask 1", "Subtask 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Subtask 2", "Subtask 2", epicId);
        final Status epicStatus = epicOne.getStatus();

        assertEquals(Status.NEW, epicStatus, "Статусы не совпадают.");
    }

    @Test
    void testEpicStatusWithDiffSubtasks() {
        Epic epicOne = imtm.createEpic("Test epic status with different subtasks",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Subtask 1", "Subtask 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Subtask 2", "Subtask 2", epicId);
        Subtask subtaskThree = imtm.createSubtask("Subtask 3", "Subtask 3", epicId);

        imtm.updateSubtaskStatus(subtaskTwo.getId(), Status.IN_PROGRESS);
        imtm.updateSubtaskStatus(subtaskThree.getId(), Status.DONE);
        final Status epicStatus = epicOne.getStatus();

        assertEquals(Status.IN_PROGRESS, epicStatus, "Статусы не совпадают.");
    }

    @Test
    void testEpicStatusWithDoneSubtasks() {
        Epic epicOne = imtm.createEpic("Test epic status with done subtasks",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Subtask 1", "Subtask 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Subtask 2", "Subtask 2", epicId);

        imtm.updateSubtaskStatus(subtaskOne.getId(), Status.DONE);
        imtm.updateSubtaskStatus(subtaskTwo.getId(), Status.DONE);
        final Status epicStatus = epicOne.getStatus();

        assertEquals(Status.DONE, epicStatus, "Статусы не совпадают.");
    }

    @Test
    void testEpicDeletion() {
        final int initCounter = imtm.getEntitiesCounter();
        Epic epicOne = imtm.createEpic("Test deleteEpic()", "Test description");
        final int epicId = epicOne.getId();

        imtm.deleteEpic(epicId);
        final int finalCounter = imtm.getEntitiesCounter();

        assertEquals(initCounter, finalCounter, "Эпик не удалён.");
    }

    @Test
    void testEpicWithSubtasksDeletion() {
        final int initCounter = imtm.getEntitiesCounter();
        Epic epicOne = imtm.createEpic("Test deleteEpic() with subtasks",
                "Test description");
        final int epicId = epicOne.getId();
        imtm.createSubtask("Title", "Description", epicId);
        imtm.createSubtask("Title", "Description", epicId);
        imtm.createSubtask("Title", "Description", epicId);

        imtm.deleteEpic(epicId);
        final int finalCounter = imtm.getEntitiesCounter();

        assertEquals(initCounter, finalCounter, "Эпик не удалён.");
    }

    @Test
    void testAllEpicsDeletion() {
        Epic epicOne = imtm.createEpic("Test deleteAllEpics() 1",
                "Test description 1");
        Epic epicTwo = imtm.createEpic("Test deleteAllEpics() 2",
                "Test description 2");
        Subtask subtaskOne = imtm.createSubtask("Subtask 1",
                "Subtask 1", epicOne.getId());
        Subtask subtaskTwo = imtm.createSubtask("Subtask 2",
                "Subtask 2", epicTwo.getId());

        imtm.deleteAllEpics();
        final int counter = imtm.getEntitiesCounter();

        assertEquals(0, counter, "Эпики не удалены.");
    }

    @Test
    void testEpicOutput() {
        Epic epicOne = imtm.createEpic("Test title", "Test description");
        final int epicId = epicOne.getId();
        final ArrayList<Integer> subtasksIds = epicOne.getSubtasksIds();
        String expectedOutput = "EPIC," + epicId + ",NEW,Test title,Test description," +
                subtasksIds.toString();
        String actualOutput = epicOne.toString();

        assertEquals(expectedOutput, actualOutput, "Эпик не удалён.");
    }
}