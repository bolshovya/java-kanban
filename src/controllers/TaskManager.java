package controllers;

import model.*;
import java.util.*;

public interface TaskManager {

    List<Task> getPrioritizedTasks();

    List<Task> listOfAllTask();


    List<Epic> listOfAllEpic();


    List<Subtask> listOfAllSubtask();


    void clearTaskList();


    void clearEpicList();


    void clearSubtaskList();


    Task getTaskById(Integer id);


    Epic getEpicById(Integer id);


    Subtask getSubtaskById(Integer id);


    void addTask(Task newTask);


    void addEpic(Epic newEpic);


    void addSubtask(Subtask newSubtask);


    void updateTask(Integer idTask, Task newTask);


    void updateEpic(Integer idTask, Epic newEpic);


    void updateSubtask(Integer idSubtask, Subtask newSubtask);


    void removeTask(Integer id);


    void removeEpic(Integer id);


    void removeSubtask(Integer id);


    List<Subtask> getListOfAllSubtaskEpic(Epic epic);


    List<Task> getHistory();

}
