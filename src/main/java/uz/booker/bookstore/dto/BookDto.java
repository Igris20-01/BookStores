package uz.booker.bookstore.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDto {

    Long id;
    String title;
    Long authorId;
    Set<GenreDto> genres;
    Short pages;
    BigDecimal price;
    String description;

}
