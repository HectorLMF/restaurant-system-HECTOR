package es.ull.esit.app.middleware;

import java.net.http.*;
import java.net.URI;
import java.time.Duration;

public class ApiClient {
    private final HttpClient http;
    private final String baseUrl;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String getInfo() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/info"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }

    public String getMenu() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/menu"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }

    public String login(String name) throws Exception {
        String json = "{\"name\":\"" + escapeJson(name) + "\"}";
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/login"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }

    // Main para ejecutar el ApiClient de forma independiente y probar endpoints simples.
    public static void main(String[] args) {
        String base = (args != null && args.length > 0) ? args[0] : "http://localhost:8080";
        ApiClient client = new ApiClient(base);
        System.out.println("ApiClient test runner - base URL: " + base);
        try {
            System.out.println("-> login (tester)");
            String loginRes = client.login("tester");
            System.out.println(loginRes);

            System.out.println("-> info");
            String info = client.getInfo();
            System.out.println(info);

            System.out.println("-> menu");
            String menu = client.getMenu();
            System.out.println(menu);

            System.out.println("Finished successfully.");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Error while running ApiClient: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
}
