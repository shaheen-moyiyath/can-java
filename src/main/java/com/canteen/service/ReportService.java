package com.canteen.service;

import com.canteen.dto.DailyReportResponse;
import com.canteen.dto.ItemSalesResponse;
import com.canteen.dto.KitchenTallyResponse;
import com.canteen.entity.OrderStatus;
import com.canteen.repository.OrderItemRepository;
import com.canteen.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ReportService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Aggregated live kitchen tally for today's orders (e.g., "Veg Thali: 52, Chicken Biryani: 35").
     */
    @Transactional(readOnly = true)
    public List<KitchenTallyResponse> getLiveKitchenTally(LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        return orderRepository.getLiveKitchenTally(targetDate).stream()
                .map(KitchenTallyResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Generates a detailed daily summary report with order counts, revenue, and item-by-item breakdown.
     */
    @Transactional(readOnly = true)
    public DailyReportResponse getDailyReport(LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();

        long totalOrders = orderRepository.countByOrderDate(targetDate);
        long pendingOrders = orderRepository.countByOrderDateAndStatus(targetDate, OrderStatus.PENDING);
        long preparingOrders = orderRepository.countByOrderDateAndStatus(targetDate, OrderStatus.PREPARING);
        long completedOrders = orderRepository.countByOrderDateAndStatus(targetDate, OrderStatus.COMPLETED);
        long cancelledOrders = orderRepository.countByOrderDateAndStatus(targetDate, OrderStatus.CANCELLED);

        BigDecimal totalRevenue = orderRepository.calculateDailyRevenue(targetDate, OrderStatus.CANCELLED);
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        List<ItemSalesResponse> itemBreakdown = orderItemRepository.getDailyItemSales(targetDate).stream()
                .map(ItemSalesResponse::new)
                .collect(Collectors.toList());

        return new DailyReportResponse(
                targetDate,
                totalOrders,
                pendingOrders,
                preparingOrders,
                completedOrders,
                cancelledOrders,
                totalRevenue,
                itemBreakdown
        );
    }
}
