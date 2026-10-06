package com.canteen.controller;

import com.canteen.dto.*;
import com.canteen.service.OrderService;
import com.canteen.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Admin Operations", description = "Endpoints for Canteen Staff: Live Dashboard, Kitchen Tallies, Order Verification, and Reporting")
public class AdminController {

    private final ReportService reportService;
    private final OrderService orderService;

    public AdminController(ReportService reportService, OrderService orderService) {
        this.reportService = reportService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard/live-counts")
    @Operation(summary = "Live Kitchen Tally", description = "Returns aggregated real-time dish counts for kitchen preparation (e.g. Veg Thali: 52, Chicken Biryani: 35)")
    public ResponseEntity<ApiResponse<List<KitchenTallyResponse>>> getLiveKitchenTally(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        List<KitchenTallyResponse> tallies = reportService.getLiveKitchenTally(targetDate);
        return ResponseEntity.ok(ApiResponse.ok("Live kitchen tallies retrieved successfully", tallies));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Admin Live Dashboard (Tallies & Orders)", description = "Direct parity with the legacy Express route returning aggregated tallies and recent orders")
    public ResponseEntity<Map<String, Object>> getAdminDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        List<KitchenTallyResponse> tallies = reportService.getLiveKitchenTally(targetDate);
        List<OrderResponse> orders = orderService.getOrders(targetDate);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("date", targetDate.toString());
        response.put("tallies", tallies);
        response.put("orders", orders);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    @Operation(summary = "Order Verification", description = "Detailed view of who placed which order with student details, items, time, and status")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<OrderResponse> orders = orderService.getOrders(date);
        return ResponseEntity.ok(ApiResponse.ok("Orders retrieved successfully", orders));
    }

    @PutMapping("/orders/{id}/status")
    @Operation(summary = "Update Order Status", description = "Updates order status (PENDING, PREPARING, COMPLETED, CANCELLED)")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse updated = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.ok("Order status updated to " + request.getStatus(), updated));
    }

    @GetMapping("/reports/daily")
    @Operation(summary = "Daily Summary Report", description = "Generates comprehensive daily report on order volume, total sales revenue, and item-by-item popularity")
    public ResponseEntity<ApiResponse<DailyReportResponse>> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        DailyReportResponse report = reportService.getDailyReport(targetDate);
        return ResponseEntity.ok(ApiResponse.ok("Daily report generated successfully", report));
    }
}
