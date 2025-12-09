package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Appetizer;
import es.ull.esit.server.middleware.model.Drink;
import es.ull.esit.server.middleware.model.MainCourse;
import es.ull.esit.server.repo.AppetizerRepository;
import es.ull.esit.server.repo.DrinkRepository;
import es.ull.esit.server.repo.MainCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @brief REST controller that exposes a consolidated restaurant menu.
 *
 *        Provides CRUD operation endpoints to retrieve the full menu in a
 *        single
 *        HTTP request. It aggregates main courses, appetizers and drinks into a
 *        single JSON response.
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {

  /** Repository for main course entities. */
  @Autowired
  private MainCourseRepository mainCourseRepository;

  /** Repository for appetizer entities. */
  @Autowired
  private AppetizerRepository appetizerRepository;

  /** Repository for drink entities. */
  @Autowired
  private DrinkRepository drinkRepository;

  /**
   * @brief Returns the full menu (main courses, appetizers and drinks).
   *
   *        Endpoint: GET /api/menu
   *
   *        The response body is a JSON object with the following keys:
   *        - "mainCourses": list of MainCourse
   *        - "appetizers": list of Appetizer
   *        - "drinks": list of Drink
   *        - "totalItems": integer with the total count of menu items
   *
   * @return ResponseEntity containing the aggregated menu and HTTP 200.
   */
  @GetMapping
  public ResponseEntity<Map<String, Object>> getFullMenu() {
    Map<String, Object> menu = new HashMap<>();

    List<MainCourse> mainCourses = mainCourseRepository.findAll();
    List<Appetizer> appetizers = appetizerRepository.findAll();
    List<Drink> drinks = drinkRepository.findAll();

    menu.put("mainCourses", mainCourses);
    menu.put("appetizers", appetizers);
    menu.put("drinks", drinks);
    menu.put("totalItems", mainCourses.size() + appetizers.size() + drinks.size());

    return ResponseEntity.ok(menu);
  }
}
