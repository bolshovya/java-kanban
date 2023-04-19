package server;

import com.google.gson.*;
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
    private Gson gson = new Gson();
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

        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void shouldReturnTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "{\n" +
                "  \"name\": \"Купить продукты\",\n" +
                "  \"description\": \"Съездить в Окей\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"id\": 1,\n" +
                "  \"startTime\": {\n" +
                "    \"date\": {\n" +
                "      \"year\": 2023,\n" +
                "      \"month\": 3,\n" +
                "      \"day\": 26\n" +
                "    },\n" +
                "    \"time\": {\n" +
                "      \"hour\": 0,\n" +
                "      \"minute\": 56,\n" +
                "      \"second\": 0,\n" +
                "      \"nano\": 0\n" +
                "    }\n" +
                "  },\n" +
                "  \"duration\": {\n" +
                "    \"seconds\": 1200,\n" +
                "    \"nanos\": 0\n" +
                "  },\n" +
                "  \"endTime\": null\n" +
                "}";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }


    @Test
    void shouldReturnSubtaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "{\n" +
                "  \"epicId\": 3,\n" +
                "  \"name\": \"Успешно пройти теоретический блок спринта №3\",\n" +
                "  \"description\": \"Изучить всю теорию и выполнить успешно все задачи в тренажере\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"id\": 4,\n" +
                "  \"startTime\": {\n" +
                "    \"date\": {\n" +
                "      \"year\": 2023,\n" +
                "      \"month\": 3,\n" +
                "      \"day\": 26\n" +
                "    },\n" +
                "    \"time\": {\n" +
                "      \"hour\": 1,\n" +
                "      \"minute\": 1,\n" +
                "      \"second\": 0,\n" +
                "      \"nano\": 0\n" +
                "    }\n" +
                "  },\n" +
                "  \"duration\": {\n" +
                "    \"seconds\": 1200,\n" +
                "    \"nanos\": 0\n" +
                "  },\n" +
                "  \"endTime\": null\n" +
                "}";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }


    @Test
    void shouldReturnEpicByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=6");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "{\n" +
                "  \"name\": \"Изучить курс на Ютубе JAVA\",\n" +
                "  \"description\": \"Курс Алишева\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"id\": 6,\n" +
                "  \"startTime\": {\n" +
                "    \"date\": {\n" +
                "      \"year\": 2023,\n" +
                "      \"month\": 3,\n" +
                "      \"day\": 26\n" +
                "    },\n" +
                "    \"time\": {\n" +
                "      \"hour\": 1,\n" +
                "      \"minute\": 3,\n" +
                "      \"second\": 0,\n" +
                "      \"nano\": 0\n" +
                "    }\n" +
                "  },\n" +
                "  \"duration\": {\n" +
                "    \"seconds\": 0,\n" +
                "    \"nanos\": 0\n" +
                "  },\n" +
                "  \"endTime\": {\n" +
                "    \"date\": {\n" +
                "      \"year\": 2023,\n" +
                "      \"month\": 3,\n" +
                "      \"day\": 26\n" +
                "    },\n" +
                "    \"time\": {\n" +
                "      \"hour\": 1,\n" +
                "      \"minute\": 23,\n" +
                "      \"second\": 0,\n" +
                "      \"nano\": 0\n" +
                "    }\n" +
                "  }\n" +
                "}";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }


    @Test
    void shouldReturnAllTasksList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "[\n" +
                "  {\n" +
                "    \"name\": \"Купить продукты\",\n" +
                "    \"description\": \"Съездить в Окей\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 1,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 0,\n" +
                "        \"minute\": 56,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Купить мебель\",\n" +
                "    \"description\": \"Съездить в Икею\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 2,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 0,\n" +
                "        \"minute\": 58,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  }\n" +
                "]";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }


    @Test
    void shouldReturnAllSubtasksList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "[\n" +
                "  {\n" +
                "    \"epicId\": 3,\n" +
                "    \"name\": \"Успешно пройти теоретический блок спринта №3\",\n" +
                "    \"description\": \"Изучить всю теорию и выполнить успешно все задачи в тренажере\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 4,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 1,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 3,\n" +
                "    \"name\": \"Выполнить задачу по Cody Style\",\n" +
                "    \"description\": \"Выполнить задачу самому и провести ревью сокурсника\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 5,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 2,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 6,\n" +
                "    \"name\": \"Просмотреть все ролики из плей-листа\",\n" +
                "    \"description\": \"Ютуб\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 7,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 3,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  }\n" +
                "]";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }


    @Test
    void shouldReturnAllEpicsList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "[\n" +
                "  {\n" +
                "    \"name\": \"Выполнить проектную работу Практикума\",\n" +
                "    \"description\": \"Выполнить проектную работу согласно ТЗ\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 3,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 1,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 0,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 22,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Изучить курс на Ютубе JAVA\",\n" +
                "    \"description\": \"Курс Алишева\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 6,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 3,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 0,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 23,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "]";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }


    @Test
    void shouldReturnPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "[\n" +
                "  {\n" +
                "    \"name\": \"Купить продукты\",\n" +
                "    \"description\": \"Съездить в Окей\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 1,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 0,\n" +
                "        \"minute\": 56,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Купить мебель\",\n" +
                "    \"description\": \"Съездить в Икею\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 2,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 0,\n" +
                "        \"minute\": 58,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 3,\n" +
                "    \"name\": \"Успешно пройти теоретический блок спринта №3\",\n" +
                "    \"description\": \"Изучить всю теорию и выполнить успешно все задачи в тренажере\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 4,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 1,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 3,\n" +
                "    \"name\": \"Выполнить задачу по Cody Style\",\n" +
                "    \"description\": \"Выполнить задачу самому и провести ревью сокурсника\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 5,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 2,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 6,\n" +
                "    \"name\": \"Просмотреть все ролики из плей-листа\",\n" +
                "    \"description\": \"Ютуб\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 7,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 3,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  }\n" +
                "]";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }

    @Test
    void shouldReturnHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "[\n" +
                "  {\n" +
                "    \"epicId\": 3,\n" +
                "    \"name\": \"Выполнить задачу по Cody Style\",\n" +
                "    \"description\": \"Выполнить задачу самому и провести ревью сокурсника\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 5,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 2,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 3,\n" +
                "    \"name\": \"Успешно пройти теоретический блок спринта №3\",\n" +
                "    \"description\": \"Изучить всю теорию и выполнить успешно все задачи в тренажере\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 4,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 1,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 6,\n" +
                "    \"name\": \"Просмотреть все ролики из плей-листа\",\n" +
                "    \"description\": \"Ютуб\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 7,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 3,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Выполнить проектную работу Практикума\",\n" +
                "    \"description\": \"Выполнить проектную работу согласно ТЗ\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 3,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 1,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 0,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 22,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Изучить курс на Ютубе JAVA\",\n" +
                "    \"description\": \"Курс Алишева\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 6,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 3,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 0,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 23,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Купить мебель\",\n" +
                "    \"description\": \"Съездить в Икею\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 2,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 0,\n" +
                "        \"minute\": 58,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Купить продукты\",\n" +
                "    \"description\": \"Съездить в Окей\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 1,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 0,\n" +
                "        \"minute\": 56,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  }\n" +
                "]";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }


    @Test
    void shouldReturnEpicSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String taskJson = "[\n" +
                "  {\n" +
                "    \"epicId\": 3,\n" +
                "    \"name\": \"Успешно пройти теоретический блок спринта №3\",\n" +
                "    \"description\": \"Изучить всю теорию и выполнить успешно все задачи в тренажере\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 4,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 1,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 3,\n" +
                "    \"name\": \"Выполнить задачу по Cody Style\",\n" +
                "    \"description\": \"Выполнить задачу самому и провести ревью сокурсника\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"id\": 5,\n" +
                "    \"startTime\": {\n" +
                "      \"date\": {\n" +
                "        \"year\": 2023,\n" +
                "        \"month\": 3,\n" +
                "        \"day\": 26\n" +
                "      },\n" +
                "      \"time\": {\n" +
                "        \"hour\": 1,\n" +
                "        \"minute\": 2,\n" +
                "        \"second\": 0,\n" +
                "        \"nano\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": {\n" +
                "      \"seconds\": 1200,\n" +
                "      \"nanos\": 0\n" +
                "    },\n" +
                "    \"endTime\": null\n" +
                "  }\n" +
                "]";
        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }


    @Test
    void shouldAddNewTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task taskToServer = new Task("Task name", "Task discription", 99, LocalDateTime.of(2023, 4, 20, 0, 12), Duration.ofMinutes(20));
        assertEquals(2, server.manager.listOfAllTask().size());


        URI uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(taskToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());

        assertEquals(3, server.manager.listOfAllTask().size());
    }

    @Test
    void shouldAddNewSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Subtask subtaskToServer = new Subtask("Subtask test name"
                , "Subtask test discription", 88,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), 3);
        assertEquals(3, server.manager.listOfAllSubtask().size());

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subtaskToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());

        assertEquals(4, server.manager.listOfAllSubtask().size());
    }


    @Test
    void shouldAddNewEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epicToServer = new Epic("Epic test name"
                , "Epic test discription", 77);
        assertEquals(2, server.manager.listOfAllEpic().size());

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epicToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());

        assertEquals(3, server.manager.listOfAllEpic().size());
    }

    @Test
    void shouldDeleteTaskAndClearMap() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        assertEquals(2, server.manager.listOfAllTask().size());

        URI uriDeleteById = URI.create("http://localhost:8080/tasks/task/?id=1");
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

