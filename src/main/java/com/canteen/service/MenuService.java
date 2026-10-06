package com.canteen.service;

import com.canteen.dto.MenuItemRequest;
import com.canteen.dto.MenuItemResponse;
import com.canteen.entity.MenuItem;
import com.canteen.exception.BadRequestException;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.MenuItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAvailableMenuItems() {
        return menuItemRepository.findByIsAvailableTrue().stream()
                .map(MenuItemResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll(Sort.by(Sort.Direction.ASC, "category").and(Sort.by(Sort.Direction.ASC, "name"))).stream()
                .map(MenuItemResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItemById(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        return new MenuItemResponse(item);
    }

    @Transactional
    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        if (menuItemRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new BadRequestException("Menu item with name '" + request.getName() + "' already exists");
        }

        MenuItem menuItem = new MenuItem(
                request.getName().trim(),
                request.getDescription() != null ? request.getDescription().trim() : null,
                request.getPrice(),
                request.getCategory().trim(),
                request.getIsAvailable() != null ? request.getIsAvailable() : true
        );

        MenuItem saved = menuItemRepository.save(menuItem);
        return new MenuItemResponse(saved);
    }

    @Transactional
    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        menuItem.setName(request.getName().trim());
        menuItem.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory().trim());
        if (request.getIsAvailable() != null) {
            menuItem.setIsAvailable(request.getIsAvailable());
        }

        MenuItem updated = menuItemRepository.save(menuItem);
        return new MenuItemResponse(updated);
    }

    @Transactional
    public MenuItemResponse toggleAvailability(Long id, Boolean isAvailable) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        menuItem.setIsAvailable(isAvailable != null ? isAvailable : !menuItem.getIsAvailable());
        MenuItem updated = menuItemRepository.save(menuItem);
        return new MenuItemResponse(updated);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }
}
