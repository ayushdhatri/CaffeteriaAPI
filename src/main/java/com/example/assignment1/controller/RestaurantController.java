package com.example.assignment1.controller;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.RestaurantRequest;
import com.example.assignment1.model.Restaurant;
import com.example.assignment1.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * TODO: POST /api/restaurants
     * Create a new restaurant.
     *
     * - Accept a RestaurantRequest from the request body (already done via @RequestBody).
     * - Call the service to create the restaurant.
     * - Return HTTP 201 (Created) with the created restaurant in the response body.
     *
     * Hint: ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant)
     */
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantRequest request) {
        Restaurant newRestraunt = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRestraunt);
    }

    /**
     * TODO: GET /api/restaurants
     * Get all restaurants.
     *
     * - Call the service to get all restaurants.
     * - Return HTTP 200 (OK) with the list in the response body.
     *
     * Hint: ResponseEntity.ok(list)
     */
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        // TODO: Implement this method
        return null;
    }

    /**
     * TODO: GET /api/restaurants/{id}
     * Get a restaurant by its ID.
     *
     * - The {id} is already extracted via @PathVariable.
     * - Call the service to get the restaurant.
     * - Return HTTP 200 with the restaurant.
     * - If not found, the service throws an exception → GlobalExceptionHandler handles it.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantById(id));
    }

    /**
     * TODO: PUT /api/restaurants/{id}
     * Update an existing restaurant.
     *
     * - Extract id from path, request body already parsed.
     * - Call the service to update.
     * - Return HTTP 200 with the updated restaurant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @RequestBody RestaurantRequest request) {
        // TODO: Implement this method
        return null;
    }

    /**
     * TODO: DELETE /api/restaurants/{id}
     * Delete a restaurant.
     *
     * - Call the service to delete the restaurant.
     * - Return HTTP 204 (No Content) with an empty body.
     *
     * Hint: ResponseEntity.noContent().build()
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        // TODO: Implement this method
        return null;
    }
}
