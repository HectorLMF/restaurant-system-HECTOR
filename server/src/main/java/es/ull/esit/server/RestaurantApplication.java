package es.ull.esit.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @brief Main Spring Boot application class for the restaurant backend.
 *
 *        Bootstraps the Spring Boot application.
 *        Enables component scanning, auto-configuration and starts the embedded
 *        web server that
 *        exposes the REST controllers defined in the project.
 *
 *        The Swing frontend communicates with this backend via the HTTP
 *        endpoints
 *        provided by the controllers (Drinks, Appetizers, MainCourse, Menu,
 *        etc.).
 */
@SpringBootApplication
public class RestaurantApplication {

  /**
   * @brief Main entry point for the Spring Boot backend.
   *
   *        Delegates to SpringApplication.run(), which starts the application
   *        context, configures the environment and launches the embedded server.
   *
   * @param args [String[]] Command-line arguments (not used).
   */
  public static void main(String[] args) {
    SpringApplication.run(RestaurantApplication.class, args);
  }
}
