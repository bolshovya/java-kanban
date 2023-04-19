package controllers;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import model.*;
import server.KVTaskClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;


public class HttpTaskManager extends FileBackedTasksManager {
    Gson gson;
    KVTaskClient client;


    public HttpTaskManager(URI uri) throws IOException, InterruptedException {
        super();
        this.client = new KVTaskClient(uri);
        this.gson = new Gson();

    }


    @Override
    public void save() {
        try {
            String tasksJson = gson.toJson(listOfAllTask());
            client.put("tasks", tasksJson);

            String subtasksJson = gson.toJson(listOfAllSubtask());
            client.put("subtasks", subtasksJson);

            String epicJson = gson.toJson(listOfAllEpic());
            client.put("epic", epicJson);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        try {
            String tasksJson = client.load("tasks");
            Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
            List<Task> addedTasks = gson.fromJson(tasksJson, taskType);
            for (Task task : addedTasks) {
                taskStorage.put(task.getId(), task);
            }

            String subtaskJson = client.load("subtasks");
            Type subtaskType = new TypeToken<ArrayList<Task>>() {}.getType();
            List<Subtask> addedSubtasks = gson.fromJson(tasksJson, taskType);
            for (Subtask subtask : addedSubtasks) {
                subtaskStorage.put(subtask.getId(), subtask);
            }

            String epicJson = client.load("epics");
            Type epicType = new TypeToken<ArrayList<Task>>() {}.getType();
            List<Epic> addedEpics = gson.fromJson(tasksJson, taskType);
            for (Epic epic : addedEpics) {
                taskStorage.put(epic.getId(), epic);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
