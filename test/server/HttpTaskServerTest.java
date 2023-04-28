package server;

import com.google.gson.*;
import controllers.Managers;
import org.junit.jupiter.api.*;

import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer server;
    private KVServer kvServer;
    private Gson gson;
    private Task task1;
    private Task task2;
    private Subtask subtask11;
    private Subtask subtask12;
    private Epic epic1;
    private Epic epic2;




    @BeforeEach
    void init() throws IOException, InterruptedException {
        gson = Managers.getGson();
        kvServer = new KVServer();
        kvServer.start();
        server = new HttpTaskServer();
        server.start();
    }



    @AfterEach
    void tearDown() {
        kvServer.stop();
        server.stop();
    }


    @Test
    void shouldReturnTaskByIdTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllTask().size());

        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        server.manager.addTask(task1);

        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, server.manager.listOfAllTask().size());

    }


    @Test
    void shouldReturnSubtaskByIdTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllSubtask().size());

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        server.manager.addEpic(epic1);

        subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());
        server.manager.addSubtask(subtask11);

        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, server.manager.listOfAllSubtask().size());
    }


    @Test
    void shouldReturnEpicByIdTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllEpic().size());

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        server.manager.addEpic(epic1);

        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, server.manager.listOfAllEpic().size());
    }



    @Test
    void shouldReturnAllEpicList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllEpic().size());

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        server.manager.addEpic(epic1);

        epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");
        server.manager.addEpic(epic2);


        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, server.manager.listOfAllEpic().size());

    }


    @Test
    void shouldReturnAllSubtasksList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllSubtask().size());

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        server.manager.addEpic(epic1);

        subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());
        server.manager.addSubtask(subtask11);

        subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), epic1.getId());
        server.manager.addSubtask(subtask12);

        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, server.manager.listOfAllSubtask().size());

    }


    @Test
    void shouldReturnAllTaskList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllTask().size());

        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        server.manager.addTask(task1);

        task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));
        server.manager.addTask(task2);

        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, server.manager.listOfAllTask().size());
    }


    @Test
    void shouldReturnPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.getPrioritizedTasks().size());

        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        server.manager.addTask(task1);

        task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));
        server.manager.addTask(task2);

        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, server.manager.getPrioritizedTasks().size());
    }


    @Test
    void shouldReturnHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.getHistory().size());

        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        server.manager.addTask(task1);

        task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));
        server.manager.addTask(task2);

        server.manager.getTaskById(task1.getId());
        server.manager.getTaskById(task2.getId());

        URI uri = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, server.manager.getHistory().size());
    }


    @Test
    void shouldReturnEpicSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        server.manager.addEpic(epic1);

        subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());
        server.manager.addSubtask(subtask11);

        subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), epic1.getId());
        server.manager.addSubtask(subtask12);

        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + epic1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, server.manager.getListOfAllSubtaskEpic(epic1).size());
    }


    @Test
    void shouldAddNewTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllTask().size());

        Task taskToServer = new Task("Task name", "Task discription", 99, LocalDateTime.of(2023, 4, 20, 0, 12), Duration.ofMinutes(20));

        URI uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(taskToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, server.manager.listOfAllTask().size());
    }


    @Test
    void shouldAddNewSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllSubtask().size());

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        server.manager.addEpic(epic1);

        Subtask subtaskToServer  = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());

        // Gson gson = Managers.getGson();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subtaskToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, server.manager.listOfAllSubtask().size());
    }


    @Test
    void shouldAddNewEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(0, server.manager.listOfAllEpic().size());

        Epic epicToServer = new Epic("Epic test name"
                , "Epic test discription", 77);

        Gson gson = Managers.getGson();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epicToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, server.manager.listOfAllEpic().size());
    }



    @Test
    void shouldDeleteTaskAndClearMap() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();



        assertEquals(0, server.manager.listOfAllTask().size());

        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        server.manager.addTask(task1);

        task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));
        server.manager.addTask(task2);

        assertEquals(2, server.manager.listOfAllTask().size());

        URI uriDeleteById = URI.create("http://localhost:8080/tasks/task/?id=" + task1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriDeleteById)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, server.manager.listOfAllTask().size());

        URI uriDeleteAll = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uriDeleteAll)
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(0, server.manager.listOfAllTask().size());
    }

}

