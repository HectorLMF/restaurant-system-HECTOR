package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @brief Client-side model representing a cashier returned by the backend.
 *
 *        It is used by the Swing application to display and manage cashier
 *        information obtained from the "/api/cashiers" endpoint.
 */
public class Cashier {

  /** Unique identifier of the cashier (JSON property "id"). */
  @JsonProperty("id")
  private Long id;

  /** Cashier name (JSON property "name"). */
  @JsonProperty("name")
  private String name;

  /** Cashier salary (JSON property "salary"). */
  @JsonProperty("salary")
  private Integer salary;

  /**
   * @brief Default constructor required for JSON deserialization.
   */
  public Cashier() {
  }

  /**
   * @brief Constructs a cashier with all fields.
   *
   * @param id     [Long] Unique identifier of the cashier.
   * @param name   [String] Name of the cashier.
   * @param salary [Integer] Salary of the cashier.
   */
  public Cashier(Long id, String name, Integer salary) {
    this.id = id;
    this.name = name;
    this.salary = salary;
  }

  /**
   * @brief Gets the cashier identifier.
   *
   * @return [Long] Unique identifier of the cashier.
   */
  public Long getId() {
    return id;
  }

  /**
   * @brief Sets the cashier identifier.
   *
   * @param id [Long] Unique identifier of the cashier.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @brief Gets the cashier name.
   *
   * @return [String] Name of the cashier.
   */
  public String getName() {
    return name;
  }

  /**
   * @brief Sets the cashier name.
   *
   * @param name [String] Name of the cashier.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @brief Gets the cashier salary.
   *
   * @return [Integer] Salary of the cashier.
   */
  public Integer getSalary() {
    return salary;
  }

  /**
   * @brief Sets the cashier salary.
   *
   * @param salary [Integer] Salary of the cashier.
   */
  public void setSalary(Integer salary) {
    this.salary = salary;
  }
}
