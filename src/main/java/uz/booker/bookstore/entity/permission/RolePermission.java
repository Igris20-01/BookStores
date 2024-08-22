package uz.booker.bookstore.entity.permission;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.booker.bookstore.enums.Permission;
import uz.booker.bookstore.enums.Role;

@Entity
@Table(name = "permission")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission {

    @Id
    Long id;

    boolean delete;

    @Enumerated(EnumType.STRING)
    Role role;

    @Enumerated(EnumType.STRING)
    Permission permission;



}
