package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderItemDto {

    private UUID id;
    private UUID orderId;
    private Long bookId;
    private BigDecimal bookPrice;
}
