package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controllers.FileBackedTasksManager;
import controllers.Managers;
import controllers.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HttpUserServer {

    public static final int PORT = 8080;
    private HttpServer server;
    private Gson gson;
    private FileBackedTasksManager taskManager;


    public HttpUserServer() throws IOException {
        this.taskManager = FileBackedTasksManager.loadFromFile(new File("src/resources/data.csv"));
        this.gson = new Gson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/api/v1/tasks", this::handleTasks);
    }

    public static void main(String[] args) throws IOException {
        HttpUserServer userServer = new HttpUserServer();
        userServer.start();
        userServer.stop();
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/api/v1/tasks/task$", path)) {
                        String response = gson.toJson(taskManager.listOfAllTask());
                        sendTest(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/api/v1/tasks/task/\\d+$", path)) {  // "^" - спец.символ означающий начало строки
                        // "$" - спец. символ означающий конец строки
                        String pathId = path.replaceFirst("/api/v1/tasks/task/", "");
                        int id = parsePathId(path);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getTaskById(id));
                            sendTest(httpExchange, response);
                            break;
                        } else {
                            System.out.println("Получен некорректный id = " + id);
                            httpExchange.sendResponseHeaders(405,0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                        break;
                    }

                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/api/v1/tasks/\\d+$", path)) {  // "^" - спец.символ означающий начало строки
                                                                                // "$" - спец. символ означающий конец строки
                        String pathId = path.replaceFirst("/api/v1/tasks/", "");
                        int id = parsePathId(path);
                        if (id != -1) {
                            taskManager.removeTask(id);
                            System.out.println("Удалили пользователя id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + id);
                            httpExchange.sendResponseHeaders(405,0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Жнем GET или DELETE запрос, а получили - " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Started UserServer  " + PORT);
        System.out.println("http://localhost:" + PORT + "/api/v1/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private void sendTest(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

}
