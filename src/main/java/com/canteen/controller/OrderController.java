package com.canteen.controller;

import com.canteen.dto.ApiResponse;
import com.canteen.dto.OrderRequest;
import com.canteen.dto.OrderResponse;
import com.canteen.entity.Role;
import com.canteen.entity.User;
import com.canteen.exception.UnauthorizedException;
import com.canteen.service.AuthService;
import com.canteen.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Endpoints for students to place food orders and view their history")
@SecurityRequirement(name = "BearerAuth")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Place Food Order", description = "Places a new order with selected dishes and quantities")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@Valid @RequestBody OrderRequest request) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        OrderResponse orderResponse = orderService.placeOrder(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Order placed successfully! Order ID: #" + orderResponse.getId(), orderResponse));
    }

    @GetMapping("/my-history")
    @Operation(summary = "View Order History", description = "Retrieves the logged-in student's past canteen orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrderHistory() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        List<OrderResponse> history = orderService.getUserOrderHistory(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.ok("Order history retrieved", history));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Order Details", description = "Retrieves an order by its ID (accessible by order owner or admin)")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        OrderResponse order = orderService.getOrderById(id);

        if (!order.getUserId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("You are not authorized to view this order");
        }

        return ResponseEntity.ok(ApiResponse.ok(order));
    }
}
