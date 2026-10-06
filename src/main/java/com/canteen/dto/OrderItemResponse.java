package com.canteen.dto;

import com.canteen.entity.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public OrderItemResponse() {
    }

    public OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        if (orderItem.getMenuItem() != null) {
            this.itemId = orderItem.getMenuItem().getId();
            this.itemName = orderItem.getMenuItem().getName();
        }
        this.quantity = orderItem.getQuantity();
        this.unitPrice = orderItem.getUnitPrice();
        if (this.unitPrice != null && this.quantity != null) {
            this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("item_id")
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @JsonProperty("item_name")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("unit_price")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
