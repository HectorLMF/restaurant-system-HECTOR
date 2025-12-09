package es.ull.esit.app.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.http.*;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Cashier;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import es.ull.esit.app.middleware.model.User;

/**
 * @brief API client for interacting with the backend REST API.
 * 
 *        Wraps HTTPClient and provides methods to:
 *        - performs CRUD operations on Appetizers, Cashiers, Drinks,
 *        MainCourses.
 *        - sends login requests to the backend.
 */
public class ApiClient {
  /** Low-level HTTP client. to send requests. */
  private final HttpClient http;

  /** Base URL of the Rest API. */
  private final String baseUrl;

  /**
   * JSON object mapper for serialization/deserialization: converts between JSON
   * and Java objects.
   */
  private final ObjectMapper mapper;

  /**
   * @brief Constructs the ApiClient with the given base URL.
   * 
   *        If the base URL ends with "/", the slash is removed to avoid double
   *        slashes.
   * @param baseUrl [String] Base URL of the REST API, such as
   *                "http://localhost:8080".
   */
  public ApiClient(String baseUrl) {
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.http = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();
    this.mapper = new ObjectMapper();
  }

  /**
   * @brief Generic GET helper that returns a single object.
   * 
   *        It validates the HTTP status code before attempting to parse JSON.
   *        - If status is 200–299 and body is non-empty: parse JSON.
   *        - If status is 204 or body is empty: return null.
   *        - For any other status: throw a RuntimeException with details.
   * 
   * @param <T>          [T] Type of the response object.
   * @param path         [String] API endpoint path.
   * @param responseType [Class<T>] Class of the response type.
   * @return [T] Object of type T or null if 204 / cuerpo vacío.
   * @throws Exception If an error occurs during the request.
   */
  private <T> T get(String path, Class<T> responseType) throws Exception {
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .GET()
        .header("Accept", "application/json")
        .build();

    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
    int status = res.statusCode();
    String body = res.body();

    System.out.println("GET " + path + " -> HTTP " + status);
    System.out.println("Response body: '" + body + "'");

    if (status == 204 || body == null || body.trim().isEmpty()) {
      return null;
    }

    if (status < 200 || status >= 300) {
      throw new RuntimeException("GET " + path + " failed with HTTP " + status + " body: " + body);
    }

    return mapper.readValue(body, responseType);
  }

  /**
   * Generic method for GET requests returning lists.
   *
   * It validates the HTTP status code before attempting to parse JSON.
   * - If status is 200–299 and body is non-empty: parse JSON.
   * - If status is 204 or body is empty: return an empty list.
   * - For any other status: throw a RuntimeException with details.
   * 
   * @param <T>     [T] Type of the list elements.
   * @param path    [String] API endpoint path.
   * @param typeRef [TypeReference<List<T>>] Type reference for deserialization
   * @return [List<T>] List of objects of type T.
   * @throws Exception If an error occurs during the request.
   */
  private <T> List<T> getList(String path, TypeReference<List<T>> typeRef) throws Exception {
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .GET()
        .header("Accept", "application/json")
        .build();

    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
    int status = res.statusCode();
    String body = res.body();

    System.out.println("GET " + path + " -> HTTP " + status);
    System.out.println("Response body: '" + body + "'");

    if (status == 204 || body == null || body.trim().isEmpty()) {
      return java.util.Collections.emptyList();
    }

    if (status < 200 || status >= 300) {
      throw new RuntimeException("GET " + path + " failed with HTTP " + status + " body: " + body);
    }

    return mapper.readValue(body, typeRef);
  }

