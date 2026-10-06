package com.canteen.service;

import com.canteen.dto.OrderItemRequest;
import com.canteen.dto.OrderRequest;
import com.canteen.dto.OrderResponse;
import com.canteen.entity.MenuItem;
import com.canteen.entity.Order;
import com.canteen.entity.OrderItem;
import com.canteen.entity.OrderStatus;
import com.canteen.entity.User;
import com.canteen.exception.BadRequestException;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.MenuItemRepository;
import com.canteen.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * Places a customer order transactionally.
     * Validates menu item availability and calculates total amount server-side.
     */
    @Transactional
    public OrderResponse placeOrder(OrderRequest request, User user) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        Order order = new Order(user, BigDecimal.ZERO, OrderStatus.PENDING, LocalDate.now());

        for (OrderItemRequest itemReq : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + itemReq.getItemId()));

            if (Boolean.FALSE.equals(menuItem.getIsAvailable())) {
                throw new BadRequestException("Item '" + menuItem.getName() + "' is currently unavailable for order");
            }

            if (itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                throw new BadRequestException("Invalid quantity for item: " + menuItem.getName());
            }

            BigDecimal unitPrice = menuItem.getPrice();
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = new OrderItem(order, menuItem, itemReq.getQuantity(), unitPrice);
            order.addOrderItem(orderItem);
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrderHistory(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(LocalDate date) {
        List<Order> orders;
        if (date != null) {
            orders = orderRepository.findByOrderDateOrderByCreatedAtDesc(date);
        } else {
            orders = orderRepository.findAllByOrderByCreatedAtDesc();
        }
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return new OrderResponse(updated);
    }
}
