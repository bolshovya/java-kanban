package controllers;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> nodes = new HashMap<>();
    Node first; // head
    Node last; // tail
    //private int size = 0;




    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodes.containsKey(task.getId())) {
                remove(task.getId());
            }
            linkLast(task);
            nodes.put(task.getId(), last);
        }


        /*
        if (task == null) {
            return;
        }
        remove(task.getId());
        linkLast(task);
        nodes.put(task.getId(), last); */
    }

    public List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node currentNode = first;
        while (currentNode != null) {
            history.add(currentNode.getTask());
            currentNode = currentNode.next;
        }
        return history;
    }

    public void linkLast(Task task) {
        Node l = last;
        Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }

        // создать новую ноду (newNode) через конструктор
        // если Ноды нет, то новую ноду положим в first
        // а еслт уже какие-то Ноды есть, то нужно к последней Ноде привязать новосозданную last.next = newNode
        // всегда в конце last = новосозданная Нода
    }

    @Override
    public void remove(int id) {
        if (nodes.containsKey(id) && nodes.get(id) != null) {
            Node removeNode = nodes.get(id);
            Node prevNode = removeNode.getPrev();
            Node nextNode = removeNode.getNext();
            if (prevNode == null && nextNode == null) {
                first = null;
                last = null;
            } else if (prevNode == null) {
                nextNode.prev = null;
                first = nextNode;
            } else if (nextNode == null) {
                prevNode.next = null;
                last = prevNode;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }

        /*
        Node node = nodes.get(id);
        if (node == null) {
            return;
        } else {
            if (node.prev != null) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            } else if (node.next == null) {
                last = node.prev;
                node.prev = null;
            } else if (node.prev == null) {
                first = node.next;
                node.next.prev = null;
            }
        }

        */

        // получить из Мапы Ноду (node) по АЙДи Таска
        // есть такой ноды неТ, то и удалять нечего, выходим
        // 1. если Нода не Первая,
        // перепривязать указатель node.prev.next = node.next
        // 1.1 если node.next == null, тогда переопределим last
        // 1.2 если node.next != null, тогда нужно перепривязать node.next.prev = node.prev
        // 2. если удаляемая Нода ПЕРВАЯ
        // переопеределить first
        // 2.1 если новый first == null , то и переопределить last (null)
        // 2.2 если новый first != null, то тогда у новой first.prev = null
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node currentNode = first;
        while (currentNode != null) {
            history.add(currentNode.getTask());
            currentNode = currentNode.next;
        }
        return history;
    }


    private static class Node {
        private Task item;
        private Node next;
        private Node prev;

        Node(Node prev, Task task, Node next) {
            this.item = task;
            this.next = next;
            this.prev = prev;
        }

        public Task getTask() {
            return item;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }
    }







/*

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() > 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }






    class CustomLinkedList<T> {
        Node<T> first;
        Node<T> last;
        private int size = 0;
        Map <Integer, Node> nodes = new HashMap<>();

        public void add(Task task) {
            if (task == null) {
                return;
            }
            remove(task.id);
            linkLast(task);
            nodes.put(task.id, last);
        }

        public List<Task> getTasks() {
            List<Task> history = new ArrayList<>();
            Node currentNode = first;
            while (currentNode != null) {
                history.put()
                currentNode = currentNode.next;
            }
            return
        }

        public void linkLast(Task task) {

        }


    }
    */

}
