package controllers;

import com.google.gson.Gson;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {
    Gson gson;
    KVTaskClient client;


    public HttpTaskManager() {
        /* конструктор HttpTaskManager должен будет вместо имени файла принимать URL к серверу KVServer.
        Также HttpTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера. Вам нужно
        заменить вызовы сохранения состояния в файлах на вызов клиента.
         */
        this.client = new KVTaskClient();

    }

    // в конце обновите статический метод getDefault() в утилитарном классе Managers, чтобы он возвращал HttpTaskManager.

    @Override
    public void save() {
        // 1. Сохранить Таски
        String tasksJson = gson.fromJson(taskStorage.values());
        client.put("tasks", tasksJson);

        String subtasksJson = gson.fromJson(subtaskStorage.values());
        client.put("subtasks", subtasksJson);

        String epicJson = gson.fromJson(epicStorage.values());
        client.put("epic", epicJson);

    }
}
