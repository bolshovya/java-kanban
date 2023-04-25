package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utilities.LocalDateAdapter;

import java.io.IOException;
import java.time.LocalDateTime;

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

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
    }


}
