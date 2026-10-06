package com.canteen.dto;

import com.canteen.repository.ItemSalesProjection;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class ItemSalesResponse {

    private Long itemId;
    private String itemName;
    private String category;
    private Long quantitySold;
    private BigDecimal totalSales;

    public ItemSalesResponse() {
    }

    public ItemSalesResponse(Long itemId, String itemName, String category, Long quantitySold, BigDecimal totalSales) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.quantitySold = quantitySold;
        this.totalSales = totalSales;
    }

    public ItemSalesResponse(ItemSalesProjection projection) {
        this.itemId = projection.getItemId();
        this.itemName = projection.getItemName();
        this.category = projection.getCategory();
        this.quantitySold = projection.getQuantitySold();
        this.totalSales = projection.getTotalSales();
    }

    public Long getItemId() {
        return itemId;
    }

    @JsonProperty("item_id")
    public Long getItemIdProperty() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    @JsonProperty("item_name")
    public String getItemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getQuantitySold() {
        return quantitySold;
    }

    @JsonProperty("quantity_sold")
    public Long getQuantitySoldProperty() {
        return quantitySold;
    }

    public void setQuantitySold(Long quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    @JsonProperty("total_sales")
    public BigDecimal getTotalSalesProperty() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }
}
