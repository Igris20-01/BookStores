package uz.booker.bookstore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {


    ACTIVE,

    EMPTY,

    ABANDONED,

    EXPIRED,

    PENDING,

    PROCESSING,

    COMPLETED,

    CANCELLED,

    FAILED,

    REFUNDED,

    PAID

}
