package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.User;

/**
 * @brief Service responsible for handling authentication logic on the client side.
 *
 *        Performs local validation of credentials and delegates the actual login
 *        process to the ApiClient.
 */
public class AuthService {

  /** REST API client used to communicate with the backend login endpoint. */
  private final ApiClient client;

  /**
   * @brief Constructs an AuthService with the given API client.
   *
   * @param client [ApiClient] Client used to perform HTTP requests to the backend.
   */
  public AuthService(ApiClient client) {
    this.client = client;
  }

  /**
   * @brief Attempts to authenticate a user against the backend.
   *
   *        Validates username and password locally, then calls
   *        ApiClient.login(username, password). If validation fails or the
   *        server rejects the credentials, an exception is thrown.
   *
   * @param username [String] Username entered by the user.
   * @param password [String] Password entered by the user.
   * @return [User] Authenticated user object returned by the backend.
   * @throws Exception If validation fails or the backend returns an error.
   */
  public User authenticate(String username, String password) throws Exception {
    // 1. Local Validation.
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be empty.");
    }
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be empty.");
    }

    // 2. Call API.
    return client.login(username, password);
  }
}
