package es.ull.esit.server.middleware.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @brief JPA entity that represents a user in the system.
 * 
 *        It is mapped to the "users" table in the database.
 *        Stores user information such as username, password hash, and role.
 */
@Entity
@Table(name = "users")
public class User {

  /** Unique identifier for the user (primary key). */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Unique username for the user. */
  @Column(unique = true, nullable = false)
  private String username;

  /** BCrypt hashed password for the user. */
  @Column(nullable = false)
  @JsonIgnore
  private String passwordHash;

  /** Role of the user: ADMIN or CASHIER. */
  private String role;

  /**
   * @brief Gets the database identifier of the user.
   * 
   * @return [Long] The user's ID.
   */
  public Long getId() {
    return id;
  }

  /**
   * @brief Sets the database identifier of the user.
   * 
   * @param id [Long] The user's ID.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @brief Gets the user's username.
   * 
   * @return [String] The user's username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * @brief Sets the user's username.
   * 
   * @param username [String] The user's username.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @brief Gets the BCrypt hashed password of the user.
   * 
   * @return [String] The user's BCrypt hashed password.
   */
  public String getPasswordHash() {
    return passwordHash;
  }

  /**
   * @brief Sets the BCrypt hashed password of the user.
   * 
   * @param passwordHash [String] The user's BCrypt hashed password.
   */
  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  /**
   * @brief Gets the role of the user.
   * 
   * @return [String] The user's role.
   */
  public String getRole() {
    return role;
  }

  /**
   * @brief Sets the role of the user.
   * 
   * @param role [String] The user's role.
   */
  public void setRole(String role) {
    this.role = role;
  }
}