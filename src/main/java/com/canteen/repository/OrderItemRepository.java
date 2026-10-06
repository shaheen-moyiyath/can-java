package com.canteen.repository;

import com.canteen.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query(value = """
        SELECT 
            m.id AS itemId,
            m.name AS itemName,
            m.category AS category,
            SUM(oi.quantity) AS quantitySold,
            SUM(oi.quantity * oi.unit_price) AS totalSales
        FROM order_items oi
        JOIN menu_items m ON oi.menu_item_id = m.id
        JOIN orders o ON oi.order_id = o.id
        WHERE o.order_date = :orderDate AND o.status <> 'CANCELLED'
        GROUP BY m.id, m.name, m.category
        ORDER BY quantitySold DESC, totalSales DESC
        """, nativeQuery = true)
    List<ItemSalesProjection> getDailyItemSales(@Param("orderDate") LocalDate orderDate);
}
