package manager;

import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList cll = new CustomLinkedList();

    // добавление сущности в историю
    @Override
    public void addToHistory(Task task) {

        // создание новой ноды с пустыми prev/next
        Node<Task> newNode = new Node<>(task.getId(), task, null, null);

        // если historyMap пустая - newNode становится единственной нодой
        // она же и head, и tail
        if (cll.historyMap.isEmpty()) {
            cll.setHead(newNode.getTaskId());
            cll.setTail(newNode.getTaskId());
            cll.historyMap.put(newNode.getTaskId(), newNode);

        } else if (cll.historyMap.size() == 1) {
            // если в historyMap есть единственная нода
            // проверить, что head - это не та же таска

            if (cll.getHead() != newNode.getTaskId()) {
                // достать головную ноду из мапы
                // добавить в неё next
                // перезаписать головную ноду в мапе
                Node<Task> newHead = (Node<Task>) cll.historyMap.get(cll.getHead());
                newHead.setNext(newNode.getTaskId());
                cll.historyMap.put(newHead.getTaskId(), newHead);

                // добавить в новую ноду prev на голову
                // перезаписать tail
                // добавить новую ноду в мапу
                newNode.setPrev(newHead.getTaskId());
                cll.setTail(newNode.getTaskId());
                cll.historyMap.put(newNode.getTaskId(), newNode);
            }

        } else if (cll.historyMap.size() > 1) {
            // если в historyMap больше одной ноды

            // таска уже была просмотрена ранее?
            boolean isAlreadyViewed = cll.historyMap.containsKey(newNode.getTaskId());
            // таска - хвостик?
            boolean isTail = cll.getTail() == newNode.getTaskId() ? true : false;

            // если уже в мапе и не хвостик - удалить через remove()
            // и добавить в хвостик
            if (isAlreadyViewed && !isTail) {
                remove(newNode.getTaskId());
                Node<Task> obsTail = (Node<Task>) cll.historyMap.get(cll.getTail());
                obsTail.setNext(newNode.getTaskId());
                newNode.setPrev(cll.getTail());
                cll.setTail(newNode.getTaskId());
                cll.historyMap.put(obsTail.getTaskId(), obsTail);
                cll.historyMap.put(newNode.getTaskId(), newNode);

            } else if (!isAlreadyViewed) {
                // если ещё не просмотрена - просто добавить в хвостик
                Node<Task> obsTail = (Node<Task>) cll.historyMap.get(cll.getTail());
                obsTail.setNext(newNode.getTaskId());
                newNode.setPrev(cll.getTail());
                cll.setTail(newNode.getTaskId());
                cll.historyMap.put(obsTail.getTaskId(), obsTail);
                cll.historyMap.put(newNode.getTaskId(), newNode);
            }
        }
    }

    // удаление сущности из истории
    public void remove(int taskId) {
        // взять ноду из мапы
        // проверить на соответствие голове и хвосту
        Node<Task> taskNode = (Node<Task>) cll.historyMap.get(taskId);
        boolean isHead = Objects.equals(cll.getHead(), taskNode.getTaskId());
        boolean isTail = Objects.equals(cll.getTail(), taskNode.getTaskId());

        if (!isHead && !isTail) {
            // если не голова и не хвост
            // перезаписать prev/next у соседних нод
            // обновить их в мапе
            // удалить исходную ноду из мапы
            Node<Task> prevNode = (Node<Task>) cll.historyMap.get(taskNode.getPrevId());
            Node<Task> nextNode = (Node<Task>) cll.historyMap.get(taskNode.getNextId());
            prevNode.setNext(taskNode.getNextId());
            nextNode.setPrev(taskNode.getPrevId());
            cll.historyMap.put(prevNode.getTaskId(), prevNode);
            cll.historyMap.put(nextNode.getTaskId(), nextNode);
            cll.historyMap.remove(taskNode.getTaskId());

        } else if (isHead && isTail) {
            // если и голова, и хвост
            // то обнулить headId/tailId
            // и почистить мапу
            cll.setHead(null);
            cll.setTail(null);
            cll.historyMap.clear();

        } else if (isHead) {
            // если голова
            // перезаписать prev на null у следующей ноды
            // обновить её в мапе
            // обновить headId
            // удалить исходную ноду из мапы
            Node<Task> nextNode = (Node<Task>) cll.historyMap.get(taskNode.getNextId());
            nextNode.setPrev(null);
            cll.historyMap.put(nextNode.getTaskId(), nextNode);
            cll.setHead(nextNode.getTaskId());
            cll.historyMap.remove(taskNode.getTaskId());

        } else if (isTail) {
            // если хвост
            // перезаписать next на null у предыдущей ноды
            // обновить её в мапе
            // обновить tailId
            // удалить исходную ноду из мапы
            Node<Task> prevNode = (Node<Task>) cll.historyMap.get(taskNode.getPrevId());
            prevNode.setNext(null);
            cll.historyMap.put(prevNode.getTaskId(), prevNode);
            cll.setTail(prevNode.getTaskId());
            cll.historyMap.remove(taskNode.getTaskId());
        }
    }

    // получение истории в виде LinkedList
    // от свежего просмотра к старому
    @Override
    public LinkedList<Task> getHistory() {
        // брать из хэшмапы хвостовую ноду и идти по prev до head
        LinkedList<Task> historyList = new LinkedList<>();
        Integer prevTaskId = cll.getTail();

        while (prevTaskId != cll.getHead()) {
            Node<Task> node = (Node<Task>) cll.historyMap.get(prevTaskId);
            Task task = node.getData();
            historyList.add(task);
            prevTaskId = node.getPrevId();
        }

        return historyList;
    }

    // узел списка истории

    // prev/next храню как Integer taskId
    // так как при использовании Node node не вывожу
    // рекурсивное одновременное обновление нод внутри других нод
    public class Node<Task> {
        public int taskId;
        public Task data;
        public Integer prevId;
        public Integer nextId;

        public Node(int taskId, Task task, Integer prevId, Integer nextId) {
            this.taskId = taskId;
            this.data = task;
            this.prevId = prevId;
            this.nextId = nextId;
        }

        public Integer getTaskId() {
            return taskId;
        }

        public Integer getPrevId() {
            return prevId;
        }

        public Integer getNextId() {
            return nextId;
        }

        public Task getData() {
            return data;
        }

        public void setPrev(Integer prevId) {
            this.prevId = prevId;
        }

        public void setNext(Integer nextId) {
            this.nextId = nextId;
        }
    }

    // связный список для хранения истории в форме узлов
    // выглядит так, что всё это добро может жить просто в классе InMemoryHistoryManager
    // без вынесения в собственный класс CustomLinkedList<Task>

    // headId/tailId храню как Integer taskId
    // так как при использовании Node node не вывожу
    // рекурсивное одновременное обновление нод внутри других нод
    public class CustomLinkedList<Task> {
        Integer headId;
        Integer tailId;

        Map<Integer, Node<Task>> historyMap = new HashMap<>();

        public void setHead(Integer taskId) {
            this.headId = taskId;
        }

        public void setTail(Integer taskId) {
            this.tailId = taskId;
        }

        public Integer getHead() {
            return headId;
        }

        public Integer getTail() {
            return tailId;
        }
    }
}
