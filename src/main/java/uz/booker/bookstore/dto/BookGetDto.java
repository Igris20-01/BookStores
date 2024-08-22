package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
public class BookGetDto {
    private Long id;
    private String title;
    private AuthorDto author;
    private Set<GenreDto> genres;
    private Short pages;
    private BigDecimal price;
    private String description;
}