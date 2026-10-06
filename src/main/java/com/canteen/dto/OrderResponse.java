package com.canteen.dto;

import com.canteen.entity.Order;
import com.canteen.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private Long userId;
    private String username;
    private String studentName;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDate orderDate;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items = new ArrayList<>();

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        if (order.getUser() != null) {
            this.userId = order.getUser().getId();
            this.username = order.getUser().getUsername();
            this.studentName = order.getUser().getFullName();
        }
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.orderDate = order.getOrderDate();
        this.createdAt = order.getCreatedAt();
        if (order.getOrderItems() != null) {
            this.items = order.getOrderItems().stream()
                    .map(OrderItemResponse::new)
                    .collect(Collectors.toList());
        }
    }

    public Long getId() {
        return id;
    }

    @JsonProperty("order_id")
    public Long getOrderId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("student_name")
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @JsonProperty("total_amount")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @JsonProperty("order_date")
    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    @JsonProperty("created_at")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}
