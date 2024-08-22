package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class ReviewDto {

    private UUID id;
    private String comment;
    private Long bookId;
    private Long userId;
    private Date createdAt;
    private List<ReviewDto> replies;


}
