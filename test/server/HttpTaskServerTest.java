package server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import controllers.Managers;
import controllers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer server;
    private final Gson gson = new Gson();
    private Task task1;
    private Task task2;
    private Subtask subtask11;
    private Subtask subtask12;
    private Subtask subtask21;
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void init() throws IOException {

        server = new HttpTaskServer();
        server.setUp();
        server.start();

        /*
        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        server.manager.addTask(task1);

        task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));
        server.manager.addTask(task2);

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

        epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");
        server.manager.addEpic(epic2);

        subtask21 = new Subtask("Просмотреть все ролики из плей-листа", "Ютуб", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 3), Duration.ofMinutes(20), epic2.getId());
        server.manager.addSubtask(subtask21);

        server.manager.getTaskById(task1.getId()); // 1
        server.manager.getTaskById(task2.getId()); // 2
        server.manager.getTaskById(task1.getId()); // 1
        server.manager.getTaskById(task2.getId()); // 2
        server.manager.getSubtaskById(subtask12.getId()); // 5
        server.manager.getSubtaskById(subtask12.getId()); // 5
        server.manager.getSubtaskById(subtask11.getId()); // 4
        server.manager.getSubtaskById(subtask21.getId()); // 7
        server.manager.getEpicById(epic1.getId()); // 3
        server.manager.getEpicById(epic2.getId()); // 6
        server.manager.getTaskById(task1.getId()); // 1
        server.manager.getTaskById(task2.getId()); // 2
        server.manager.getTaskById(task1.getId()); // 1

        server.manager.getHistory();

         */

    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET().
                build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "{\"name\":\"Купить продукты\",\"description\":\"Съездить в Окей\",\"status\":\"NEW\",\"id" +
                "\":1,\"startTime\":{\"date\":{\"year\":2023,\"month\":3,\"day\":26},\"time\":{\"hour\":0,\"minute\":56," +
                "\"second\":0,\"nano\":0}},\"duration\":{\"seconds\":1200,\"nanos\":0},\"endTime\":null}";
        assertEquals(200, response.statusCode());
    }

    /*
    @Test
    void getTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Пользователи не возвращаются");
        assertEquals(2, actual.size(), "Не верное кол-во задач");
        assertEquals(task1, actual.get(0), "Задача не совпадает");
    }

     */


}

