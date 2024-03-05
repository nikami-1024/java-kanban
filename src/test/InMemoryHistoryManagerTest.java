import manager.Managers;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void testAddOneTaskInHistory() {
        TaskManager imtm1 = Managers.getDefault();
        Task taskOne = imtm1.createTask("Test getHistory()", "Test description");
        final int taskId = taskOne.getId();
        final Task savedTask = imtm1.getTaskById(taskId);
        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm1.getHistory();

        expectedHistory.add(taskOne);

        assertEquals(expectedHistory, actualHistory, "История не совпадает.");
    }

    @Test
    void testTwoTasksOrderInHistory() {
        TaskManager imtm2 = Managers.getDefault();
        Task taskOne = imtm2.createTask("Test task 1", "Test description");
        Task taskTwo = imtm2.createTask("Test task 2", "Test description");
        final int taskIdOne = taskOne.getId();
        final int taskIdTwo = taskTwo.getId();
        final Task savedTaskOne = imtm2.getTaskById(taskIdOne);
        final Task savedTaskTwo = imtm2.getTaskById(taskIdTwo);
        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm2.getHistory();

        expectedHistory.add(taskTwo);
        expectedHistory.add(taskOne);

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

    @Test
    void testTasksOrderInHistory() {
        TaskManager imtm3 = Managers.getDefault();
        Task taskOne = imtm3.createTask("Test task 1", "Test description");
        Task taskTwo = imtm3.createTask("Test task 2", "Test description");
        Task taskThree = imtm3.createTask("Test task 3", "Test description");
        Task taskFour = imtm3.createTask("Test task 4", "Test description");

        final int taskIdOne = taskOne.getId();
        final int taskIdTwo = taskTwo.getId();
        final int taskIdThree = taskThree.getId();
        final int taskIdFour = taskFour.getId();

        final Task savedTaskThree = imtm3.getTaskById(taskIdThree);
        final Task savedTaskOne = imtm3.getTaskById(taskIdOne);
        final Task savedTaskFour = imtm3.getTaskById(taskIdFour);
        final Task savedTaskTwo = imtm3.getTaskById(taskIdTwo);

        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm3.getHistory();

        expectedHistory.add(taskTwo);
        expectedHistory.add(taskFour);
        expectedHistory.add(taskOne);
        expectedHistory.add(taskThree);

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

    @Test
    void testTasksOrderInHistoryWithRepeatsFromHead() {
        TaskManager imtm4 = Managers.getDefault();
        Task taskOne = imtm4.createTask("Test task 1", "Test description");
        Task taskTwo = imtm4.createTask("Test task 2", "Test description");
        Task taskThree = imtm4.createTask("Test task 3", "Test description");
        Task taskFour = imtm4.createTask("Test task 4", "Test description");

        final int taskIdOne = taskOne.getId();
        final int taskIdTwo = taskTwo.getId();
        final int taskIdThree = taskThree.getId();
        final int taskIdFour = taskFour.getId();

        final Task savedTaskOne = imtm4.getTaskById(taskIdOne);
        final Task savedTaskTwo = imtm4.getTaskById(taskIdTwo);
        final Task savedTaskThree = imtm4.getTaskById(taskIdThree);
        final Task savedTaskFour = imtm4.getTaskById(taskIdFour);
        final Task savedTaskFive = imtm4.getTaskById(taskIdOne);

        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm4.getHistory();

        expectedHistory.add(taskOne);
        expectedHistory.add(taskFour);
        expectedHistory.add(taskThree);
        expectedHistory.add(taskTwo);

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

    @Test
    void testTasksOrderInHistoryWithRepeatsFromTail() {
        TaskManager imtm5 = Managers.getDefault();
        Task taskOne = imtm5.createTask("Test task 1", "Test description");
        Task taskTwo = imtm5.createTask("Test task 2", "Test description");
        Task taskThree = imtm5.createTask("Test task 3", "Test description");
        Task taskFour = imtm5.createTask("Test task 4", "Test description");

        final int taskIdOne = taskOne.getId();
        final int taskIdTwo = taskTwo.getId();
        final int taskIdThree = taskThree.getId();
        final int taskIdFour = taskFour.getId();

        final Task savedTaskOne = imtm5.getTaskById(taskIdOne);
        final Task savedTaskTwo = imtm5.getTaskById(taskIdTwo);
        final Task savedTaskThree = imtm5.getTaskById(taskIdThree);
        final Task savedTaskFour = imtm5.getTaskById(taskIdFour);
        final Task savedTaskFive = imtm5.getTaskById(taskIdFour);

        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm5.getHistory();

        expectedHistory.add(taskFour);
        expectedHistory.add(taskThree);
        expectedHistory.add(taskTwo);
        expectedHistory.add(taskOne);

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

    @Test
    void testTasksOrderInHistoryWithRepeatsFromCenter() {
        TaskManager imtm6 = Managers.getDefault();
        Task taskOne = imtm6.createTask("Test task 1", "Test description");
        Task taskTwo = imtm6.createTask("Test task 2", "Test description");
        Task taskThree = imtm6.createTask("Test task 3", "Test description");
        Task taskFour = imtm6.createTask("Test task 4", "Test description");

        final int taskIdOne = taskOne.getId();
        final int taskIdTwo = taskTwo.getId();
        final int taskIdThree = taskThree.getId();
        final int taskIdFour = taskFour.getId();

        final Task savedTaskOne = imtm6.getTaskById(taskIdOne);
        final Task savedTaskTwo = imtm6.getTaskById(taskIdTwo);
        final Task savedTaskThree = imtm6.getTaskById(taskIdThree);
        final Task savedTaskFour = imtm6.getTaskById(taskIdFour);
        final Task savedTaskFive = imtm6.getTaskById(taskIdTwo);

        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm6.getHistory();

        expectedHistory.add(taskTwo);
        expectedHistory.add(taskFour);
        expectedHistory.add(taskThree);
        expectedHistory.add(taskOne);

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

    @Test
    void testHeadTaskRemovingFromHistory() {
        TaskManager imtm7 = Managers.getDefault();
        Task taskOne = imtm7.createTask("Test task 1", "Test description");
        Task taskTwo = imtm7.createTask("Test task 2", "Test description");
        Task taskThree = imtm7.createTask("Test task 3", "Test description");
        Task taskFour = imtm7.createTask("Test task 4", "Test description");

        final int taskIdOne = taskOne.getId();
        final int taskIdTwo = taskTwo.getId();
        final int taskIdThree = taskThree.getId();
        final int taskIdFour = taskFour.getId();

        final Task savedTaskOne = imtm7.getTaskById(taskIdOne);
        final Task savedTaskTwo = imtm7.getTaskById(taskIdTwo);
        final Task savedTaskThree = imtm7.getTaskById(taskIdThree);
        final Task savedTaskFour = imtm7.getTaskById(taskIdFour);

        imtm7.deleteTask(taskIdOne);

        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm7.getHistory();

        expectedHistory.add(taskFour);
        expectedHistory.add(taskThree);
        expectedHistory.add(taskTwo);

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

    @Test
    void testTailTaskRemovingFromHistory() {
        TaskManager imtm8 = Managers.getDefault();
        Task taskOne = imtm8.createTask("Test task 1", "Test description");
        Task taskTwo = imtm8.createTask("Test task 2", "Test description");
        Task taskThree = imtm8.createTask("Test task 3", "Test description");
        Task taskFour = imtm8.createTask("Test task 4", "Test description");

        final int taskIdOne = taskOne.getId();
        final int taskIdTwo = taskTwo.getId();
        final int taskIdThree = taskThree.getId();
        final int taskIdFour = taskFour.getId();

        final Task savedTaskOne = imtm8.getTaskById(taskIdOne);
        final Task savedTaskTwo = imtm8.getTaskById(taskIdTwo);
        final Task savedTaskThree = imtm8.getTaskById(taskIdThree);
        final Task savedTaskFour = imtm8.getTaskById(taskIdFour);

        imtm8.deleteTask(taskIdFour);

        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm8.getHistory();

        expectedHistory.add(taskThree);
        expectedHistory.add(taskTwo);
        expectedHistory.add(taskOne);

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

    @Test
    void testCenterTaskRemovingFromHistory() {
        TaskManager imtm9 = Managers.getDefault();
        Task taskOne = imtm9.createTask("Test task 1", "Test description");
        Task taskTwo = imtm9.createTask("Test task 2", "Test description");
        Task taskThree = imtm9.createTask("Test task 3", "Test description");
        Task taskFour = imtm9.createTask("Test task 4", "Test description");

        final int taskIdOne = taskOne.getId();
        final int taskIdTwo = taskTwo.getId();
        final int taskIdThree = taskThree.getId();
        final int taskIdFour = taskFour.getId();

        final Task savedTaskOne = imtm9.getTaskById(taskIdOne);
        final Task savedTaskTwo = imtm9.getTaskById(taskIdTwo);
        final Task savedTaskThree = imtm9.getTaskById(taskIdThree);
        final Task savedTaskFour = imtm9.getTaskById(taskIdFour);

        imtm9.deleteTask(taskIdTwo);

        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm9.getHistory();

        expectedHistory.add(taskFour);
        expectedHistory.add(taskThree);
        expectedHistory.add(taskOne);

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

    @Test
    void testSingleTaskRemovingFromHistory() {
        TaskManager imtm10 = Managers.getDefault();
        Task taskOne = imtm10.createTask("Test task 1", "Test description");

        final int taskIdOne = taskOne.getId();

        final Task savedTaskOne = imtm10.getTaskById(taskIdOne);

        imtm10.deleteTask(taskIdOne);

        final LinkedList<Task> expectedHistory = new LinkedList<>();
        final LinkedList<Task> actualHistory = imtm10.getHistory();

        assertEquals(expectedHistory, actualHistory, "Порядок задач в истории не совпадает.");
    }

}