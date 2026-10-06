package com.canteen.repository;

import com.canteen.entity.Order;
import com.canteen.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByOrderDateOrderByCreatedAtDesc(LocalDate orderDate);

    List<Order> findAllByOrderByCreatedAtDesc();

    long countByOrderDate(LocalDate orderDate);

    long countByOrderDateAndStatus(LocalDate orderDate, OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate = :orderDate AND o.status <> :cancelledStatus")
    BigDecimal calculateDailyRevenue(@Param("orderDate") LocalDate orderDate, @Param("cancelledStatus") OrderStatus cancelledStatus);

    /**
     * Aggregated live kitchen tally for today's orders (reproducing Section 4.4 from project spec).
     * Calculates total quantities needed for each dish, excluding CANCELLED orders.
     */
    @Query(value = """
        SELECT 
            m.id AS itemId, 
            m.name AS itemName, 
            COALESCE(SUM(oi.quantity), 0) AS totalQuantity
        FROM menu_items m
        LEFT JOIN order_items oi ON m.id = oi.menu_item_id
        LEFT JOIN orders o ON oi.order_id = o.id AND o.order_date = :orderDate AND o.status <> 'CANCELLED'
        GROUP BY m.id, m.name
        ORDER BY totalQuantity DESC, m.name ASC
        """, nativeQuery = true)
    List<KitchenTallyProjection> getLiveKitchenTally(@Param("orderDate") LocalDate orderDate);
}
