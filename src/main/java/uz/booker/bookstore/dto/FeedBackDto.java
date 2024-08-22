package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class FeedBackDto {

    private UUID id;
    private String message;
    private String senderName;
    private String senderEmail;

}
