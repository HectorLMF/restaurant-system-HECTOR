package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @brief Repository interface for managing cashiers in the database.
 *
 *        Extends JpaRepository to provide basic CRUD operations on the
 *        "Cashier" table, such as findAll, findById, save and delete.
 */
public interface CashierRepository extends JpaRepository<Cashier, Long> {

  /**
   * @brief Finds a cashier by their name.
   * 
   * @param name [String] Name of the cashier.
   * @return [Optional<Cashier>] The cashier if found, or empty if not found.
   */
  Optional<Cashier> findByName(String name);
}
