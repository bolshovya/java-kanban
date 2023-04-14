package server.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.Managers;
import controllers.TaskManager;
import model.Task;

import java.io.OutputStream;
import java.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class taskHandler implements HttpHandler {

    private static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            //.registerTypeAdapter()
            .create();

    private TaskManager manager = Managers.getDefaultFileBackedTasksManager();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers requestHeaders = exchange.getRequestHeaders();
        List<String> contentTypeValues = requestHeaders.get("Content=type");
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
        String[] splitPath = path.split("/");
        String request = path.split("/")[1];
        Integer id = Integer.parseInt(path.split("=")[1]);
        exchange.sendResponseHeaders(200,0);

        if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
            switch (method) {
                case "GET":
                    if (path.equals("/tasks/task/")) {
                        String response = gson.toJson(manager.listOfAllTask());
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            os.close();
                            return;
                        }
                    } else if (path.contains("tasks/task/?id=")) {
                        String response = gson.toJson(manager.getTaskById(id));
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            os.close();
                            return;
                        }
                    } else if (path.equals("tasks/subtask/")) {
                        String response = gson.toJson(manager.listOfAllSubtask());
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            os.close();
                            return;
                        }
                    } else if (path.contains("tasks/subtask/?id=")) {
                        String response = gson.toJson(manager.getSubtaskById(id));
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            os.close();
                            return;
                        }
                    } else if (path.equals("tasks/epic/")) {
                            String response = gson.toJson(manager.listOfAllEpic());
                            try (OutputStream os = exchange.getResponseBody()) {
                                exchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                os.close();
                                return;
                            }
                    } else if (path.contains("tasks/epic/?id=")) {
                        String response = gson.toJson(manager.getEpicById(id));
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            os.close();
                            return;
                        }
                    } else if (path.equals("/tasks/history/")) {
                        String response = gson.toJson(manager.getHistory());
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            os.close();
                            return;
                        }
                    } else if (path.equals("/tasks/")) {
                        String response = gson.toJson(manager.getPrioritizedTasks());
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            os.close();
                            return;
                        }
                    } else {
                        exchange.sendResponseHeaders(404, 0);

                    }
                    break;
                case "POST":
                    if (path.equals("/tasks/task/")) {
                        Task addedTask = gson.fromJson(body, Task.class);
                        manager.addTask(addedTask);
                        return;
                    } else if (path.split("=").length > 0) {
                        String response = gson.toJson(manager.getTaskById(id));
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            os.close();
                            return;
                        }
                    } else {
                        exchange.sendResponseHeaders(404, 0);

                    }
            }
        }
    }
}
