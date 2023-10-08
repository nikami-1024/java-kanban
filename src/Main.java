import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager imtm = Managers.getDefault();
        HistoryManager hm = Managers.getDefaultHistory();

        // создание и обработка тасок
        Task taskOne = imtm.createTask("Купить молока", "Банановое, фундучное, по 1л.");
        Task taskTwo = imtm.createTask("Помыть посуду", "Всю-всю, и сковородку тоже.");
        Task taskThree = imtm.createTask("Купить игрушку коту", "Звенящий мячик");

        imtm.printInfoTask(1);
        imtm.updateTaskTitle(1, "Купить НЕмолока");
        imtm.updateTaskDescription(1, "Банановое, миндальное, фундучное, по 1л.");
        imtm.updateTaskStatus(1, Status.IN_PROGRESS);
        imtm.updateTaskStatus(3, Status.DONE);

        imtm.printInfoAllTasks();

        // создание эпиков и сабтасок
        Epic epicOne = imtm.createEpic("Купить куртку", "Зимнюю, тёплую, с карманами");
        Subtask subtaskA = imtm.createSubtask("Выделить бюджет",
                "Посчитать максимально допустимую стоимость куртки", 4);
        Subtask subtaskB = imtm.createSubtask("Выбрать цвет",
                "Синяя или голубая? Со световозвращайками?", 4);
        Subtask subtaskC = imtm.createSubtask("Сходить в магазин",
                "Перемерить все куртки и купить самую крутую!", 4);

        Epic epicTwo = imtm.createEpic("Почистить ноутбук", "А то перегревается и запылился =(");
        Subtask subtaskD = imtm.createSubtask("Найти инструкцию",
                "И посмотреть картинки", 8);
        Subtask subtaskE = imtm.createSubtask("Подготовить рабочее место",
                "Проверить, есть ли термопаста", 8);
        Subtask subtaskF = imtm.createSubtask("Разобрать ноут",
                "Не потеряй винтики!", 8);
        Subtask subtaskG = imtm.createSubtask("Почистить систему охлаждения",
                "И обновить термопасту", 8);
        Subtask subtaskH = imtm.createSubtask("Собрать ноут",
                "Не осталось лишних винтиков?", 8);
        Subtask subtaskI = imtm.createSubtask("Отпраздновать тортиком",
                "Съесть прямо ложкой из коробки", 8);

        // обновление эпика и сабтасок
        imtm.printInfoEpic(4);
        imtm.printInfoEpic(8);
        imtm.printInfoEpicWithSubtasks(4);
        imtm.updateEpicTitle(4, "Купить зимнюю куртку");
        imtm.updateSubtaskStatus(6, Status.IN_PROGRESS);
        imtm.updateSubtaskTitle(6, "Выбрать идеальный цвет");
        imtm.updateSubtaskDescription(6, "Синюю, думать нечего");
        imtm.updateSubtaskStatus(6, Status.DONE);
        imtm.updateEpicDescription(4, "Синюю, тёплую, с карманами и световозвращайками");
        imtm.updateSubtaskStatus(5, Status.DONE);
        imtm.updateSubtaskStatus(7, Status.DONE);
        imtm.printInfoEpicWithSubtasks(4);

        // проверка вывода в строку
        Task taskExample = imtm.getTaskById(3);
        hm.addToHistory(taskExample);
        Epic epicExample = imtm.getEpicById(4);
        hm.addToHistory(epicExample);
        Subtask subtaskExapmle = imtm.getSubtaskById(14);
        hm.addToHistory(subtaskExapmle);
        System.out.println("\n" + taskExample.toString());
        System.out.println("\n" + epicExample.toString());
        System.out.println("\n" + subtaskExapmle.toString());

        // проверка всех комбинаций статусов
        imtm.printInfoEpicWithSubtasks(8);
        imtm.updateSubtaskStatus(9, Status.DONE);
        imtm.updateSubtaskStatus(12, Status.IN_PROGRESS);
        imtm.deleteSubtaskFromEpic(10);
        imtm.moveSubtask(14, 4);

        imtm.printInfoAllEpics();
        imtm.printInfoAllEpicsWithSubtasks();

        // тест истории
        System.out.println("\nВывод истории просмотров:");
        for (Task entity : hm.getHistory()) {
            System.out.println(entity.toString());
        }

        taskExample = imtm.getTaskById(1);
        hm.addToHistory(taskExample);
        taskExample = imtm.getTaskById(1);
        hm.addToHistory(taskExample);
        taskExample = imtm.getTaskById(1);
        hm.addToHistory(taskExample);
        taskExample = imtm.getTaskById(1);
        hm.addToHistory(taskExample);
        taskExample = imtm.getTaskById(1);
        hm.addToHistory(taskExample);
        taskExample = imtm.getTaskById(1);
        hm.addToHistory(taskExample);
        taskExample = imtm.getTaskById(1);
        hm.addToHistory(taskExample);
        taskExample = imtm.getTaskById(2);
        hm.addToHistory(taskExample);

        System.out.println("\nИстория просмотров:");
        for (Task entity : hm.getHistory()) {
            System.out.println(entity.toString());
        }

        // очищение хранилищ
        imtm.deleteTask(3);
        imtm.deleteAllTasks();
        imtm.deleteEpic(4);
        imtm.deleteAllSubtasksFromEpic(8);
        imtm.deleteAllEpics();
    }
}