  /**
   * @brief Generic POST helper that sends an object and returns a response
   *        object.
   * 
   *        It validates the HTTP status code before attempting to parse JSON.
   *        - If status is 200–299: parse JSON.
   *        - For any other status: throw a RuntimeException with details.
   * 
   * @param <T>          [T] Type of the request body.
   * @param <R>          [R] Type of the response body.
   * @param path         [String] API endpoint path.
   * @param body         [T] Request body object.
   * @param responseType [Class<R>] Class of the response type.
   * @return [R] Response object of type R.
   * @throws Exception If an error occurs during the request.
   */
  private <T, R> R post(String path, T body, Class<R> responseType) throws Exception {
    String json = mapper.writeValueAsString(body);
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .header("Content-Type", "application/json")
        .build();
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

    int status = res.statusCode();
    String responseBody = res.body();

    System.out.println("POST " + path + " -> HTTP " + status);
    System.out.println("Response body: '" + responseBody + "'");

    if (status < 200 || status >= 300) {
      throw new RuntimeException("POST " + path + " failed with HTTP " + status + " body: " + responseBody);
    }

    return mapper.readValue(responseBody, responseType);
  }

  // ------------- CRUD methods for Appetizers ----------------
  /**
   * @brief GET all appetizers.
   * 
   *        Retrieves a list of all appetizers from the backend.
   * 
   * @return [List<Appetizer>] List of all appetizers.
   * @throws Exception If an error occurs during the request.
   */
  public List<Appetizer> getAllAppetizers() throws Exception {
    return getList("/api/appetizers", new TypeReference<List<Appetizer>>() {
    });
  }

  /**
   * @brief GET appetizer by ID.
   * 
   *        Retrieves an appetizer by its ID from the backend.
   * 
   * @param id [Long] ID of the appetizer.
   * @return [Appetizer] Appetizer object.
   * @throws Exception If an error occurs during the request.
   */
  public Appetizer getAppetizerById(Long id) throws Exception {
    return get("/api/appetizers/" + id, Appetizer.class);
  }

  /**
   * @brief POST create new appetizer.
   *        Creates a new appetizer in the backend.
   * 
   * @param appetizer [Appetizer] Appetizer object to create.
   * @return [Appetizer] Created Appetizer object returned from the backend.
   * @throws Exception If an error occurs during the request.
   */
  public Appetizer createAppetizer(Appetizer appetizer) throws Exception {
    return post("/api/appetizers", appetizer, Appetizer.class);
  }

  /**
   * @brief PUT update appetizer by ID.
   *        Updates an existing appetizer in the backend.
   * 
   * @param id        [Long] ID of the appetizer to update.
   * @param appetizer [Appetizer] Appetizer object with updated data.
   * @return [Appetizer] Updated Appetizer object returned from the backend.
   * @throws Exception If an error occurs during the request.
   */
  public Appetizer updateAppetizer(Long id, Appetizer appetizer) throws Exception {
    String json = mapper.writeValueAsString(appetizer);
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/api/appetizers/" + id))
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .header("Content-Type", "application/json")
        .build();
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

    int status = res.statusCode();
    String body = res.body();

    if (status < 200 || status >= 300) {
      throw new RuntimeException("PUT /api/appetizers/" + id + " failed with HTTP " + status + " body: " + body);
    }

    return mapper.readValue(body, Appetizer.class);
  }

  /**
   * @brief DELETE appetizer by ID.
   *        Deletes an existing appetizer in the backend.
   * 
   * @param id [Long] ID of the appetizer to delete.
   * @throws Exception If an error occurs during the request.
   */
  public void deleteAppetizer(Long id) throws Exception {
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/api/appetizers/" + id))
        .DELETE()
        .build();
    http.send(req, HttpResponse.BodyHandlers.ofString());
  }

  // ------------- READ / UPDATE methods for Cashiers ----------------

  /**
   * GET all cashiers.
   * 
   * Retrieves a list of all cashiers from the backend.
   * 
   * @return [List<Cashier>] List of all cashiers.
   * @throws Exception If an error occurs during the request.
   */
  public List<Cashier> getAllCashiers() throws Exception {
    return getList("/api/cashiers", new TypeReference<List<Cashier>>() {
    });
  }

