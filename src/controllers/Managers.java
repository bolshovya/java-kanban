package controllers;

import java.io.IOException;

public class Managers {


    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getHttpDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }


}
