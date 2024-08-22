package uz.booker.bookstore.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class ReplyCreateDto {
    private UUID reviewId;
    private Long bookId;
    private Long userId;
    private String comment;
}