  /**
   * @brief GET cashier by ID.
   *        Retrieves a cashier by its ID from the backend.
   * 
   * @param id [Long] ID of the cashier.
   * @return [Cashier] Cashier object.
   * @throws Exception If an error occurs during the request.
   */
  public Cashier getCashierById(Long id) throws Exception {
    return get("/api/cashiers/" + id, Cashier.class);
  }

  /**
   * @brief GET cashier by name.
   *        Retrieves a cashier by its username from the backend.
   *
   * @param name [String] username of the cashier.
   * @return [Cashier] Cashier object.
   * @throws Exception If an error occurs during the request.
   */
  public Cashier getCashierByName(String name) throws Exception {
    return get("/api/cashiers/name/" + name, Cashier.class);
  }

  /**
   * @brief PUT update cashier by ID.
   * 
   *        Updates an existing cashier in the backend (name and/or salary).
   *
   * @param id      [Long] ID of the cashier to update.
   * @param cashier [Cashier] Cashier object with updated data.
   * @return [Cashier] Updated Cashier object returned from the backend.
   * @throws Exception If an error occurs during the request.
   */
  public Cashier updateCashier(Long id, Cashier cashier) throws Exception {
    String json = mapper.writeValueAsString(cashier);
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/api/cashiers/" + id))
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .header("Content-Type", "application/json")
        .build();
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

    int status = res.statusCode();
    String body = res.body();

    if (status < 200 || status >= 300) {
      throw new RuntimeException("PUT /api/cashiers/" + id + " failed with HTTP " + status + " body: " + body);
    }

    return mapper.readValue(body, Cashier.class);
  }

  // ------------- CRUD methods for Drinks ----------------
  /**
   * @brieef GET all drinks.
   * 
   *         Retrieves a list of all drinks from the backend.
   * 
   * @return [List<Drink>] List of all drinks.
   * @throws Exception If an error occurs during the request.
   */
  public List<Drink> getAllDrinks() throws Exception {
    return getList("/api/drinks", new TypeReference<List<Drink>>() {
    });
  }

  /**
   * @brief GET drink by ID.
   * 
   *        Retrieves a drink by its ID from the backend.
   * 
   * @param id [Long] ID of the drink.
   * @return [Drink] Drink object.
   * @throws Exception If an error occurs during the request.
   */
  public Drink getDrinkById(Long id) throws Exception {
    return get("/api/drinks/" + id, Drink.class);
  }

  /**
   * @brief POST create new drink.
   * 
   *        Creates a new drink in the backend.
   * 
   * @param drink [Drink] Drink object to create.
   * @return [Drink] Created Drink object returned from the backend.
   * @throws Exception If an error occurs during the request.
   */
  public Drink createDrink(Drink drink) throws Exception {
    return post("/api/drinks", drink, Drink.class);
  }

  /**
   * @brief PUT update drink by ID.
   * 
   *        Updates an existing drink in the backend by its ID.
   * 
   * @param id    [Long] ID of the drink to update.
   * @param drink [Drink] Drink object with updated data.
   * @return [Drink] Updated Drink object returned from the backend.
   * @throws Exception If an error occurs during the request.
   */
  public Drink updateDrink(Long id, Drink drink) throws Exception {
    String json = mapper.writeValueAsString(drink);
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/api/drinks/" + id))
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .header("Content-Type", "application/json")
        .build();
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

    int status = res.statusCode();
    String body = res.body();

    if (status < 200 || status >= 300) {
      throw new RuntimeException("PUT /api/drinks/" + id + " failed with HTTP " + status + " body: " + body);
    }

    return mapper.readValue(body, Drink.class);
  }

  /**
   * @brief DELETE drink by ID.
   * 
   *        Deletes an existing drink in the backend.
   * 
   * @param id [Long] ID of the drink to delete.
   * @throws Exception If an error occurs during the request
   */
  public void deleteDrink(Long id) throws Exception {
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/api/drinks/" + id))
        .DELETE()
        .build();
    http.send(req, HttpResponse.BodyHandlers.ofString());
  }

