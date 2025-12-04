package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.MainCourse;
import es.ull.esit.server.repo.MainCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @brief REST controller for managing main courses.
 * 
 *        Provides CRUD operations for MainCourse entities using the path
 *        /api/maincourses.
 *        These endpoints are used by the Swing client to load and modify main
 *        course information.
 */
@RestController
@RequestMapping("/api/maincourses")
public class MainCourseController {

  /** Repository used to access the "main_courses" table. */
  @Autowired
  private MainCourseRepository mainCourseRepository;

  /**
   * @brief Returns all available main course items.
   *
   *        Endpoint: GET /api/maincourses
   * 
   * @return [ResponseEntity<List<MainCourse>>] List of main courses with status
   *         200.
   */
  @GetMapping
  public ResponseEntity<List<MainCourse>> getAllMainCourses() {
    List<MainCourse> courses = mainCourseRepository.findAll();
    return ResponseEntity.ok(courses);
  }

  /**
   * @brief Returns a specific main course by its ID.
   *
   *        Endpoint: GET /api/maincourses/{id}
   * 
   * @param id [Long] Identifier of the main course.
   * @return [ResponseEntity<MainCourse>] The main course if found or 404
   *         otherwise.
   */
  @GetMapping("/{id}")
  public ResponseEntity<MainCourse> getMainCourseById(@PathVariable Long id) {
    Optional<MainCourse> course = mainCourseRepository.findById(id);
    return course
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * @brief Creates a new main course entry.
   *
   *        Endpoint: POST /api/maincourses
   * 
   * @param mainCourse [MainCourse] Main course data from the request body.
   * @return [ResponseEntity<MainCourse>] The created main course with status 201
   */
  @PostMapping
  public ResponseEntity<MainCourse> createMainCourse(@RequestBody MainCourse mainCourse) {
    MainCourse saved = mainCourseRepository.save(mainCourse);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  /**
   * @brief Updates an existing main course entry.
   *
   *        Endpoint: PUT /api/maincourses/{id}
   * 
   * @param id         [Long] Identifier of the main course to update.
   * @param mainCourse [MainCourse] Updated main course data from the request
   *                   body.
   * @return [ResponseEntity<MainCourse>] The updated main course if found or 404
   *         otherwise.
   */
  @PutMapping("/{id}")
  public ResponseEntity<MainCourse> updateMainCourse(@PathVariable Long id, @RequestBody MainCourse mainCourse) {

    if (!mainCourseRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    mainCourse.setFoodId(id);
    MainCourse updated = mainCourseRepository.save(mainCourse);
    return ResponseEntity.ok(updated);
  }

  /**
   * @brief Deletes a main course by its ID.
   *
   *        Endpoint: DELETE /api/maincourses/{id}
   * 
   * @param id [Long] Identifier of the main course to delete.
   * @return [ResponseEntity<Void>] Status 204 if deleted or 404 if not found.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMainCourse(@PathVariable Long id) {
    if (!mainCourseRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    mainCourseRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
