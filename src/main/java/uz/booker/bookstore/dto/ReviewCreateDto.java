package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ReviewCreateDto {
    private Long bookId;
    private Long userId;
    private String comment;
}
