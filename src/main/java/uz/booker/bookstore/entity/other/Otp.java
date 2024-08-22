package uz.booker.bookstore.entity.other;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.booker.bookstore.entity.BaseEntity;
import uz.booker.bookstore.entity.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "otp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Otp extends BaseEntity {

    String otp;

    LocalDateTime otpGeneratedTime;

    @Column(name = "user_id")
    Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    User user;

}
