import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager imtm = Managers.getDefault();

        System.out.println("\nСоздание тасок:");
        Task taskOne = imtm.createTask("Купить молока", "Банановое, фундучное, по 1л.");
        Task taskTwo = imtm.createTask("Помыть посуду", "Всю-всю, и сковородку тоже.");
        Task taskThree = imtm.createTask("Купить игрушку коту", "Звенящий мячик");

        System.out.println("\nОбновление тасок:");
        System.out.println("\n" + taskOne.toString());

        imtm.updateTaskTitle(1, "Купить НЕмолока");
        imtm.updateTaskDescription(1, "Банановое, миндальное, фундучное, по 1л.");
        imtm.updateTaskStatus(1, Status.IN_PROGRESS);
        imtm.updateTaskStatus(3, Status.DONE);

        System.out.println("\n" + imtm.getTaskById(1).toString());
        System.out.println(imtm.getTaskById(2).toString());
        System.out.println(imtm.getTaskById(3).toString());

        System.out.println("\nСоздание эпиков и сабтасок:");
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

        System.out.println("\nОбновление эпика и сабтасок:");
        System.out.println("\n" + imtm.getEpicById(8).toString());
        System.out.println(imtm.getEpicById(4).toString());
        System.out.println(imtm.getSubtaskById(5).toString());
        System.out.println(imtm.getSubtaskById(6).toString());
        System.out.println(imtm.getSubtaskById(7).toString());

        imtm.updateEpicTitle(4, "Купить зимнюю куртку");
        imtm.updateSubtaskStatus(6, Status.IN_PROGRESS);
        imtm.updateSubtaskTitle(6, "Выбрать идеальный цвет");
        imtm.updateSubtaskDescription(6, "Синюю, думать нечего");
        imtm.updateSubtaskStatus(6, Status.DONE);
        imtm.updateEpicDescription(4, "Синюю, тёплую, с карманами и световозвращайками");
        imtm.updateSubtaskStatus(5, Status.DONE);
        imtm.updateSubtaskStatus(7, Status.DONE);

        System.out.println("\n" + imtm.getEpicById(4).toString());
        System.out.println(imtm.getSubtaskById(5).toString());
        System.out.println(imtm.getSubtaskById(6).toString());
        System.out.println(imtm.getSubtaskById(7).toString());

        System.out.println("\nПроверка всех комбинаций статусов:");
        System.out.println("\n" + imtm.getEpicById(4).toString());
        System.out.println(imtm.getEpicById(8).toString());

        imtm.updateSubtaskStatus(9, Status.DONE);
        imtm.updateSubtaskStatus(12, Status.IN_PROGRESS);
        imtm.deleteSubtaskFromEpic(10);
        imtm.moveSubtask(14, 4);

        System.out.println("\n" + imtm.getEpicById(4).toString());
        System.out.println(imtm.getEpicById(8).toString());

        System.out.println("\nТест истории:");
        imtm.getHistory();

        taskOne = imtm.getTaskById(1);
        taskOne = imtm.getTaskById(1);
        taskOne = imtm.getTaskById(1);
        taskOne = imtm.getTaskById(1);
        taskOne = imtm.getTaskById(1);
        taskOne = imtm.getTaskById(1);
        taskOne = imtm.getTaskById(1);
        taskTwo = imtm.getTaskById(2);

        imtm.getHistory();

        System.out.println("\nОчищение хранилищ:");
        imtm.deleteTask(3);
        imtm.deleteAllTasks();
        imtm.deleteEpic(4);
        imtm.deleteAllSubtasksFromEpic(8);
        imtm.deleteAllEpics();
    }
}
