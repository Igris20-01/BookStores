package uz.booker.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.booker.bookstore.dto.CartDto;
import uz.booker.bookstore.dto.CartItemDto;
import uz.booker.bookstore.service.interfaces.CartService;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart management APIs")
public class CartController {

    @Autowired
    CartService cartService;

    @Operation(summary = "add-cart", description = "bar")
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartDto cartDto) {
        cartService.addToCart(cartDto);
        return ResponseEntity.ok("Book added to cart successfully");
    }

    @Operation(summary = "delete-cart", description = "bar")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteFromCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.ok("Item removed from cart successfully");

    }

    @Operation(summary = "get-cart-info", description = "bar")
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDto>> getCartInfo(@PathVariable Long userId) {
        List<CartItemDto> cartItems = cartService.getCartInfo(userId);
        if (cartItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @Operation(summary = "delete-cart-item-by-book", description = "bar")
    @DeleteMapping("/item/{bookId}")
    public ResponseEntity<String> deleteCartItemByBookId(@PathVariable Long bookId) {
        cartService.removeFromCart(bookId);
        return ResponseEntity.ok("Cart item(s) deleted successfully");
    }

    @Operation(summary = "checkout-from-cart", description = "bar")
    @PostMapping("/checkout/{userId}")
    public void checkout(@PathVariable Long userId) {
        cartService.checkout(userId);
    }

}
