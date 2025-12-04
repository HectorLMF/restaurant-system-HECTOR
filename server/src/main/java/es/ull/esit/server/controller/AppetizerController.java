package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Appetizer;
import es.ull.esit.server.repo.AppetizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @brief REST controller for managing appetizers.
 * 
 * Rovides CRUD operations for Appetizer entities using the path "/api/appetizers".
 *       These endpoints are used by the Swing client to load and modify appetizer information.
 * 
 */
@RestController
@RequestMapping("/api/appetizers")
public class AppetizerController {

  /** Repository used to perform database operations on appetizers. */
  @Autowired
  private AppetizerRepository appetizerRepository;

  /**
   * @brief Returns the complete list of appetizers.
   * 
   *        HTTP GET /api/appetizers
   * 
   * @return [ResponseEntity<List<Appetizer>>] List of appetizers with status 200.
   */
  @GetMapping
  public ResponseEntity<List<Appetizer>> getAllAppetizers() {
    List<Appetizer> appetizers = appetizerRepository.findAll();
    return ResponseEntity.ok(appetizers);
  }

  /**
   * @brief Returns a single appetizer by its id.
   * 
   *        HTTP GET /api/appetizers/{id}
   * 
   * @param id [Long] Identifier of the appetizer.
   * @return [ResponseEntity<Appetizer>] The appetizer if found or 404 otherwise.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Appetizer> getAppetizerById(@PathVariable Long id) {
    Optional<Appetizer> appetizer = appetizerRepository.findById(id);
    return appetizer
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * @brief Creates a new appetizer.
   * 
   *        HTTP POST /api/appetizers
   * 
   * @param appetizer [Appetizer] Appetizer to create.
   * @return [ResponseEntity<Appetizer>] The created appetizer with status 201.
   */
  @PostMapping
  public ResponseEntity<Appetizer> createAppetizer(@RequestBody Appetizer appetizer) {
    Appetizer saved = appetizerRepository.save(appetizer);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  /**
   * @brief Updates an existing appetizer.
   * 
   *        HTTP PUT /api/appetizers/{id}
   * 
   * @param id [Long] Identifier of the appetizer to update.
   * @param appetizer [Appetizer] Updated appetizer data.
   * @return [ResponseEntity<Appetizer>] The updated appetizer or 404 if not found.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Appetizer> updateAppetizer(@PathVariable Long id,
                                                   @RequestBody Appetizer appetizer) {
    if (!appetizerRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    appetizer.setAppetizersId(id);
    Appetizer updated = appetizerRepository.save(appetizer);
    return ResponseEntity.ok(updated);
  }

  /**
   * @brief Deletes an appetizer by its id.
   * 
   *        HTTP DELETE /api/appetizers/{id}
   * 
   * @param id [Long] Identifier of the appetizer to delete.
   * @return [ResponseEntity<Void>] 204 if deleted or 404 if not found.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAppetizer(@PathVariable Long id) {
    if (!appetizerRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    appetizerRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
