package com.example.assignment1.controller;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.MenuRequest;
import com.example.assignment1.dto.MenuResponse;
import com.example.assignment1.service.MenuService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * TODO: POST /api/menus
     * Create a new menu.
     *
     * - Return HTTP 201 (Created) with the MenuResponse.
     */
    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest request) {
        MenuResponse createdMenu = menuService.createMenu(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    /**
     * TODO: GET /api/menus
     * Get all menus.
     *
     * - Return HTTP 200 with the list of all MenuResponse.
     */
    @GetMapping
    public ResponseEntity<List<MenuResponse>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }

    /**
     * TODO: GET /api/menus/{id}
     * Get a menu by ID.
     *
     * - Return HTTP 200 with the MenuResponse.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getMenuById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getMenuById(id));
    }

    /**
     * TODO: GET /api/menus/restaurant/{restaurantId}
     * Get all menus for a specific restaurant.
     *
     * - Return HTTP 200 with the list of MenuResponse.
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuResponse>> getMenusByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuService.getMenusByRestaurantId(restaurantId));
    }

    /**
     * TODO: GET /api/menus/date/{date}
     * Get all menus for a specific date.
     *
     * The date format in the URL is "yyyy-MM-dd" (e.g., /api/menus/date/2026-02-10).
     * The @DateTimeFormat annotation on the parameter handles parsing automatically.
     *
     * - Return HTTP 200 with the list of MenuResponse.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<MenuResponse>> getMenusByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(menuService.getMenusByDate(date));
    }

    /**
     * TODO: GET /api/menus/restaurant/{restaurantId}/date/{date}
     * Get all menus for a specific restaurant on a specific date.
     * This returns all meals (breakfast, lunch, dinner) for that restaurant on that date.
     *
     * - Return HTTP 200 with the list of MenuResponse.
     */
    @GetMapping("/restaurant/{restaurantId}/date/{date}")
    public ResponseEntity<List<MenuResponse>> getMenusByRestaurantAndDate(
            @PathVariable Long restaurantId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(menuService.getMenusByRestaurantIdAndDate(restaurantId, date));
    }

    /**
     * TODO: PUT /api/menus/{id}
     * Update an existing menu.
     *
     * - Return HTTP 200 with the updated MenuResponse.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long id,
                                                   @RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.updateMenu(id, request));
    }

    /**
     * TODO: DELETE /api/menus/{id}
     * Delete a menu.
     *
     * - Return HTTP 204 (No Content) with an empty body.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}