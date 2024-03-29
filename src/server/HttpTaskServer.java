package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import controllers.Managers;
import controllers.TaskManager;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.Optional;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private Gson gson;
    private HttpServer httpServer;
    protected TaskManager manager;

    public HttpTaskServer() throws IOException, InterruptedException {
        this.gson = Managers.getGson();
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost",PORT), 0);
        this.manager = Managers.getHttpDefault();
        this.httpServer.createContext("/tasks", this::Handler);
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");

    }

    public void Handler(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            Optional<String> query = Optional.ofNullable(exchange.getRequestURI().getQuery());

            switch (method) {
                case "GET":
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        if (query.isEmpty()) {
                            String response = gson.toJson(manager.listOfAllTask());
                            writeResponse(exchange, response, 200);
                            break;
                        } else {
                            String queryId = query.get().substring(3);
                            int id = getTaskId(queryId);
                            if (id != -1) {
                                String response = gson.toJson(manager.getTaskById(id));
                                System.out.println("вернули задачу id " + id);
                                writeResponse(exchange, response, 200);
                                break;
                            } else {
                                System.out.println("получен некорректный id " + id);
                                exchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                    }

                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        if (query.isEmpty()) {
                            String response = gson.toJson(manager.listOfAllSubtask());
                            writeResponse(exchange, response, 200);
                            break;
                        } else {
                            String queryId = query.get().substring(3);
                            int id = getTaskId(queryId);
                            if (id != -1) {
                                String response = gson.toJson(manager.getSubtaskById(id));
                                System.out.println("вернули подзадачу id " + id);
                                writeResponse(exchange, response, 200);
                                break;
                            } else {
                                System.out.println("получен некорректный id " + id);
                                exchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                    }

                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        if (query.isEmpty()) {
                            String response = gson.toJson(manager.listOfAllEpic());
                            writeResponse(exchange, response, 200);
                            break;
                        } else {
                            String queryId = query.get().substring(3);
                            int id = getTaskId(queryId);
                            if (id != -1) {
                                String response = gson.toJson(manager.getEpicById(id));
                                System.out.println("вернули эпик id " + id);
                                writeResponse(exchange, response, 200);
                                break;
                            } else {
                                System.out.println("получен некорректный id " + id);
                                exchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                    }

                    if (Pattern.matches("^/tasks/history/$", path)) {
                        String response = gson.toJson(manager.getHistory());
                        writeResponse(exchange, response, 200);
                        break;
                    }

                    if (Pattern.matches("^/tasks/$", path)) {
                        String response = gson.toJson(manager.getPrioritizedTasks());
                        writeResponse(exchange, response, 200);
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask/epic/$", path)) {
                        if (query.isEmpty()) {
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        } else {
                            String queryId = query.get().substring(3);
                            int id = getTaskId(queryId);
                            if (id != -1) {
                                String response = gson.toJson(manager.getListOfAllSubtaskEpic(manager.getEpicById(id)));
                                writeResponse(exchange, response, 200);
                                break;
                            } else {
                                System.out.println("получен некорректный id " + id);
                                exchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                    }

                case "DELETE":
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        if (query.isEmpty()) {
                            manager.clearTaskList();
                            exchange.sendResponseHeaders(204, 0);
                            break;
                        } else {
                            String queryId = query.get().substring(3);
                            int id = getTaskId(queryId);
                            if (id != -1) {
                                manager.removeTask(id);
                                System.out.println("удалили задачу id " + id);
                                exchange.sendResponseHeaders(204, 0);
                                break;
                            } else {
                                System.out.println("получен некорректный id " + id);
                                exchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                    }

                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        if (query.isEmpty()) {
                            manager.clearSubtaskList();
                            exchange.sendResponseHeaders(204, 0);
                            break;
                        } else {
                            String queryId = query.get().substring(3);
                            int id = getTaskId(queryId);
                            if (id != -1) {
                                manager.removeSubtask(id);
                                System.out.println("удалили подзадачу id " + id);
                                exchange.sendResponseHeaders(204, 0);
                                break;
                            } else {
                                System.out.println("получен некорректный id " + id);
                                exchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                    }

                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        if (query.isEmpty()) {
                            manager.clearEpicList();
                            exchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            String queryId = query.get().substring(3);
                            int id = getTaskId(queryId);
                            if (id != -1) {
                                manager.removeEpic(id);
                                System.out.println("удалили подзадачу id " + id);
                                exchange.sendResponseHeaders(200, 0);
                                break;
                            } else {
                                System.out.println("получен некорректный id " + id);
                                exchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                    }

                case "POST":
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task addedTask = gson.fromJson(body, Task.class);
                        manager.addTask(addedTask);
                        exchange.sendResponseHeaders(200, 0);
                    }

                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Subtask addedSubtask = gson.fromJson(body, Subtask.class);
                        manager.addSubtask(addedSubtask);
                        exchange.sendResponseHeaders(200, 0);
                    }

                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic addedEpic = gson.fromJson(body, Epic.class);
                        manager.addEpic(addedEpic);
                        exchange.sendResponseHeaders(200, 0);
                    }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }


    private int getTaskId(String path) { // Артем Дяченко
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    public void start() {
        System.out.println("Started server " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }


}
