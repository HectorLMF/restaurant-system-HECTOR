package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @brief Repository interface for accessing users in the database.
 *
 * Extends JpaRepository to provide standard CRUD operations on the "users" table 
 * and defines an extra method to find a user by username.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * @brief Finds a user by their unique username.
   *
   * @param username [String] The unique username of the user to find.
   * @return [Optional<User>] An Optional containing the User if found, or empty if not found.
   */
  Optional<User> findByUsername(String username);
}