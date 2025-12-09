package es.ull.esit.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @brief REST controller for health and database connectivity checks.
 *
 *        Exposes simple diagnostic endpoints to:
 *        - receive basic service liveness information.
 *        - check connectivity with the underlying database.
 *        Not required by the Swing frontend but are very
 *        useful for debugging, monitoring or for external tools that need to
 *        verify that the service and the database are up and running.
 */
@RestController
@RequestMapping("/api")
public class HealthController {

  /** DataSource used to verify connectivity with the database. */
  @Autowired
  private DataSource dataSource;

  /**
   * @brief Basic health endpoint.
   *
   *        Endpoint: GET /api/health
   *
   *        Returns a small JSON payload indicating that the service is up,
   *        together with a timestamp and a human-readable service name.
   *
   * @return [ResponseEntity<Map<String, Object>>] Health status with HTTP 200.
   */
  @GetMapping("/health")
  public ResponseEntity<Map<String, Object>> health() {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "UP");
    response.put("timestamp", System.currentTimeMillis());
    response.put("service", "Restaurant Server");
    return ResponseEntity.ok(response);
  }

  /**
   * @brief Database connectivity check.
   *
   *        Endpoint: GET /api/db-check
   *
   *        Tries to obtain a JDBC connection from the configured DataSource
   *        and verifies that it is valid. If the connection is valid, information
   *        about the database catalog and URL is included in the response.
   *
   *        On success:
   *        - HTTP 200 with JSON fields "status" = "UP" and "database" =
   *        "Connected".
   *
   *        On failure:
   *        - HTTP 503 (SERVICE_UNAVAILABLE) with "status" = "DOWN" and an
   *        additional "error" message describing the issue.
   *
   * @return [ResponseEntity<Map<String, Object>>] Database connectivity status:
   *         HTTP 200 if connected, HTTP 503 otherwise.
   */
  @GetMapping("/db-check")
  public ResponseEntity<Map<String, Object>> checkDatabase() {
    Map<String, Object> response = new HashMap<>();

    try (Connection conn = dataSource.getConnection()) {
      boolean isValid = conn.isValid(2);

      if (isValid) {
        response.put("status", "UP");
        response.put("database", "Connected");
        response.put("catalog", conn.getCatalog());
        response.put("url", conn.getMetaData().getURL());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
      } else {
        response.put("status", "DOWN");
        response.put("database", "Connection not valid");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
      }
    } catch (Exception e) {
      response.put("status", "DOWN");
      response.put("database", "Connection failed");
      response.put("error", e.getMessage());
      response.put("timestamp", System.currentTimeMillis());
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
  }
}
