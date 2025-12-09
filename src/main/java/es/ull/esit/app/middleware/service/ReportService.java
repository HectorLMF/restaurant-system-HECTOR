package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Cashier;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import java.util.List;

/**
 * @brief Service providing reporting and system status operations.
 *
 *        It offers methods to retrieve cashier information and to verify that
 *        the menu endpoints of the backend are accessible.
 */
public class ReportService {

  /** REST API client used to communicate with the backend. */
  private final ApiClient client;

  /**
   * @brief Constructs a ReportService with the given API client.
   *
   * @param client [ApiClient] Client used to perform HTTP requests.
   */
  public ReportService(ApiClient client) {
    this.client = client;
  }

  /**
   * @brief Loads information about all registered cashiers.
   *
   *        Uses the "/api/cashiers" endpoint to obtain the list.
   *
   * @return [List<Cashier>] List of cashiers returned by the backend.
   * @throws Exception If an error occurs while contacting the backend.
   */
  public List<Cashier> getCashierInfo() throws Exception {
    return client.getAllCashiers();
  }

  /**
   * @brief Checks backend connectivity and counts menu items.
   *
   *        Performs GET requests to appetizers, drinks and main courses endpoints
   *        and returns a textual summary of the available items.
   *
   * @return [String] Human-readable status message about the menu system.
   * @throws Exception If any of the API calls fail.
   */
  public String checkMenuStatus() throws Exception {
    // Fetch lists to verify connectivity.
    List<Appetizer> appetizers = client.getAllAppetizers();
    List<Drink> drinks = client.getAllDrinks();
    List<MainCourse> mainCourses = client.getAllMainCourses();

    return String.format(
        "Menu System Online: %d appetizers, %d drinks, %d main courses available.",
        appetizers.size(), drinks.size(), mainCourses.size());
  }
}
