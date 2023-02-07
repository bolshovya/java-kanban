package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        int historySize = 10;
        while (history.size() > historySize) {
            history.remove(0);
        }
        return history;
    }

    public List<Task> getHistoryList() {
        return history;
    }

    @Override
    public void setHistory(List<Task> list) {
        this.history = list;
    }


}
