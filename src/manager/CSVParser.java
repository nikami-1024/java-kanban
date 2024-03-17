package manager;

import model.*;

public class CSVParser {

    public static Task taskParser(String str) {

        // здесь из-за дополнительного создания таски рушится
        // вся блестящая логика последовательных id
        // а может быть не только здесь =(

        String[] rawData = str.split(",");

        Task rawTask;
        Status status;
        int id = Integer.parseInt(rawData[1]);

        if (rawData[2].equals(Status.NEW.toString())) {
            status = Status.NEW;
        } else if (rawData[2].equals(Status.IN_PROGRESS.toString())) {
            status = Status.IN_PROGRESS;
        } else {
            status = Status.DONE;
        }

        if (rawData[0].equals(TaskType.TASK.toString())) {
            rawTask = new Task(rawData[3], rawData[4]);
            rawTask.setId(id);
            if (status != Status.NEW) {
                rawTask.setStatus(status);
            }
        } else if (rawData[0].equals(TaskType.EPIC.toString())) {
            rawTask = new Epic(rawData[3], rawData[4]);
            rawTask.setId(id);
        } else {
            rawTask = new Subtask(rawData[3], rawData[4], Integer.parseInt(rawData[5]));
            rawTask.setId(id);
            if (status != Status.NEW) {
                rawTask.setStatus(status);
            }
        }

        return rawTask;
    }

    // это вообще сойдёт за самостоятельный парсер? он же плюшевый!
    public static int[] historyParser(String str) {
        String[] strArray = str.split(",");
        int[] idArray = new int[strArray.length];

        for (int i = 0; i < strArray.length; i++) {
            idArray[i] = Integer.parseInt(strArray[i]);
        }

        return idArray;
    }
}
