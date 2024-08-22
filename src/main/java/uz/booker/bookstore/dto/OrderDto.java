package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uz.booker.bookstore.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderDto {

    private UUID id;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private Status orderStatus;

}
