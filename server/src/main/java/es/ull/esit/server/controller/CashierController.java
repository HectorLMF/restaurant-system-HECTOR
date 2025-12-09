package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Cashier;
import es.ull.esit.server.repo.CashierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * @brief REST controller for managing cashiers.
 * 
 *        Provides CRUD operations for Cashier entities using the path
 *        "/api/cashiers".
 * 
 *        Cashiers are created automatically when users with 'CASHIER' role are
 *        added to the table 'users'.
 *        This controller is mainly READ-ONLY and allows updating salary/name.
 * 
 */
@RestController
@RequestMapping("/api/cashiers")
public class CashierController {

  /* Repository used to perform database operations for Cashier entities. */
  @Autowired
  private CashierRepository cashierRepository;

  /**
   * @brief Returns all cashiers.
   * 
   *        HTTP GET /api/cashiers
   * 
   * @return [ResponseEntity<List<Cashier>>] List of cashiers with HTTP 200
   *         status.
   */
  @GetMapping
  public ResponseEntity<List<Cashier>> getAllCashiers() {
    List<Cashier> cashiers = cashierRepository.findAll();
    return ResponseEntity.ok(cashiers);
  }

  /**
   * @brief Returns a single cashier by ID.
   * 
   *        HTTP GET /api/cashiers/{id}
   * 
   * @param id [Long] Identifier of the cashier.
   * @return [ResponseEntity<Cashier>] The cashier if found with HTTP 200 status,
   *         or HTTP 404 if not found.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Cashier> getCashierById(@PathVariable Long id) {
    Optional<Cashier> cashier = cashierRepository.findById(id);
    return cashier
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * @brief Returns a single cashier by name (username).
   * 
   *        HTTP GET /api/cashiers/name/{name}
   * 
   * @param name [String] Name of the cashier.
   * @return [ResponseEntity<Cashier>] The cashier if found with HTTP 200 status,
   *         or HTTP 404 if not found.
   */
  @GetMapping("/name/{name}")
  public ResponseEntity<Cashier> getCashierByName(@PathVariable String name) {
    Optional<Cashier> cashier = cashierRepository.findByName(name);
    return cashier
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * @brief Updates an existing cashier, but only the name and salary fields.
   * 
   *        HTTP PUT /api/cashiers/{id}
   * 
   * @param id      [Long] Identifier of the cashier to update.
   * @param cashier [Cashier] New data for the cashier.
   * @return [ResponseEntity<Cashier>] The updated cashier with HTTP 200 status,
   *         or HTTP 404 if not found.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Cashier> updateCashier(@PathVariable Long id,
      @RequestBody Cashier cashier) {
    Optional<Cashier> existingOpt = cashierRepository.findById(id);
    if (!existingOpt.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Cashier existing = existingOpt.get();
    existing.setName(cashier.getName());
    existing.setSalary(cashier.getSalary());

    Cashier updated = cashierRepository.save(existing);
    return ResponseEntity.ok(updated);
  }

}