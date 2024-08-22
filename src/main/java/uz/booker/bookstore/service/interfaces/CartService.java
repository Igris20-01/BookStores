package uz.booker.bookstore.service.interfaces;

import uz.booker.bookstore.dto.CartDto;
import uz.booker.bookstore.dto.CartItemDto;
import uz.booker.bookstore.dto.OrderDto;

import java.util.List;

public interface CartService {
    void addToCart(CartDto cartDto);

    void clearCart(Long userId);

    List<CartItemDto> getCartInfo(Long userId);

    void removeFromCart(Long bookId);

    OrderDto checkout(Long userId);
}
