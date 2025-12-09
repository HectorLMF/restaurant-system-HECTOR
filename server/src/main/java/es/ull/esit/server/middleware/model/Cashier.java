package es.ull.esit.server.middleware.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @brief JPA entity that represents a cashier in the system.
 *
 *        It is mapped to the "cashier" table used by the REST API.
 *        Each row contains a unique identifier, name (username), and salary.
 */
@Entity
@Table(name = "cashier")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cashier {

  /** Primary key (unique identifier) of the cashier (column "cashier_id"). */
  @Id
  @Column(name = "cashier_id")
  private Long id;

  /** Cashier username (column "cashier_name"). */
  @Column(name = "cashier_name")
  private String name;

  /** Cashier salary (column "cashier_salary"). */
  @Column(name = "cashier_salary")
  private Integer salary;

  /**
   * Default constructor required by JPA.
   */
  public Cashier() {
  }

  /**
   * @brief Full constructor.
   * 
   * @param id     [Long] Identifier of the cashier.
   * @param name   [String] Username of the cashier.
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
   * @return [Long] Cashier id.
   */
  public Long getId() {
    return id;
  }

  /**
   * @brief Sets the cashier identifier.
   *        Must be unique.
   * 
   * @param id [Long] New cashier id.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @brief Gets the cashier name (username).
   * 
   * @return [String] Cashier name (username).
   */
  public String getName() {
    return name;
  }

  /**
   * @brief Sets the cashier name (username).
   * 
   * @param name [String] New cashier name (username).
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @brief Gets the cashier salary.
   * 
   * @return [Integer] cashier salary.
   */
  public Integer getSalary() {
    return salary;
  }

  /**
   * @brief Sets the cashier salary.
   * 
   * @param salary [Integer] New cashier salary.
   */
  public void setSalary(Integer salary) {
    this.salary = salary;
  }
}