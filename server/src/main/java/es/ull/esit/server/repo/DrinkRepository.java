package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Repository interface for managing drinks in the database.
 *
 *        Extends JpaRepository to provide basic CRUD operations on the "drinks"
 *        table, such as findAll, findById, save and delete.
 */
public interface DrinkRepository extends JpaRepository<Drink, Long> {
  // No extra methods are added for now, but custom queries can be defined here if
  // needed in the future.
}