  // ------------- CRUD methods for MainCourses ----------------
  /**
   * @brief GET all maincourses.
   * 
   *        Retrieves a list of all maincourses from the backend.
   * 
   * @return [List<MainCourse>] List of all maincourses.
   * @throws Exception If an error occurs during the request.
   */
  public List<MainCourse> getAllMainCourses() throws Exception {
    return getList("/api/maincourses", new TypeReference<List<MainCourse>>() {
    });
  }

  /**
   * @brief GET maincourse by ID.
   * 
   *        Retrieves a maincourse by its ID from the backend.
   * 
   * @param id [Long] ID of the maincourse.
   * @return [MainCourse] MainCourse object.
   * @throws Exception If an error occurs during the request.
   */
  public MainCourse getMainCourseById(Long id) throws Exception {
    return get("/api/maincourses/" + id, MainCourse.class);
  }

  /**
   * @brief POST create new maincourse.
   * 
   *        Creates a new maincourse in the backend.
   * 
   * @param mainCourse [MainCourse] MainCourse object to create.
   * @return [MainCourse] Created MainCourse object returned from the backend.
   * @throws Exception If an error occurs during the request.
   */
  public MainCourse createMainCourse(MainCourse mainCourse) throws Exception {
    return post("/api/maincourses", mainCourse, MainCourse.class);
  }

  /**
   * @brief PUT update maincourse by ID.
   * 
   *        Updates an existing maincourse in the backend.
   * 
   * @param id         [Long] ID of the maincourse to update.
   * @param mainCourse [MainCourse] MainCourse object with updated data.
   * @return [MainCourse] Updated MainCourse object returned from the backend.
   * @throws Exception If an error occurs during the request.
   */
  public MainCourse updateMainCourse(Long id, MainCourse mainCourse) throws Exception {
    String json = mapper.writeValueAsString(mainCourse);
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/api/maincourses/" + id))
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .header("Content-Type", "application/json")
        .build();
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

    int status = res.statusCode();
    String body = res.body();

    if (status < 200 || status >= 300) {
      throw new RuntimeException("PUT /api/maincourses/" + id + " failed with HTTP " + status + " body: " + body);
    }

    return mapper.readValue(body, MainCourse.class);
  }

  /**
   * @brief DELETE maincourse by ID.
   * 
   *        Deletes an existing maincourse in the backend by its ID.
   * 
   * @param id [Long] ID of the maincourse to delete.
   * @throws Exception If an error occurs during the request.
   */
  public void deleteMainCourse(Long id) throws Exception {
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/api/maincourses/" + id))
        .DELETE()
        .build();
    http.send(req, HttpResponse.BodyHandlers.ofString());
  }

  // ------------- Authentication methods ----------------

  /**
   * @brief Legacy login method kept for compatibility.
   * 
   *        If authentication is added in the future, it can be implemented here.
   * 
   * @param ignored [String] Ignored parameter.
   * @throws Exception if login fails.
   */
  public void login(String ignored) throws Exception {
    // No-op: kept for compatibility.
  }

  /**
   * @brief Authenticates a user against the backend.
   * 
   *        Sends a POST request to "/api/login" with username and password.
   *        If the response status is 200, it returns the User parsed from JSON.
   *        For any other status code it throws a RuntimeException.
   *
   * @param username [String] Username entered by the user.
   * @param password [String] Password entered by the user.
   * @return [User] Authenticated User object.
   * @throws Exception If the request or JSON parsing fails.
   */
  public User login(String username, String password) throws Exception {

    String jsonBody = mapper.writeValueAsString(Map.of(
        "username", username,
        "password", password));

    HttpRequest request = HttpRequest.newBuilder()

        .uri(URI.create(baseUrl + "/api/login"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("POST /api/login -> HTTP " + response.statusCode());
    System.out.println("Response body: '" + response.body() + "'");

    if (response.statusCode() == 200) {
      return mapper.readValue(response.body(), User.class);
    } else {
      throw new RuntimeException("Login failed with status: " + response.statusCode());
    }
  }

}
