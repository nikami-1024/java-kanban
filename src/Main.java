public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        // создание и обработка тасок
        Task taskOne = tm.createTask("Купить молока", "Банановое, фундучное, по 1л.");
        Task taskTwo = tm.createTask("Помыть посуду", "Всю-всю, и сковородку тоже.");
        Task taskThree = tm.createTask("Купить игрушку коту", "Звенящий мячик");

        tm.printInfoTask(1);
        tm.updateTaskTitle(1, "Купить НЕмолока");
        tm.updateTaskDescription(1, "Банановое, миндальное, фундучное, по 1л.");
        tm.updateTaskStatus(1, Status.IN_PROGRESS);
        tm.updateTaskStatus(3, Status.DONE);

        tm.printInfoAllTasks();

        Epic epicOne = tm.createEpic("Купить куртку", "Зимнюю, тёплую, с карманами");
        Subtask subtaskA = tm.createSubtask("Выделить бюджет",
                "Посчитать максимально допустимую стоимость куртки", 4);
        Subtask subtaskB = tm.createSubtask("Выбрать цвет",
                "Синяя или голубая? Со световозвращайками?", 4);
        Subtask subtaskC = tm.createSubtask("Сходить в магазин",
                "Перемерить все куртки и купить самую крутую!", 4);

        Epic epicTwo = tm.createEpic("Почистить ноутбук", "А то перегревается и запылился =(");
        Subtask subtaskD = tm.createSubtask("Найти инструкцию", "И посмотреть картинки", 8);
        Subtask subtaskE = tm.createSubtask("Подготовить рабочее место", "Проверить, есть ли термопаста", 8);
        Subtask subtaskF = tm.createSubtask("Разобрать ноут", "Не потеряй винтики!", 8);
        Subtask subtaskG = tm.createSubtask("Почистить систему охлаждения", "И обновить термопасту", 8);
        Subtask subtaskH = tm.createSubtask("Собрать ноут", "Не осталось лишних винтиков?", 8);
        Subtask subtaskI = tm.createSubtask("Отпраздновать тортиком", "Съесть прямо ложкой из коробки", 8);

        // обновление эпика и сабтасок
        tm.printInfoEpic(4);
        tm.printInfoEpic(8);
        tm.printInfoEpicWithSubtasks(4);
        tm.updateEpicTitle(4, "Купить зимнюю куртку");
        tm.updateSubtaskStatus(6, Status.IN_PROGRESS);
        tm.updateSubtaskTitle(6, "Выбрать идеальный цвет");
        tm.updateSubtaskDescription(6, "Синюю, думать нечего");
        tm.updateSubtaskStatus(6, Status.DONE);
        tm.updateEpicDescription(4, "Синюю, тёплую, с карманами и световозвращайками");
        tm.updateSubtaskStatus(5, Status.DONE);
        tm.updateSubtaskStatus(7, Status.DONE);
        tm.printInfoEpicWithSubtasks(4);

        Task taskExample = tm.getTaskById(3);
        Epic epicExample = tm.getEpicById(4);
        Subtask subtaskExapmle = tm.getSubtaskById(14);
        System.out.println("\n" + taskExample.toString());
        System.out.println("\n" + epicExample.toString());
        System.out.println("\n" + subtaskExapmle.toString());

        tm.printInfoEpicWithSubtasks(8);
        tm.updateSubtaskStatus(9, Status.DONE);
        tm.updateSubtaskStatus(12, Status.IN_PROGRESS);
        tm.deleteSubtaskFromEpic(10);
        tm.moveSubtask(14, 4);

        tm.printInfoAllEpics();
        tm.printInfoAllEpicsWithSubtasks();

        tm.deleteTask(3);
        tm.deleteAllTasks();
        tm.deleteEpic(4);
        tm.deleteAllSubtasksFromEpic(8);
        tm.deleteAllEpics();
    }
}
