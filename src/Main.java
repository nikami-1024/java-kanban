public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        // создание и обработка тасок
        tm.createTask("Купить молока", "Банановое, фундучное, по 1л.");
        tm.createTask("Помыть посуду", "Всю-всю, и сковородку тоже.");
        tm.createTask("Купить игрушку коту", "Звенящий мячик");

        tm.printInfoTask(1);
        tm.updateTaskTitle(1, "Купить НЕмолока");
        tm.updateTaskDescription(1, "Банановое, миндальное, фундучное, по 1л.");
        tm.updateTaskStatus(1, Status.IN_PROGRESS);
        tm.updateTaskStatus(3, Status.DONE);

        tm.printInfoAllTasks();
        tm.deleteTask(3);
        tm.deleteAllTasks();
        tm.printInfoAllTasks();

        tm.createEpic("Купить куртку", "Зимнюю, тёплую, с карманами");
        tm.createSubtask("Выделить бюджет",
                "Посчитать максимально допустимую стоимость куртки", 4);
        tm.createSubtask("Выбрать цвет",
                "Синяя или голубая? Со световозвращайками?", 4);
        tm.createSubtask("Сходить в магазин",
                "Перемерить все куртки и купить самую крутую!", 4);

        tm.createEpic("Почистить ноутбук", "А то перегревается и запылился =(");
        tm.createSubtask("Найти инструкцию", "И посмотреть картинки", 8);
        tm.createSubtask("Подготовить рабочее место", "Проверить, есть ли термопаста", 8);
        tm.createSubtask("Разобрать ноут", "Не потеряй винтики!", 8);
        tm.createSubtask("Почистить систему охлаждения", "И обновить термопасту", 8);
        tm.createSubtask("Собрать ноут", "Не осталось лишних винтиков?", 8);
        tm.createSubtask("Отпраздновать тортиком", "Съесть прямо ложкой из коробки", 8);

        // обновление эпика и сабтасок
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

        tm.printInfoEpicWithSubtasks(8);
        tm.updateSubtaskStatus(9, Status.DONE);
        tm.deleteSubtaskFromEpic(10);
        tm.moveSubtask(14, 4);

        tm.printInfoAllEpics();
        tm.printInfoAllEpicsWithSubtasks();

        tm.deleteEpic(4);
        tm.deleteAllSubtasksFromEpic(8);
        tm.deleteAllEpics();
    }
}
