package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CartDto {

    private Long userId;

    private Long bookId;

}
