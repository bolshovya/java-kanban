package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String apiToken;


    public KVTaskClient(String url) throws IOException, InterruptedException {
        // Конструктор принимает URL к серверу хранилища и регистрируется. При регистрации выдается токен (API_TOKEN)
        // который нужен при работе  сервером
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/register");   // "http://localhost:8078/register"
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        this.apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        // должен сохранять состояние менеджера задач через запрос: POST/save/<ключ>?API_TOKEN=
        URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        // HttpResponse.BodyHandler<String> response = HttpResponse.BodyHandlers.ofString();
        // client.send(request, response);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        // должен возвращать состояние менеджера задач через запрос: GET/load/<ключ>?API_TOKEN=
        URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    /*
    Далее проверьте код клиента в main. Для этого запустите KVServer, создайте экземпляр KVTaskClient.
    Затем сохраните значение под разными ключами и проверьте, что при запросе возвращаются нужные данные. Удостоверьтесь,
    что если изменить знание, то при повторном вызове вернётся уже не старое, а новое.
     */


}
