package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private Gson gson;
    private HttpServer httpServer;
    protected TaskManager manager;

    public HttpTaskServer() throws IOException, InterruptedException {
        this.gson = new Gson();
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost",PORT), 0);
        this.manager = Managers.getHttpDefault();
        this.httpServer.createContext("/tasks", this::Handler);
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");

    }

    public static void main(String[] args) throws IOException, InterruptedException {


        HttpTaskServer server = new HttpTaskServer();
        // server.setUp();

        Epic epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");

        Subtask subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());
        server.manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), epic1.getId());
        server.manager.addSubtask(subtask12);


        server.start();


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

    public void setUp() {
        Task task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        manager.addTask(task1);

        Task task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));
        manager.addTask(task2);

        Epic epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), epic1.getId());
        manager.addSubtask(subtask12);

        Epic epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Просмотреть все ролики из плей-листа", "Ютуб", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 3), Duration.ofMinutes(20), epic2.getId());
        manager.addSubtask(subtask21);

        manager.getTaskById(task1.getId()); // 1
        manager.getTaskById(task2.getId()); // 2
        manager.getTaskById(task1.getId()); // 1
        manager.getTaskById(task2.getId()); // 2
        manager.getSubtaskById(subtask12.getId()); // 5
        manager.getSubtaskById(subtask12.getId()); // 5
        manager.getSubtaskById(subtask11.getId()); // 4
        manager.getSubtaskById(subtask21.getId()); // 7
        manager.getEpicById(epic1.getId()); // 3
        manager.getEpicById(epic2.getId()); // 6
        manager.getTaskById(task1.getId()); // 1
        manager.getTaskById(task2.getId()); // 2
        manager.getTaskById(task1.getId()); // 1

        manager.getHistory();
    }

}
