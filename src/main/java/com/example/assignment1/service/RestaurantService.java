package com.example.assignment1.service;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.RestaurantRequest;
import com.example.assignment1.exception.InvalidRequestException;
import com.example.assignment1.exception.ResourceNotFoundException;
import com.example.assignment1.model.Restaurant;
import com.example.assignment1.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * TODO: Create a new restaurant.
     *
     * Steps:
     *   1. Validate that the restaurant name is not null or blank (empty/whitespace).
     *      If invalid → throw InvalidRequestException("Restaurant name cannot be empty")
     *   2. Validate that the location is not null or blank.
     *      If invalid → throw InvalidRequestException("Restaurant location cannot be empty")
     *   3. Create a new Restaurant object (id should be null — the repository will assign it).
     *   4. Save it using the repository and return the saved restaurant.
     */
    public Restaurant createRestaurant(RestaurantRequest request) {
        // check if name and location should not be empty
        if(request.getName() == null || request.getName().isEmpty()
          || request.getLocation() == null || request.getLocation().isEmpty()) {
            throw new ResourceNotFoundException("Name and Location cannot be null or blank");
        }
        // create object of restraunt
        Restaurant newRestraunt = new Restaurant();
        newRestraunt.setName(request.getName());
        newRestraunt.setLocation(request.getLocation());

        // save in the db(in-memory)
        restaurantRepository.save(newRestraunt);

        return newRestraunt;
    }

    /**
     * TODO: Get a restaurant by its ID.
     *
     * Steps:
     *   1. Look up the restaurant in the repository using findById.
     *   2. If not found → throw ResourceNotFoundException("Restaurant not found with id: " + id)
     *   3. Return the found restaurant.
     *
     * Hint: Optional has an .orElseThrow() method that is very useful here.
     */
    public Restaurant getRestaurantById(Long id) {
        // TODO: Implement this method
        return null;
    }

    /**
     * TODO: Get all restaurants.
     *
     * @return list of all restaurants (empty list if none exist)
     */
    public List<Restaurant> getAllRestaurants() {
        // TODO: Implement this method
        return null;
    }

    /**
     * TODO: Update an existing restaurant.
     *
     * Steps:
     *   1. Fetch the existing restaurant using getRestaurantById (will throw if not found).
     *   2. Validate that the new name is not null or blank.
     *      If invalid → throw InvalidRequestException("Restaurant name cannot be empty")
     *   3. Validate that the new location is not null or blank.
     *      If invalid → throw InvalidRequestException("Restaurant location cannot be empty")
     *   4. Update the existing restaurant's name and location with values from the request.
     *   5. Save and return the updated restaurant.
     */
    public Restaurant updateRestaurant(Long id, RestaurantRequest request) {
        // TODO: Implement this method
        return null;
    }

    /**
     * TODO: Delete a restaurant by ID.
     *
     * Steps:
     *   1. Verify the restaurant exists using getRestaurantById (will throw if not found).
     *   2. Delete it from the repository using deleteById.
     */
    public void deleteRestaurant(Long id) {
        // TODO: Implement this method
    }
}
