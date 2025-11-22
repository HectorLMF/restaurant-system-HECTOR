package es.ull.esit.server.controller;

import es.ull.esit.server.repo.AppetizerRepository;
import es.ull.esit.server.repo.DrinkRepository;
import es.ull.esit.server.repo.MainCourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MenuController {

    private final MainCourseRepository mainRepo;
    private final AppetizerRepository appetizerRepo;
    private final DrinkRepository drinkRepo;

    @Autowired
    public MenuController(MainCourseRepository mainRepo,
                          AppetizerRepository appetizerRepo,
                          DrinkRepository drinkRepo) {
        this.mainRepo = mainRepo;
        this.appetizerRepo = appetizerRepo;
        this.drinkRepo = drinkRepo;
    }

    @GetMapping("/menu")
    public ResponseEntity<Map<String, Object>> menu() {
        Map<String, Object> out = new HashMap<>();
        out.put("mains", mainRepo.findAll());
        out.put("appetizers", appetizerRepo.findAll());
        out.put("drinks", drinkRepo.findAll());
        return ResponseEntity.ok(out);
    }
}
package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Cashier;
import es.ull.esit.server.repo.CashierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CashierController {

    private final CashierRepository cashierRepo;

    @Autowired
    public CashierController(CashierRepository cashierRepo) {
        this.cashierRepo = cashierRepo;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "Mi Restaurante");
        info.put("address", "Calle Falsa 123");
        return ResponseEntity.ok(info);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String name = body.getOrDefault("name", "");
        Map<String, String> res = new HashMap<>();

        if (name == null || name.isBlank()) {
            res.put("status", "error");
            res.put("message", "missing name");
            return ResponseEntity.badRequest().body(res);
        }

        Optional<Cashier> cashierOpt = cashierRepo.findByName(name);
        if (cashierOpt.isPresent()) {
            res.put("status", "ok");
            res.put("welcome", "Welcome " + name);
        } else {
            res.put("status", "error");
            res.put("message", "unknown cashier");
        }

        return ResponseEntity.ok(res);
    }
}

