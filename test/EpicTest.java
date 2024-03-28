import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
    void testEpicEmptyStartTime() {
        Epic epicOne = imtm.createEpic("Test EpicEmptyStartTime epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title 1", "Description 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Title 2", "Description 2", epicId);

        final Epic savedEpic = imtm.getEpicById(epicId);

        assertEquals(LocalDateTime.of(2000, 10, 25, 19, 59),
                savedEpic.getStartTime(), "Время старта не совпадает.");
    }

    @Test
    void testEpicStartTime() {
        Epic epicOne = imtm.createEpic("Test EpicStartTime epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title 1", "Description 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Title 2", "Description 2", epicId);
        final int subOneId = subtaskOne.getId();
        final int subTwoId = subtaskTwo.getId();

        imtm.updateSubtaskStartTime(subOneId, LocalDateTime.of(2012, 12,
                23, 14, 26));
        imtm.updateSubtaskStartTime(subTwoId, LocalDateTime.of(2012, 12,
                23, 14, 27));
        final Epic savedEpic = imtm.getEpicById(epicId);

        assertEquals(LocalDateTime.of(2012, 12, 23, 14,
                26), savedEpic.getStartTime(), "Время старта не совпадает.");
    }

    @Test
    void testEpicEmptyDuration() {
        Epic epicOne = imtm.createEpic("Test EpicEmptyDuration epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title 1", "Description 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Title 2", "Description 2", epicId);

        final Epic savedEpic = imtm.getEpicById(epicId);

        assertEquals(0, savedEpic.getDuration(), "Длительность не совпадает.");
    }

    @Test
    void testEpicDuration() {
        Epic epicOne = imtm.createEpic("Test EpicDuration epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title 1", "Description 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Title 2", "Description 2", epicId);
        final int subOneId = subtaskOne.getId();
        final int subTwoId = subtaskTwo.getId();

        imtm.updateSubtaskDuration(subOneId, 25);
        imtm.updateSubtaskDuration(subTwoId, 26);
        final Epic savedEpic = imtm.getEpicById(epicId);

        assertEquals(51, savedEpic.getDuration(), "Длительность не совпадает.");
    }

    @Test
    void testEpicEmptyEndTime() {
        Epic epicOne = imtm.createEpic("Test EpicEmptyEndTime epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title 1", "Description 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Title 2", "Description 2", epicId);

        final Epic savedEpic = imtm.getEpicById(epicId);

        assertEquals(LocalDateTime.of(2000, 10, 25, 19, 59),
                savedEpic.getEndTime(), "Время окончания не совпадает.");
    }

    @Test
    void testEpicEndTime() {
        Epic epicOne = imtm.createEpic("Test EpicEndTime epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title 1", "Description 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Title 2", "Description 2", epicId);
        final int subOneId = subtaskOne.getId();
        final int subTwoId = subtaskTwo.getId();

        imtm.updateSubtaskStartTime(subOneId, LocalDateTime.of(2015, 5, 5,
                18, 40));
        imtm.updateSubtaskStartTime(subTwoId, LocalDateTime.of(2015, 6, 6,
                19, 40));
        imtm.updateSubtaskDuration(subOneId, 56);
        imtm.updateSubtaskDuration(subTwoId, 85);
        final Epic savedEpic = imtm.getEpicById(epicId);

        assertEquals(LocalDateTime.of(2015, 6, 6, 21, 5),
                savedEpic.getEndTime(), "Время окончания не совпадает.");
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
                "19:59 25-10-2000,0," + subtasksIds.toString();
        String actualOutput = epicOne.toString();

        assertEquals(expectedOutput, actualOutput, "Выводы не совпадают.");
    }
}