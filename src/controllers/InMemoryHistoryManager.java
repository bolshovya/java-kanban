package controllers;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> nodes = new HashMap<>();
    Node first; // head
    Node last; // tail


    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodes.containsKey(task.getId())) {
                remove(task.getId());
            }
            linkLast(task);
            nodes.put(task.getId(), last);
        }
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
}
