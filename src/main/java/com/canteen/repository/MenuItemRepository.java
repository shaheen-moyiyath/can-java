package com.canteen.repository;

import com.canteen.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByIsAvailableTrue();
    List<MenuItem> findByCategoryIgnoreCase(String category);
    boolean existsByNameIgnoreCase(String name);
}
