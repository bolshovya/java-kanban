package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.FileBackedTasksManager;
import controllers.Managers;
import controllers.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {

    public static final int PORT = 8080;
    protected HttpServer httpServer;
    protected Gson gson;
    private TaskManager taskManager;
    private static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;



    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                //.registerTypeAdapter()
                .create();
        httpServer.createContext("/tasks", this::handler);
        // taskManager = new FileBackedTasksManager().loadFromFile(new File("resources/data.csv"));
        // httpServer.createContext("/tasks", this::getTasks);
        System.out.println("HTTP-сервер запущен на " + PORT + "порту.");
    }

    private void handler(HttpExchange exchange) {

    }







    /*

    TaskManager fileBackedTasksManager = Managers.getFileBackedTasksManager();



    // Как получить все задачи:

    HttpClient client = HttpClient.newHttpClient();
    URI uri = URI.create("http:localhost:8080/tasks/task");
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());




    // Как создать задачу:

    URI url = URI.create("http:localhost:8080/tasks/task");
    Gson gson = new Gson();
    String json = gson.toJson(newTask);
    final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
    HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


    // Как получить задачу с id = 1:

    HttpClient client = HttpClient.newHttpClient();
    URI uri = URI.create("http:localhost:8080/tasks/task/?id=1");
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
    HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());


     */

}
