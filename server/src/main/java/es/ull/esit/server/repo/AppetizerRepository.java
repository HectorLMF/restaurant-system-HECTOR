package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.Appetizer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Repository interface for managing appetizers in the database.
 *
 *        Extends JpaRepository to provide basic CRUD operations on the "appetizers"
 *        table, such as findAll, findById, save and delete.
 */
public interface AppetizerRepository extends JpaRepository<Appetizer, Long> {
  // No extra methods are added for now, but custom queries can be defined here if
  // needed in the future.
}
