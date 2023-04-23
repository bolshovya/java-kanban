package server;

import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String apiToken;


    public KVTaskClient(String url) {
        // Конструктор принимает URL к серверу хранилища и регистрируется. При регистрации выдается токен (API_TOKEN)
        // который нужен при работе  сервером
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create(url + "/register");   // "http://localhost:8078/register"
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Error");
            }
            this.apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void put(String key, String json) {
        // должен сохранять состояние менеджера задач через запрос: POST/save/<ключ>?API_TOKEN=
        try {
            URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Неверный запрос: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load(String key) {
        // должен возвращать состояние менеджера задач через запрос: GET/load/<ключ>?API_TOKEN=
        try {
            URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Неверный запрос: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException();
        }
    }


}
