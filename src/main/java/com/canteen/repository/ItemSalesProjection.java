package com.canteen.repository;

import java.math.BigDecimal;

public interface ItemSalesProjection {
    Long getItemId();
    String getItemName();
    String getCategory();
    Long getQuantitySold();
    BigDecimal getTotalSales();
}
