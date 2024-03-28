import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private static final TaskManager imtm = Managers.getDefault();

    @Test
    void testAddNewSubtask() {
        Epic epicOne = imtm.createEpic("Test createSubtask epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();
        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertNotNull(savedSubtask, "Сабтаска не найдена.");
        assertEquals(subtaskOne, savedSubtask, "Сабтаски не совпадают.");
    }

    @Test
    void testUpdateSubtaskTitle() {
        Epic epicOne = imtm.createEpic("Test updateSubtaskTitle epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();
        String newTitle = "Updated title";

        imtm.updateSubtaskTitle(subId, newTitle);
        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(newTitle, savedSubtask.getTitle(), "Заголовки не совпадают.");
    }

    @Test
    void testUpdateSubtaskDescription() {
        Epic epicOne = imtm.createEpic("Test updateSubtaskDescription epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();
        String newDescription = "Updated description";

        imtm.updateSubtaskDescription(subId, newDescription);
        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(newDescription, savedSubtask.getDescription(),
                "Описания не совпадают.");
    }

    @Test
    void testUpdateSubtaskStatus() {
        Epic epicOne = imtm.createEpic("Test updateSubtaskStatus epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();

        imtm.updateSubtaskStatus(subId, Status.IN_PROGRESS);
        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(Status.IN_PROGRESS, savedSubtask.getStatus(), "Статусы не совпадают.");
    }

    @Test
    void testSubtaskEmptyStartTime() {
        Epic epicOne = imtm.createEpic("Test SubtaskEmptyStartTime epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();

        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(LocalDateTime.of(2000, 10, 25, 19, 59),
                savedSubtask.getStartTime(), "Время старта не совпадает.");
    }

    @Test
    void testSubtaskStartTime() {
        Epic epicOne = imtm.createEpic("Test updateSubtaskStartTime epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();

        imtm.updateSubtaskStartTime(subId, LocalDateTime.of(2011, 11, 24,
                13, 25));
        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(LocalDateTime.of(2011, 11, 24, 13,
                25), savedSubtask.getStartTime(), "Время старта не совпадает.");
    }

    @Test
    void testSubtaskEmptyDuration() {
        Epic epicOne = imtm.createEpic("Test SubtaskEmptyDuration epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();

        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(0, savedSubtask.getDuration(), "Длительность не совпадает.");
    }

    @Test
    void testSubtaskDuration() {
        Epic epicOne = imtm.createEpic("Test updateSubtaskDuration epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();

        imtm.updateSubtaskDuration(subId, 53);
        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(53, savedSubtask.getDuration(), "Длительность не совпадает.");
    }

    @Test
    void testSubtaskEmptyEndTime() {
        Epic epicOne = imtm.createEpic("Test SubtaskEmptyEndTime epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();

        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(LocalDateTime.of(2000, 10, 25, 19, 59),
                savedSubtask.getEndTime(), "Время окончания не совпадает.");
    }

    @Test
    void testSubtaskEndTime() {
        Epic epicOne = imtm.createEpic("Test subtask.getEndTime() epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();


        imtm.updateSubtaskStartTime(subId, LocalDateTime.of(2011, 11, 24,
                13, 25));
        imtm.updateSubtaskDuration(subId, 15);
        final Subtask savedSubtask = imtm.getSubtaskById(subId);

        assertEquals(LocalDateTime.of(2011, 11, 24, 13,
                40), savedSubtask.getEndTime(), "Время окончания не совпадает.");
    }

    @Test
    void testSubtaskMoving() {
        Epic epicOne = imtm.createEpic("Test moveSubtask epic 1",
                "Test description");
        Epic epicTwo = imtm.createEpic("Test moveSubtask epic 2",
                "Test description");
        final int epicOneId = epicOne.getId();
        final int epicTwoId = epicTwo.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicOneId);
        final int subId = subtaskOne.getId();

        imtm.moveSubtask(subId, epicTwoId);
        final Subtask savedSubtask = imtm.getSubtaskById(subId);
        final int newEpicId = savedSubtask.getEpicId();

        assertEquals(epicTwoId, newEpicId, "Эпики не совпадают.");
    }

    @Test
    void testSubtaskDeletion() {
        int initCounter = imtm.getEntitiesCounter();
        Epic epicOne = imtm.createEpic("Test deleteSubtaskFromEpic epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title", "Description", epicId);
        final int subId = subtaskOne.getId();

        initCounter++;
        imtm.deleteSubtaskFromEpic(subId);
        final int finalCounter = imtm.getEntitiesCounter();

        assertEquals(initCounter, finalCounter, "Сабтаска не удалена.");
    }

    @Test
    void testAllSubtaskDeletion() {
        int initCounter = imtm.getEntitiesCounter();
        Epic epicOne = imtm.createEpic("Test deleteAllSubtasksFromEpic epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Title 1", "Description 1", epicId);
        Subtask subtaskTwo = imtm.createSubtask("Title 2", "Description 2", epicId);

        initCounter++;
        imtm.deleteAllSubtasksFromEpic(epicId);
        final int finalCounter = imtm.getEntitiesCounter();

        assertEquals(initCounter, finalCounter, "Сабтаски не удалены.");
    }

    @Test
    void testSubtaskOutput() {
        Epic epicOne = imtm.createEpic("Test deleteAllSubtasksFromEpic epic",
                "Test description");
        final int epicId = epicOne.getId();
        Subtask subtaskOne = imtm.createSubtask("Test title", "Test description",
                epicId);

        String expectedOutput = "SUBTASK," + subtaskOne.getId() + ",NEW,Test title," +
                "Test description,19:59 25-10-2000,0," + epicId;
        String actualOutput = subtaskOne.toString();

        assertEquals(expectedOutput, actualOutput, "Выводы не совпадают.");
    }
}