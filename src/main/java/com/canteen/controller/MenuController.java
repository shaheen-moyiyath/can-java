package com.canteen.controller;

import com.canteen.dto.ApiResponse;
import com.canteen.dto.MenuItemRequest;
import com.canteen.dto.MenuItemResponse;
import com.canteen.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu Management", description = "Endpoints for viewing and managing canteen menu items")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    @Operation(summary = "View Daily Food Menu", description = "Retrieves active food items currently available for ordering")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAvailableMenu() {
        List<MenuItemResponse> items = menuService.getAvailableMenuItems();
        return ResponseEntity.ok(ApiResponse.ok("Active menu items retrieved successfully", items));
    }

    @GetMapping("/all")
    @Operation(summary = "View All Menu Items", description = "Retrieves all items including currently unavailable items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAllMenu() {
        List<MenuItemResponse> items = menuService.getAllMenuItems();
        return ResponseEntity.ok(ApiResponse.ok("All menu items retrieved successfully", items));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Menu Item Details", description = "Retrieves a single food menu item by ID")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItemById(@PathVariable Long id) {
        MenuItemResponse item = menuService.getMenuItemById(id);
        return ResponseEntity.ok(ApiResponse.ok(item));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Add Menu Item (Admin)", description = "Adds a new dish to the canteen menu")
    public ResponseEntity<ApiResponse<MenuItemResponse>> createMenuItem(@Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse created = menuService.createMenuItem(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Menu item created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Update Menu Item (Admin)", description = "Updates details, pricing, and availability of an existing menu item")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem(@PathVariable Long id,
                                                                        @Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse updated = menuService.updateMenuItem(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Menu item updated successfully", updated));
    }

    @PatchMapping("/{id}/toggle-availability")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Enable / Disable Menu Item (Admin)", description = "Toggles item availability for the daily menu")
    public ResponseEntity<ApiResponse<MenuItemResponse>> toggleAvailability(@PathVariable Long id,
                                                                            @RequestParam(required = false) Boolean available) {
        MenuItemResponse updated = menuService.toggleAvailability(id, available);
        return ResponseEntity.ok(ApiResponse.ok("Menu item availability updated", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Delete Menu Item (Admin)", description = "Permanently removes a dish from the canteen catalog")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.ok(ApiResponse.ok("Menu item deleted successfully", null));
    }
}
