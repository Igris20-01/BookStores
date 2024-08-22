package uz.booker.bookstore.mapper;

import org.mapstruct.Mapper;
import uz.booker.bookstore.dto.BookSearchDto;
import uz.booker.bookstore.entity.book.Book;


@Mapper(componentModel = "spring")
public interface BookSearchMapper {


    BookSearchDto toDto(Book book);

}
