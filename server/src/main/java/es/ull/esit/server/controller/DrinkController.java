package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Drink;
import es.ull.esit.server.repo.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @brief REST controller for managing drinks.
 *
 *        Provides CRUD operations for Drink entities using the path
 *        "/api/drinks". These endpoints are used by the Swing client to load
 *        and modify drink information.
 */
@RestController
@RequestMapping("/api/drinks")
public class DrinkController {

  /** Repository used to access the "drinks" table. */
  @Autowired
  private DrinkRepository drinkRepository;

  /**
   * @brief Returns the complete list of drinks.
   *
   *        HTTP GET /api/drinks
   *
   * @return [ResponseEntity<List<Drink>>] List of drinks with status 200.
   */
  @GetMapping
  public ResponseEntity<List<Drink>> getAllDrinks() {
    List<Drink> drinks = drinkRepository.findAll();
    return ResponseEntity.ok(drinks);
  }

  /**
   * @brief Returns a single drink by its id.
   *
   *        HTTP GET /api/drinks/{id}
   *
   * @param id [Long] Identifier of the drink.
   * @return [ResponseEntity<Drink>] The drink if found or 404 otherwise.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Drink> getDrinkById(@PathVariable Long id) {
    Optional<Drink> drink = drinkRepository.findById(id);
    return drink.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * @brief Creates a new drink.
   *
   *        HTTP POST /api/drinks
   *
   * @param drink [Drink] Drink data sent in the request body.
   * @return [ResponseEntity<Drink>] The created drink with status 201.
   */
  @PostMapping
  public ResponseEntity<Drink> createDrink(@RequestBody Drink drink) {
    Drink saved = drinkRepository.save(drink);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  /**
   * @brief Updates an existing drink.
   *
   *        HTTP PUT /api/drinks/{id}
   *
   * @param id    [Long] Identifier of the drink to update.
   * @param drink [Drink] New data for the drink.
   * @return [ResponseEntity<Drink>] The updated drink or 404 if not found.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Drink> updateDrink(@PathVariable Long id,
      @RequestBody Drink drink) {
    if (!drinkRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    drink.setDrinksId(id);
    Drink updated = drinkRepository.save(drink);
    return ResponseEntity.ok(updated);
  }

  /**
   * @brief Deletes a drink by its id.
   *
   *        HTTP DELETE /api/drinks/{id}
   *
   * @param id [Long] Identifier of the drink to delete.
   * @return [ResponseEntity<Void>] 204 if deleted or 404 if not found.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDrink(@PathVariable Long id) {
    if (!drinkRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    drinkRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
