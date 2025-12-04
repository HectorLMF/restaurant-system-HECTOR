package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.MainCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Repository interface for managing main courses in the database.
 *
 *        Extends JpaRepository to provide basic CRUD operations on the
 *        "main_courses"
 *        table, such as findAll, findById, save and delete.
 */
public interface MainCourseRepository extends JpaRepository<MainCourse, Long> {
  // No extra methods are added for now, but custom queries can be defined here if
  // needed in the future.
}
