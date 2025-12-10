package es.ull.esit.app.middleware;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Cashier;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import es.ull.esit.app.middleware.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @brief API client for interacting with the backend REST API.
 *
 *        Wraps HttpClient and provides methods to:
 *        - perform CRUD operations on Appetizers, Cashiers, Drinks, MainCourses.
 *        - send login requests to the backend.
 */
public class ApiClient {

  /** Constant for JSON content type. */
  private static final String APPLICATION_JSON = "application/json";

  /** Logger for logging events and errors. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class);

  /** HTTP header for content type. */
  private static final String CONTENT_TYPE = "Content-Type";

  /** API endpoint for appetizers. */
  private static final String API_APPETIZERS = "/api/appetizers/";

  /** API endpoint for drinks. */
  private static final String API_DRINKS = "/api/drinks/";

  /** API endpoint for main courses. */
  private static final String API_MAINCOURSES = "/api/maincourses/";

  private static final String API_LOGIN = "/api/login";

  /** Low-level HTTP client to send requests. */
  private final HttpClient http;

  /** Base URL of the REST API. */
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
   *
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

  // ---------------------------------------------------------------------------
  // Helpers genéricos
  // ---------------------------------------------------------------------------

  /**
   * @brief Generic GET helper that returns a single object.
   *
   *        It validates the HTTP status code before attempting to parse JSON.
   *        - If status is 200–299 and body is non-empty: parse JSON.
   *        - If status is 204 or body is empty: return null.
   *        - For any other status: throw ApiClientException with details.
   *
   * @param <T>          [T] Type of the response object.
   * @param path         [String] API endpoint path.
   * @param responseType [Class<T>] Class of the response type.
   * @return [T] Object of type T or null if 204 / empty body.
   */
  private <T> T get(String path, Class<T> responseType) {
    try {
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .GET()
          .header("Accept", APPLICATION_JSON)
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("GET  {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status == 204 || body == null || body.trim().isEmpty()) {
        return null;
      }

      if (status < 200 || status >= 300) {
        throw new ApiClientException("GET " + path + " failed with HTTP " + status + " body: " + body);
      }

      return mapper.readValue(body, responseType);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during GET " + path, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during GET " + path, e);
    }
  }

  /**
   * Generic method for GET requests returning lists.
   *
   * It validates the HTTP status code before attempting to parse JSON.
   * - If status is 200–299 and body is non-empty: parse JSON.
   * - If status is 204 or body is empty: return an empty list.
   * - For any other status: throw ApiClientException with details.
   *
   * @param <T>     [T] Type of the list elements.
   * @param path    [String] API endpoint path.
   * @param typeRef [TypeReference<List<T>>] Type reference for deserialization.
   * @return [List<T>] List of objects of type T.
   */
  private <T> List<T> getList(String path, TypeReference<List<T>> typeRef) {
    try {
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .GET()
          .header("Accept", APPLICATION_JSON)
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("GET  {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status == 204 || body == null || body.trim().isEmpty()) {
        return java.util.Collections.emptyList();
      }

      if (status < 200 || status >= 300) {
        throw new ApiClientException("GET " + path + " failed with HTTP " + status + " body: " + body);
      }

      return mapper.readValue(body, typeRef);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during GET " + path, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during GET " + path, e);
    }
  }

  /**
   * @brief Generic POST helper that sends an object and returns a response
   *        object.
   *
   *        It validates the HTTP status code before attempting to parse JSON.
   *        - If status is 200–299: parse JSON.
   *        - For any other status: throw ApiClientException with details.
   *
   * @param <T>          [T] Type of the request body.
   * @param <R>          [R] Type of the response body.
   * @param path         [String] API endpoint path.
   * @param body         [T] Request body object.
   * @param responseType [Class<R>] Class of the response type.
   * @return [R] Response object of type R.
   */
  private <T, R> R post(String path, T body, Class<R> responseType) {
    try {
      String json = mapper.writeValueAsString(body);

      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

      int status = res.statusCode();
      String responseBody = res.body();

      LOGGER.info("POST {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", responseBody);

      if (status < 200 || status >= 300) {
        throw new ApiClientException("POST " + path + " failed with HTTP " + status + " body: " + responseBody);
      }

      return mapper.readValue(responseBody, responseType);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during POST " + path, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during POST " + path, e);
    }
  }

  // ---------------------------------------------------------------------------
  // CRUD methods for Appetizers
  // ---------------------------------------------------------------------------

  /**
   * @brief GET all appetizers.
   *
   *        Retrieves a list of all appetizers from the backend.
   *
   * @return [List<Appetizer>] List of all appetizers.
   */
  public List<Appetizer> getAllAppetizers() {
    return getList("/api/appetizers", new TypeReference<List<Appetizer>>() {});
  }

  /**
   * @brief GET appetizer by ID.
   *
   *        Retrieves an appetizer by its ID from the backend.
   *
   * @param id [Long] ID of the appetizer.
   * @return [Appetizer] Appetizer object.
   */
  public Appetizer getAppetizerById(Long id) {
    return get(API_APPETIZERS + id, Appetizer.class);
  }

  /**
   * @brief POST create new appetizer.
   *        Creates a new appetizer in the backend.
   *
   * @param appetizer [Appetizer] Appetizer object to create.
   * @return [Appetizer] Created Appetizer object returned from the backend.
   */
  public Appetizer createAppetizer(Appetizer appetizer) {
    return post("/api/appetizers", appetizer, Appetizer.class);
  }

  /**
   * @brief PUT update appetizer by ID.
   *        Updates an existing appetizer in the backend.
   *
   * @param id        [Long] ID of the appetizer to update.
   * @param appetizer [Appetizer] Appetizer object with updated data.
   * @return [Appetizer] Updated Appetizer object returned from the backend.
   */
  public Appetizer updateAppetizer(Long id, Appetizer appetizer) {
    try {
      String json = mapper.writeValueAsString(appetizer);
      String path = API_APPETIZERS + id;

      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .PUT(HttpRequest.BodyPublishers.ofString(json))
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("PUT  {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status < 200 || status >= 300) {
        throw new ApiClientException("PUT " + path + " failed with HTTP " + status + " body: " + body);
      }

      return mapper.readValue(body, Appetizer.class);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during updateAppetizer id=" + id, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during updateAppetizer id=" + id, e);
    }
  }

  /**
   * @brief DELETE appetizer by ID.
   *        Deletes an existing appetizer in the backend.
   *
   * @param id [Long] ID of the appetizer to delete.
   */
  public void deleteAppetizer(Long id) {
    String path = API_APPETIZERS + id;
    try {
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .DELETE()
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("DELETE {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status < 200 || status >= 300) {
        throw new ApiClientException("DELETE " + path + " failed with HTTP " + status + " body: " + body);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during deleteAppetizer id=" + id, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during deleteAppetizer id=" + id, e);
    }
  }

  // ---------------------------------------------------------------------------
  // READ / UPDATE methods for Cashiers
  // ---------------------------------------------------------------------------

  /**
   * GET all cashiers.
   *
   * Retrieves a list of all cashiers from the backend.
   *
   * @return [List<Cashier>] List of all cashiers.
   */
  public List<Cashier> getAllCashiers() {
    return getList("/api/cashiers", new TypeReference<List<Cashier>>() {});
  }

  /**
   * @brief GET cashier by ID.
   *        Retrieves a cashier by its ID from the backend.
   *
   * @param id [Long] ID of the cashier.
   * @return [Cashier] Cashier object.
   */
  public Cashier getCashierById(Long id) {
    return get("/api/cashiers/" + id, Cashier.class);
  }

  /**
   * @brief GET cashier by name.
   *        Retrieves a cashier by its username from the backend.
   *
   * @param name [String] username of the cashier.
   * @return [Cashier] Cashier object.
   */
  public Cashier getCashierByName(String name) {
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
   */
  public Cashier updateCashier(Long id, Cashier cashier) {
    String path = "/api/cashiers/" + id;
    try {
      String json = mapper.writeValueAsString(cashier);

      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .PUT(HttpRequest.BodyPublishers.ofString(json))
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("PUT  {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status < 200 || status >= 300) {
        throw new ApiClientException("PUT " + path + " failed with HTTP " + status + " body: " + body);
      }

      return mapper.readValue(body, Cashier.class);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during updateCashier id=" + id, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during updateCashier id=" + id, e);
    }
  }

  // ---------------------------------------------------------------------------
  // CRUD methods for Drinks
  // ---------------------------------------------------------------------------

  /**
   * @brief GET all drinks.
   *
   *         Retrieves a list of all drinks from the backend.
   *
   * @return [List<Drink>] List of all drinks.
   */
  public List<Drink> getAllDrinks() {
    return getList("/api/drinks", new TypeReference<List<Drink>>() {});
  }

  /**
   * @brief GET drink by ID.
   *
   *        Retrieves a drink by its ID from the backend.
   *
   * @param id [Long] ID of the drink.
   * @return [Drink] Drink object.
   */
  public Drink getDrinkById(Long id) {
    return get(API_DRINKS + id, Drink.class);
  }

  /**
   * @brief POST create new drink.
   *
   *        Creates a new drink in the backend.
   *
   * @param drink [Drink] Drink object to create.
   * @return [Drink] Created Drink object returned from the backend.
   */
  public Drink createDrink(Drink drink) {
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
   */
  public Drink updateDrink(Long id, Drink drink) {
    String path = API_DRINKS + id;
    try {
      String json = mapper.writeValueAsString(drink);

      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .PUT(HttpRequest.BodyPublishers.ofString(json))
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("PUT  {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status < 200 || status >= 300) {
        throw new ApiClientException("PUT " + path + " failed with HTTP " + status + " body: " + body);
      }

      return mapper.readValue(body, Drink.class);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during updateDrink id=" + id, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during updateDrink id=" + id, e);
    }
  }

  /**
   * @brief DELETE drink by ID.
   *
   *        Deletes an existing drink in the backend.
   *
   * @param id [Long] ID of the drink to delete.
   */
  public void deleteDrink(Long id) {
    String path = API_DRINKS + id;
    try {
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .DELETE()
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("DELETE {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status < 200 || status >= 300) {
        throw new ApiClientException("DELETE " + path + " failed with HTTP " + status + " body: " + body);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during deleteDrink id=" + id, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during deleteDrink id=" + id, e);
    }
  }

  // ---------------------------------------------------------------------------
  // CRUD methods for MainCourses
  // ---------------------------------------------------------------------------

  /**
   * @brief GET all maincourses.
   *
   *        Retrieves a list of all maincourses from the backend.
   *
   * @return [List<MainCourse>] List of all maincourses.
   */
  public List<MainCourse> getAllMainCourses() {
    return getList("/api/maincourses", new TypeReference<List<MainCourse>>() {});
  }

  /**
   * @brief GET maincourse by ID.
   *
   *        Retrieves a maincourse by its ID from the backend.
   *
   * @param id [Long] ID of the maincourse.
   * @return [MainCourse] MainCourse object.
   */
  public MainCourse getMainCourseById(Long id) {
    return get(API_MAINCOURSES + id, MainCourse.class);
  }

  /**
   * @brief POST create new maincourse.
   *
   *        Creates a new maincourse in the backend.
   *
   * @param mainCourse [MainCourse] MainCourse object to create.
   * @return [MainCourse] Created MainCourse object returned from the backend.
   */
  public MainCourse createMainCourse(MainCourse mainCourse) {
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
   */
  public MainCourse updateMainCourse(Long id, MainCourse mainCourse) {
    String path = API_MAINCOURSES + id;
    try {
      String json = mapper.writeValueAsString(mainCourse);

      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .PUT(HttpRequest.BodyPublishers.ofString(json))
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("PUT  {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status < 200 || status >= 300) {
        throw new ApiClientException("PUT " + path + " failed with HTTP " + status + " body: " + body);
      }

      return mapper.readValue(body, MainCourse.class);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during updateMainCourse id=" + id, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during updateMainCourse id=" + id, e);
    }
  }

  /**
   * @brief DELETE maincourse by ID.
   *
   *        Deletes an existing maincourse in the backend by its ID.
   *
   * @param id [Long] ID of the maincourse to delete.
   */
  public void deleteMainCourse(Long id) {
    String path = API_MAINCOURSES + id;
    try {
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .DELETE()
          .build();

      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      int status = res.statusCode();
      String body = res.body();

      LOGGER.info("DELETE {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status < 200 || status >= 300) {
        throw new ApiClientException("DELETE " + path + " failed with HTTP " + status + " body: " + body);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during deleteMainCourse id=" + id, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during deleteMainCourse id=" + id, e);
    }
  }

  // ---------------------------------------------------------------------------
  // Authentication methods
  // ---------------------------------------------------------------------------

  /**
   * @brief Legacy login method kept for compatibility.
   *
   *        If authentication is added in the future, it can be implemented here.
   *
   * @param ignored [String] Ignored parameter.
   */
  public void login(String ignored) {
    // No-op: kept for compatibility.
  }

  /**
   * @brief Authenticates a user against the backend.
   *
   *        Sends a POST request to "/api/login" with username and password.
   *        If the response status is 200, it returns the User parsed from JSON.
   *        For any other status code it throws ApiClientException.
   *
   * @param username [String] Username entered by the user.
   * @param password [String] Password entered by the user.
   * @return [User] Authenticated User object.
   */
  public User login(String username, String password) {
    String path = API_LOGIN;
    try {
      String jsonBody = mapper.writeValueAsString(Map.of(
          "username", username,
          "password", password));

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
          .build();

      HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

      int status = response.statusCode();
      String body = response.body();

      LOGGER.info("POST {} -> HTTP {}", path, status);
      LOGGER.info("Response body: '{}'", body);

      if (status == 200) {
        return mapper.readValue(body, User.class);
      } else {
        throw new ApiClientException("Login failed with status: " + status + " body: " + body);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ApiClientException("Thread interrupted during login for user=" + username, e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error during login for user=" + username, e);
    }
  }
}
