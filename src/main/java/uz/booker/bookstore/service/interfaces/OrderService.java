package uz.booker.bookstore.service.interfaces;

import uz.booker.bookstore.dto.OrderDto;
import uz.booker.bookstore.dto.OrderItemDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDto createOrder(Long userId);

    void markOrderAsPaid(UUID orderId);

    List<OrderDto> getOrdersByUserId(Long userId);

    List<OrderItemDto> getOrderItemsByOrderId(UUID orderId);
}
