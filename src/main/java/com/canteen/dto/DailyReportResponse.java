package com.canteen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyReportResponse {

    private LocalDate reportDate;
    private long totalOrders;
    private long pendingOrders;
    private long preparingOrders;
    private long completedOrders;
    private long cancelledOrders;
    private BigDecimal totalRevenue;
    private List<ItemSalesResponse> itemBreakdown = new ArrayList<>();

    public DailyReportResponse() {
    }

    public DailyReportResponse(LocalDate reportDate, long totalOrders, long pendingOrders,
                               long preparingOrders, long completedOrders, long cancelledOrders,
                               BigDecimal totalRevenue, List<ItemSalesResponse> itemBreakdown) {
        this.reportDate = reportDate;
        this.totalOrders = totalOrders;
        this.pendingOrders = pendingOrders;
        this.preparingOrders = preparingOrders;
        this.completedOrders = completedOrders;
        this.cancelledOrders = cancelledOrders;
        this.totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
        this.itemBreakdown = itemBreakdown != null ? itemBreakdown : new ArrayList<>();
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    @JsonProperty("report_date")
    public LocalDate getReportDateProperty() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    @JsonProperty("total_orders")
    public long getTotalOrdersProperty() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    @JsonProperty("pending_orders")
    public long getPendingOrdersProperty() {
        return pendingOrders;
    }

    public void setPendingOrders(long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public long getPreparingOrders() {
        return preparingOrders;
    }

    @JsonProperty("preparing_orders")
    public long getPreparingOrdersProperty() {
        return preparingOrders;
    }

    public void setPreparingOrders(long preparingOrders) {
        this.preparingOrders = preparingOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    @JsonProperty("completed_orders")
    public long getCompletedOrdersProperty() {
        return completedOrders;
    }

    public void setCompletedOrders(long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public long getCancelledOrders() {
        return cancelledOrders;
    }

    @JsonProperty("cancelled_orders")
    public long getCancelledOrdersProperty() {
        return cancelledOrders;
    }

    public void setCancelledOrders(long cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    @JsonProperty("total_revenue")
    public BigDecimal getTotalRevenueProperty() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<ItemSalesResponse> getItemBreakdown() {
        return itemBreakdown;
    }

    @JsonProperty("item_breakdown")
    public List<ItemSalesResponse> getItemBreakdownProperty() {
        return itemBreakdown;
    }

    public void setItemBreakdown(List<ItemSalesResponse> itemBreakdown) {
        this.itemBreakdown = itemBreakdown;
    }
}
