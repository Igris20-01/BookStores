package uz.booker.bookstore.entity.permission;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.booker.bookstore.entity.user.User;
import uz.booker.bookstore.enums.Permission;

import java.util.UUID;

@Entity
@Table(name = "user_permission")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPermission {

    @Id
    UUID id;

    boolean delete;

    @Enumerated(EnumType.STRING)
    Permission permission;

    @Column(name = "user_id")
    Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

}