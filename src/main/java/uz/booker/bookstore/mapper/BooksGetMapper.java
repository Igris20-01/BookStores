package uz.booker.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import uz.booker.bookstore.dto.BookGetDto;
import uz.booker.bookstore.entity.book.Book;

@Mapper(componentModel = "spring")
public interface BooksGetMapper {


    @Mappings({
            @Mapping(source = "author", target = "author"),
            @Mapping(source = "genres", target = "genres"),
    })
    BookGetDto toDto(Book book);
}