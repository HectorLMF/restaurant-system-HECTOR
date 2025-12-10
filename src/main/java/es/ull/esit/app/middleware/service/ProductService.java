package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import java.util.List;

/**
 * @brief Service handling business logic for menu products.
 *
 *        Decouples Swing UI components from direct REST API calls. It centralizes
 *        validation and orchestrates requests for drinks, appetizers and main
 *        courses.
 */
public class ProductService {

  /** REST API client used to communicate with the backend menu endpoints. */
  private final ApiClient client;

  /**
   * @brief Constructs a ProductService with the given API client.
   *
   * @param client [ApiClient] REST client used to perform HTTP requests.
   */
  public ProductService(ApiClient client) {
    this.client = client;
  }

  // ==================== DRINKS LOGIC ====================

  /**
   * @brief Retrieves all drinks from the backend.
   *
   * @return [List<Drink>] List of all drinks available in the menu.
   */
  public List<Drink> getAllDrinks() {
    return client.getAllDrinks();
  }

  /**
   * @brief Retrieves a single drink by its identifier.
   *
   * @param id [Long] Unique identifier of the drink.
   * @return [Drink] Drink object corresponding to the given id.
   */
  public Drink getDrinkById(Long id) {
    return client.getDrinkById(id);
  }

  /**
   * @brief Adds a new drink to the backend menu.
   *
   *        Validates the user input and sends a POST request using the ApiClient.
   *
   * @param name     [String] Name of the new drink.
   * @param priceStr [String] Price entered as a string.
   */
  public void addDrink(String name, String priceStr) {
    int price = validateAndParsePrice(priceStr);
    validateName(name);

    Drink newDrink = new Drink(null, name, price, null);
    client.createDrink(newDrink);
  }

  /**
   * @brief Updates an existing drink in the backend menu.
   *
   * @param id       [Long] Identifier of the drink to update.
   * @param name     [String] New name for the drink.
   * @param priceStr [String] New price entered as a string.
   */
  public void updateDrink(Long id, String name, String priceStr) {
    if (id == null) {
      throw new IllegalArgumentException("No drink selected!");
    }

    int price = validateAndParsePrice(priceStr);
    validateName(name);

    Drink updatedDrink = new Drink(id, name, price, null);
    client.updateDrink(id, updatedDrink);
  }

  // ==================== APPETIZERS LOGIC ====================

  /**
   * @brief Retrieves all appetizers from the backend.
   *
   * @return [List<Appetizer>] List of all appetizers in the menu.
   */
  public List<Appetizer> getAllAppetizers() {
    return client.getAllAppetizers();
  }

  /**
   * @brief Retrieves a single appetizer by its identifier.
   *
   * @param id [Long] Unique identifier of the appetizer.
   * @return [Appetizer] Appetizer object corresponding to the given id.
   */
  public Appetizer getAppetizerById(Long id) {
    return client.getAppetizerById(id);
  }

  /**
   * @brief Adds a new appetizer to the backend menu.
   *
   *        Validates user input and sends a POST request using the ApiClient.
   *
   * @param name     [String] Name of the new appetizer.
   * @param priceStr [String] Price entered as a string.
   */
  public void addAppetizer(String name, String priceStr) {
    int price = validateAndParsePrice(priceStr);
    validateName(name);

    Appetizer newAppetizer = new Appetizer(null, name, price, null);
    client.createAppetizer(newAppetizer);
  }

  /**
   * @brief Updates an existing appetizer in the backend menu.
   *
   * @param id       [Long] Identifier of the appetizer to update.
   * @param name     [String] New name for the appetizer.
   * @param priceStr [String] New price entered as a string.
   */
  public void updateAppetizer(Long id, String name, String priceStr) {
    if (id == null) {
      throw new IllegalArgumentException("No appetizer selected!");
    }

    int price = validateAndParsePrice(priceStr);
    validateName(name);

    Appetizer updatedAppetizer = new Appetizer(id, name, price, null);
    client.updateAppetizer(id, updatedAppetizer);
  }

  // ==================== MAIN COURSE LOGIC ====================

  /**
   * @brief Retrieves all main courses from the backend.
   *
   * @return [List<MainCourse>] List of all main courses in the menu.
   */
  public List<MainCourse> getAllMainCourses() {
    return client.getAllMainCourses();
  }

  /**
   * @brief Retrieves a single main course by its identifier.
   *
   * @param id [Long] Unique identifier of the main course.
   * @return [MainCourse] MainCourse object corresponding to the given id.
   */
  public MainCourse getMainCourseById(Long id) {
    return client.getMainCourseById(id);
  }

  /**
   * @brief Adds a new main course to the backend menu.
   *
   *        Validates user input and sends a POST request using the ApiClient.
   *
   * @param name     [String] Name of the new main course.
   * @param priceStr [String] Price entered as a string.
   */
  public void addMainCourse(String name, String priceStr) {
    int price = validateAndParsePrice(priceStr);
    validateName(name);

    MainCourse newMainCourse = new MainCourse(null, name, price, null);
    client.createMainCourse(newMainCourse);
  }

  /**
   * @brief Updates an existing main course in the backend menu.
   *
   * @param id       [Long] Identifier of the main course to update.
   * @param name     [String] New name for the main course.
   * @param priceStr [String] New price entered as a string.
   */
  public void updateMainCourse(Long id, String name, String priceStr) {
    if (id == null) {
      throw new IllegalArgumentException("No main course selected!");
    }

    int price = validateAndParsePrice(priceStr);
    validateName(name);

    MainCourse updatedMainCourse = new MainCourse(id, name, price, null);
    client.updateMainCourse(id, updatedMainCourse);
  }

  // ==================== VALIDATION HELPERS ====================

  /**
   * @brief Validates that the item name is not null or empty.
   *
   * @param name [String] Name of the item to validate.
   * @throws IllegalArgumentException If the name is null or empty.
   */
  private void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Item name cannot be empty.");
    }
  }

  /**
   * @brief Validates and parses the price from string to integer.
   *
   *        Ensures the price is a non-negative integer value.
   *
   * @param priceStr [String] Price entered as a string.
   * @return [int] Parsed price value.
   * @throws IllegalArgumentException If the price is empty, negative or not a valid number.
   */
  private int validateAndParsePrice(String priceStr) {
    if (priceStr == null || priceStr.trim().isEmpty()) {
      throw new IllegalArgumentException("Price cannot be empty.");
    }
    try {
      int price = Integer.parseInt(priceStr.trim());
      if (price < 0) {
        throw new IllegalArgumentException("Price cannot be negative.");
      }
      return price;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Price must be a valid whole number.");
    }
  }
}
