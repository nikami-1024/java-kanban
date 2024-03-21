package manager;

import model.*;

import java.util.LinkedList;

public class CSVParser {

    public static String[] taskParser(String str) {
        // микро-парсер, одно название
        String[] rawData = str.split(",");
        return rawData;
    }

    public static int[] historyParser(String str) {
        String[] strArray = str.split(",");
        int[] idArray = new int[strArray.length];

        for (int i = 0; i < strArray.length; i++) {
            idArray[i] = Integer.parseInt(strArray[i]);
        }

        return idArray;
    }

    public static String historyToString(LinkedList<Task> historyList) {
        StringBuilder historyStr = new StringBuilder();

        for (Task task : historyList) {
            int taskId = task.getId();
            if (historyStr.isEmpty()) {
                historyStr.append(taskId);
            } else {
                historyStr.append(",");
                historyStr.append(taskId);
            }
        }

        return historyStr.toString();
    }
}
