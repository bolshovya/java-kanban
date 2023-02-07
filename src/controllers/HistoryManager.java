package controllers;
import model.*;

import java.util.List;

public interface HistoryManager {
    // void add(Task task);
    List<Task> getHistory();
    void setHistory(List<Task> list);

}
