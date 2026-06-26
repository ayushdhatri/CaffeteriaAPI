package com.example.assignment1.service;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ║  The helper method toMenuResponse() is already done for you.    ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.MenuRequest;
import com.example.assignment1.dto.MenuResponse;
import com.example.assignment1.exception.DuplicateResourceException;
import com.example.assignment1.exception.InvalidRequestException;
import com.example.assignment1.exception.ResourceNotFoundException;
import com.example.assignment1.model.Menu;
import com.example.assignment1.model.MenuItem;
import com.example.assignment1.model.Restaurant;
import com.example.assignment1.repository.MenuRepository;
import com.example.assignment1.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository,
                       RestaurantService restaurantService,
                       MenuItemService menuItemService, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * TODO: Create a new menu.
     *
     * This is the most interesting method — it involves cross-service validation!
     *
     * Steps:
     *   1. Validate that restaurantId is not null.
     *      → throw InvalidRequestException("Restaurant ID cannot be null")
     *   2. Validate that date is not null.
     *      → throw InvalidRequestException("Menu date cannot be null")
     *   3. Validate that mealType is not null.
     *      → throw InvalidRequestException("Meal type cannot be null")
     *   4. Validate that menuItemIds is not null and not empty.
     *      → throw InvalidRequestException("Menu must contain at least one item")
     *   5. Verify the restaurant exists by calling restaurantService.getRestaurantById().
     *      (It will throw ResourceNotFoundException automatically if not found.)
     *   6. Verify ALL menu item IDs are valid by calling menuItemService.getMenuItemById()
     *      for each ID in the list. (Each call throws if the item doesn't exist.)
     *   7. Check for duplicate: use menuRepository.findByRestaurantIdAndDateAndMealType().
     *      If a menu already exists for this combination → throw DuplicateResourceException(
     *        "Menu already exists for restaurant " + restaurantId + " on " + date + " for " + mealType)
     *   8. Create a new Menu object (id = null, set all fields from request).
     *   9. Save it using the repository.
     *  10. Convert the saved menu to a MenuResponse using the toMenuResponse() helper and return it.
     */
    public MenuResponse createMenu(MenuRequest request) {
        // 1. Strict Request Validation (Blocks bad data from polluting the DB)
        if (request.getRestaurantId() == null) {
            throw new InvalidRequestException("Restaurant ID cannot be null");
        }
        if (request.getDate() == null) {
            throw new InvalidRequestException("Menu date cannot be null");
        }
        if (request.getMealType() == null) {
            throw new InvalidRequestException("Meal type cannot be null");
        }
        if (request.getMenuItemIds() == null || request.getMenuItemIds().isEmpty()) {
            throw new InvalidRequestException("Menu must contain at least one item");
        }

        // 2. Verify restaurant exists (This correctly throws 404 Not Found)
        restaurantService.getRestaurantById(request.getRestaurantId());

        // 3. Verify all menu items exist
        request.getMenuItemIds().forEach(menuItemService::getMenuItemById);

        // 4. Duplicate Check
        Optional<Menu> duplicateCheck = menuRepository.findByRestaurantIdAndDateAndMealType(
                request.getRestaurantId(), request.getDate(), request.getMealType());

        if (duplicateCheck.isPresent()) {
            throw new DuplicateResourceException("Menu already exists for restaurant " +
                    request.getRestaurantId() + " on " + request.getDate() + " for " + request.getMealType());
        }

        // 5. Save and Return
        Menu newMenu = new Menu();
        newMenu.setRestaurantId(request.getRestaurantId());
        newMenu.setDate(request.getDate());
        newMenu.setMealType(request.getMealType());
        newMenu.setMenuItemIds(request.getMenuItemIds());

        return toMenuResponse(menuRepository.save(newMenu));
    }
    /**
     * TODO: Get a menu by its ID, returned as a MenuResponse.
     *
     * Steps:
     *   1. Find the menu by ID in the repository.
     *   2. If not found → throw ResourceNotFoundException("Menu not found with id: " + id)
     *   3. Convert to MenuResponse using toMenuResponse() and return.
     */
    public MenuResponse getMenuById(Long id) {
        Menu menu =  menuRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Menu not found with id: " + id));
        return toMenuResponse(menu);
    }

    /**
     * TODO: Get all menus as a list of MenuResponse.
     *
     * Steps:
     *   1. Fetch all menus from the repository.
     *   2. Convert each one to MenuResponse using toMenuResponse().
     *   3. Return the list.
     *
     * Hint: You can use a stream with .map(this::toMenuResponse) or a for-loop.
     */
    public List<MenuResponse> getAllMenus() {
        List<Menu> allMenu = menuRepository.findAll();
        return allMenu.stream().map(this::toMenuResponse).toList();

    }

    /**
     * TODO: Get all menus for a specific restaurant.
     *
     * Steps:
     *   1. Verify the restaurant exists using restaurantService.getRestaurantById().
     *   2. Find all menus for this restaurant using menuRepository.findByRestaurantId().
     *   3. Convert each to MenuResponse and return.
     */
    public List<MenuResponse> getMenusByRestaurantId(Long restaurantId) {
         restaurantService.getRestaurantById(restaurantId);
         List<Menu> restaurantMenu = menuRepository.findByRestaurantId(restaurantId);
         return restaurantMenu.stream().map(this::toMenuResponse).toList();
    }

    /**
     * TODO: Get all menus for a specific date.
     *
     * Steps:
     *   1. Find all menus for this date using menuRepository.findByDate().
     *   2. Convert each to MenuResponse and return.
     */
    public List<MenuResponse> getMenusByDate(LocalDate date) {
        return menuRepository.findByDate(date).stream().map(this::toMenuResponse).toList();
    }

    /**
     * TODO: Get all menus for a specific restaurant on a specific date.
     *
     * Steps:
     *   1. Verify the restaurant exists.
     *   2. Find menus using menuRepository.findByRestaurantIdAndDate().
     *   3. Convert each to MenuResponse and return.
     */
    public List<MenuResponse> getMenusByRestaurantIdAndDate(Long restaurantId, LocalDate date) {
        restaurantService.getRestaurantById(restaurantId);
        return menuRepository.findByRestaurantIdAndDate(restaurantId, date).stream().map(this::toMenuResponse).toList();
    }

    /**
     * TODO: Update an existing menu.
     *
     * Steps:
     *   1. Find the existing menu by ID. If not found → throw ResourceNotFoundException.
     *   2. Validate the request fields (same as create: restaurantId, date, mealType, menuItemIds).
     *   3. Verify the restaurant exists.
     *   4. Verify all menu item IDs exist.
     *   5. Check for duplicate: if the (restaurantId + date + mealType) combination has changed,
     *      check that no OTHER menu already uses that combination.
     *      Use menuRepository.findByRestaurantIdAndDateAndMealType().
     *      If a duplicate is found AND it's not the same menu (different ID) → throw DuplicateResourceException.
     *   6. Update the existing menu's fields.
     *   7. Save and convert to MenuResponse.
     */
    public MenuResponse updateMenu(Long id, MenuRequest request) {
        // 1. Find existing
        Menu existingMenu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with ID: " + id));

        // 2. Strict Request Validation
        if (request.getRestaurantId() == null) {
            throw new InvalidRequestException("Restaurant ID cannot be null");
        }
        if (request.getDate() == null) {
            throw new InvalidRequestException("Menu date cannot be null");
        }
        if (request.getMealType() == null) {
            throw new InvalidRequestException("Meal type cannot be null");
        }
        if (request.getMenuItemIds() == null || request.getMenuItemIds().isEmpty()) {
            throw new InvalidRequestException("Menu must contain at least one item");
        }

        // 3. Verify references
        restaurantService.getRestaurantById(request.getRestaurantId());
        request.getMenuItemIds().forEach(menuItemService::getMenuItemById);

        // 4. Duplicate Check
        Optional<Menu> duplicateCheck = menuRepository.findByRestaurantIdAndDateAndMealType(
                request.getRestaurantId(), request.getDate(), request.getMealType());

        if (duplicateCheck.isPresent() && !duplicateCheck.get().getId().equals(id)) {
            throw new DuplicateResourceException("Duplicate menu conflict.");
        }

        // 5. Update and Save
        existingMenu.setRestaurantId(request.getRestaurantId());
        existingMenu.setDate(request.getDate());
        existingMenu.setMealType(request.getMealType());
        existingMenu.setMenuItemIds(request.getMenuItemIds());

        return toMenuResponse(menuRepository.save(existingMenu));
    }
    /**
     * TODO: Delete a menu by ID.
     *
     * Steps:
     *   1. Verify the menu exists (look it up, throw ResourceNotFoundException if not found).
     *   2. Delete it using the repository.
     */
    public void deleteMenu(Long id) {
        // Step 1: Verify the menu exists
        Menu existingMenu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete: Menu not found with ID: " + id));

        // Step 2: Delete it using the repository
        menuRepository.deleteById(id);
    }

    // ═══════════════════════════════════════════════════════════════
    //  HELPER METHOD — DO NOT MODIFY
    // ═══════════════════════════════════════════════════════════════

    /**
     * Converts a Menu entity into a MenuResponse DTO.
     * Enriches the response with the restaurant's name/location and full MenuItem objects
     * (instead of just IDs).
     */
    private MenuResponse toMenuResponse(Menu menu) {
        Restaurant restaurant = restaurantService.getRestaurantById(menu.getRestaurantId());
        List<MenuItem> items = menu.getMenuItemIds().stream()
                .map(menuItemService::getMenuItemById)
                .toList();

        MenuResponse response = new MenuResponse();
        response.setId(menu.getId());
        response.setRestaurantName(restaurant.getName());
        response.setRestaurantLocation(restaurant.getLocation());
        response.setDate(menu.getDate());
        response.setMealType(menu.getMealType());
        response.setItems(items);
        return response;
    }
}
