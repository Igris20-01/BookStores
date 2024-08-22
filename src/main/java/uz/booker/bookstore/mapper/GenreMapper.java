package uz.booker.bookstore.mapper;

import org.mapstruct.Mapper;
import uz.booker.bookstore.dto.GenreDto;
import uz.booker.bookstore.entity.book.Genre;


@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre toGenre(GenreDto dto);

    GenreDto toDto(Genre genre);
}
