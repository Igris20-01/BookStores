package uz.booker.bookstore.entity.token;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.booker.bookstore.entity.user.User;
import uz.booker.bookstore.enums.TokenType;

import java.util.UUID;

@Data
@Entity
@Table(name = "tokens")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String token;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    boolean expired;

    boolean revoke;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}