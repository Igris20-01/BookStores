package uz.booker.bookstore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER,

    ADMIN,

    SUPER_ADMIN,

    CUSTOMER,

    REVIEWER,

    GUEST,


}
