package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @brief Client-side model representing an authenticated user.
 *
 *        It maps the JSON returned by the "/api/login" endpoint and contains
 *        only the fields needed by the Swing application: username and role.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  /** Username of the authenticated user (JSON property "username"). */
  @JsonProperty("username")
  private String username;

  /** Role of the user (JSON property "role"), e.g., ADMIN or CASHIER. */
  @JsonProperty("role")
  private String role;

  /**
   * @brief Default constructor required for JSON deserialization.
   */
  public User() {
  }

  /**
   * @brief Constructs a user with the given username and role.
   *
   * @param username [String] Username of the user.
   * @param role     [String] Role of the user.
   */
  public User(String username, String role) {
    this.username = username;
    this.role = role;
  }

  /**
   * @brief Gets the username.
   *
   * @return [String] Username of the user.
   */
  public String getUsername() {
    return username;
  }

  /**
   * @brief Sets the username.
   *
   * @param username [String] Username of the user.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @brief Gets the user role.
   *
   * @return [String] Role of the user.
   */
  public String getRole() {
    return role;
  }

  /**
   * @brief Sets the user role.
   *
   * @param role [String] Role of the user.
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * @brief Checks if the user has the ADMIN role.
   *
   * @return [boolean] True if the user role is ADMIN (case-insensitive), false otherwise.
   */
  public boolean isAdmin() {
    return "ADMIN".equalsIgnoreCase(this.role);
  }
}
