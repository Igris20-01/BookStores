package uz.booker.bookstore.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.booker.bookstore.entity.user.User;
import uz.booker.bookstore.enums.Role;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionDto {

    Long id;

    Role role;

    User user;


}
