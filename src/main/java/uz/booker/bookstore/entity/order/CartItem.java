package uz.booker.bookstore.entity.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.booker.bookstore.entity.book.Book;
import uz.booker.bookstore.entity.user.UserCart;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "book_id")
    Long bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    Book book;

    @Column(name = "user_cart_id")
    UUID userCartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cart_id", insertable = false, updatable = false)
    UserCart userCart;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    Boolean deleted = false;
}
