package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.User;
import es.ull.esit.server.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @brief Rest controller for handling authentication-related requests.
 * 
 *        Exposes an HTTP endpoint that allows clients to log in by sending
 *        a username and password. The credentials are validated against the
 *        users stored in the database.
 */
@RestController
@RequestMapping("/api")
public class AuthController {

  /** Logger for logging authentication events. */
  private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

  /** Repository for accessing user data. */
  @Autowired
  private UserRepository userRepository;

  /** Component used to verify raw passwords against stored hashes. */
  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * @brief Simple DTO (Data Transfer Object) for login requests payload.
   * 
   *        It is populated automatically from the JSON request body of the HTTP
   *        request.
   */
  public static class LoginRequest {
    public String username;
    public String password;
  }

  /**
   * @brief Endpoint for handling user login requests:
   *        - receives a username and password in the request body.
   *        - searches for the user in the database.
   *        - compares the provided password with the stored password hash.
   *        - return 200 OK with user data if credentials are valid.
   *        - return 401 Unauthorized if credentials are invalid.
   * 
   * @param request [LoginRequest] Login request payload containing username and
   *                password.
   * @return [ResponseEntity] HTTP response indicating success or failure of
   *         authentication.
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {

    LOG.info("Login attempt for user: {}", request.username);

    Optional<User> userOpt = userRepository.findByUsername(request.username);

    if (!userOpt.isPresent()) {
      LOG.warn("User not found in DB: {}", request.username);
      return ResponseEntity.status(401).body("Invalid credentials");
    }

    User user = userOpt.get();
    LOG.debug("User found. Stored hash = {}", user.getPasswordHash()); // Mejor debug, no info

    if (!passwordEncoder.matches(request.password, user.getPasswordHash())) {
      LOG.warn("Password mismatch for user: {}", request.username);
      return ResponseEntity.status(401).body("Invalid credentials");
    }

    LOG.info("Login OK for user: {}", request.username);

    return ResponseEntity.ok(user);

  }

}