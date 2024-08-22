package uz.booker.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.booker.bookstore.dto.OrderDto;
import uz.booker.bookstore.dto.OrderItemDto;
import uz.booker.bookstore.service.interfaces.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {

    @Autowired
    OrderService orderService;



    @Operation(summary = "paid", description = "bar")
    @PostMapping("/paid/{orderId}")
    public ResponseEntity<String> markOrderAsPaid(@PathVariable UUID orderId) {
        orderService.markOrderAsPaid(orderId);
        return ResponseEntity.ok("Order with ID " + orderId + " has been marked as paid.");
    }

    @Operation(summary = "get-order", description = "bar")
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "get-order-item", description = "bar")
    @GetMapping("/order_item/{orderId}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable UUID orderId) {
        List<OrderItemDto> orderItems = orderService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }
}
