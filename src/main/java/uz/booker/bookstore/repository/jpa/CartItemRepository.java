package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.order.CartItem;
import uz.booker.bookstore.entity.user.UserCart;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findById(UUID id);

    void deleteByUserCartId(UUID userCartId);

    List<CartItem> findByUserCartId(UUID userCartId);
    

    List<CartItem> findByBookId(Long bookId);

    List<CartItem> findByUserCartUserId(Long userId);

    List<CartItem> findByUserCart(UserCart userCart);
}
