package uz.booker.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import uz.booker.bookstore.dto.BookDto;
import uz.booker.bookstore.entity.book.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mappings({
            @Mapping(source = "authorId", target = "authorId"),
            @Mapping(source = "genres", target = "genres"),
    })
    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);

}
