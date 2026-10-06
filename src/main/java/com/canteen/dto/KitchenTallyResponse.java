package com.canteen.dto;

import com.canteen.repository.KitchenTallyProjection;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KitchenTallyResponse {

    private Long itemId;
    private String itemName;
    private Long totalQuantity;

    public KitchenTallyResponse() {
    }

    public KitchenTallyResponse(Long itemId, String itemName, Long totalQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.totalQuantity = totalQuantity;
    }

    public KitchenTallyResponse(KitchenTallyProjection projection) {
        this.itemId = projection.getItemId();
        this.itemName = projection.getItemName();
        this.totalQuantity = projection.getTotalQuantity();
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

    @JsonProperty("name")
    public String getNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    @JsonProperty("total_quantity")
    public Long getTotalQuantityProperty() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
