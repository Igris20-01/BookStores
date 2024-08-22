package uz.booker.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.booker.bookstore.dto.CartDto;
import uz.booker.bookstore.dto.CartItemDto;
import uz.booker.bookstore.dto.OrderDto;
import uz.booker.bookstore.entity.book.Author;
import uz.booker.bookstore.entity.book.Book;
import uz.booker.bookstore.entity.order.CartItem;
import uz.booker.bookstore.entity.user.User;
import uz.booker.bookstore.entity.user.UserCart;
import uz.booker.bookstore.repository.jpa.*;
import uz.booker.bookstore.service.interfaces.CartService;
import uz.booker.bookstore.service.interfaces.OrderService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartServiceI implements CartService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserCartRepository userCartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    OrderService orderService;


    @Override
    public void addToCart(CartDto cartDto){
        Long userId = cartDto.getUserId();
        Long bookId = cartDto.getBookId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User id not found" + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book id not found" + bookId));

        UserCart userCart = userCartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    UserCart newUserCart = new UserCart();
                    newUserCart.setUserId(user.getId());
                    return userCartRepository.save(newUserCart);
                });
        CartItem cartItem = new CartItem();
        cartItem.setBookId(book.getId());
        cartItem.setUserCartId(userCart.getId());
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void clearCart(Long userId){
        userCartRepository.findByUserId(userId).ifPresent(userCart -> {
            cartItemRepository.deleteByUserCartId(userCart.getId());
            userCartRepository.delete(userCart);
        });
    }

    @Override
    public List<CartItemDto> getCartInfo(Long userId){
        return userCartRepository.findByUserId(userId)
                .map(userCart -> cartItemRepository.findByUserCartId(userCart.getId())
                        .stream()
                        .map(cartItem -> {
                            Optional<Book> bookOptional = bookRepository.findById(cartItem.getBookId());
                            return bookOptional.map(book -> {
                                Author author = book.getAuthor();
                                return new CartItemDto(
                                        book.getTitle(),
                                        author.getName(),
                                        book.getPrice()
                                );
                            });
                        })
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }

    @Override
    public void removeFromCart(Long bookId){
        List<CartItem> cartItems = cartItemRepository.findByBookId(bookId);
        if (cartItems.isEmpty()) {
            return;
        }
        cartItemRepository.deleteAll(cartItems);
        UserCart userCart = cartItems.get(0).getUserCart();
        List<CartItem> remainingItemsForUser = cartItemRepository.findByUserCart(userCart);
        if (remainingItemsForUser.isEmpty()) {
            userCartRepository.delete(userCart);
        }
    }


    @Override
    @Transactional
    public OrderDto checkout(Long userId) {
        OrderDto orderDto = orderService.createOrder(userId);
        clearCart(userId);
        return orderDto;
    }

}
