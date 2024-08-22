package uz.booker.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.booker.bookstore.dto.OrderDto;
import uz.booker.bookstore.dto.OrderItemDto;
import uz.booker.bookstore.entity.order.CartItem;
import uz.booker.bookstore.entity.order.Order;
import uz.booker.bookstore.entity.order.OrderItem;
import uz.booker.bookstore.entity.user.UserBook;
import uz.booker.bookstore.entity.user.UserCart;
import uz.booker.bookstore.enums.Status;
import uz.booker.bookstore.repository.jpa.*;
import uz.booker.bookstore.service.interfaces.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceI implements OrderService {

    @Autowired
    UserCartRepository userCartRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserBookRepository userBookRepository;

    @Override
    @Transactional
    public OrderDto createOrder(Long userId) {
        // Предполагается, что у вас есть другие сервисы для работы с корзиной, но я использую
        // clearCart(userId) как заглушку для очистки корзины.
        UserCart userCart = userCartRepository.findByUserId(userId).orElse(null);
        if (userCart == null) {
            // Можно добавить обработку ситуации, когда корзина пуста или не найдена.
            return null;
        }

        List<CartItem> cartItems = cartItemRepository.findByUserCart(userCart);

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(Status.PENDING);
        order = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> itemList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setBookId(cartItem.getBookId());
            orderItem.setBookPrice(cartItem.getBook().getPrice());
            itemList.add(orderItem);

            totalPrice = totalPrice.add(cartItem.getBook().getPrice());
        }
        orderItemRepository.saveAll(itemList);

        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);

        return mapOrderToDTO(order);
    }


    @Override
    public void markOrderAsPaid(UUID orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Помечаем заказ как оплаченный
        order.setOrderStatus(Status.PAID);
        orderRepository.save(order);

        // Получаем все позиции заказа
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        // Для каждой позиции заказа создаем запись в UserBook
        for (OrderItem orderItem : orderItems) {
            UserBook userBook = new UserBook();
            userBook.setUserId(order.getUserId());
            userBook.setBookId(orderItem.getBookId());
            userBook.setPurchased(true);
            userBookRepository.save(userBook);
        }
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDto> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(mapOrderToDTO(order));
        }
        return orderDTOs;
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(UUID orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<OrderItemDto> orderItemDTOs = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDTOs.add(mapOrderItemToDTO(orderItem));
        }
        return orderItemDTOs;
    }

    private OrderDto mapOrderToDTO(Order order) {
        OrderDto orderDTO = new OrderDto();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setOrderStatus(order.getOrderStatus());
        return orderDTO;
    }

    // Вспомогательный метод для преобразования OrderItem в OrderItemDTO
    private OrderItemDto mapOrderItemToDTO(OrderItem orderItem) {
        OrderItemDto orderItemDTO = new OrderItemDto();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setOrderId(orderItem.getOrderId());
        orderItemDTO.setBookId(orderItem.getBookId());
        orderItemDTO.setBookPrice(orderItem.getBookPrice());
        return orderItemDTO;
    }

}
