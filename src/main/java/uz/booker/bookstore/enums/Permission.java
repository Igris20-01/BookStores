package uz.booker.bookstore.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    BUY,

    BOOK_CREATE,

    BOOK_EDITE,

    UPDATE_USER,

    CREATE_ADMIN

}
