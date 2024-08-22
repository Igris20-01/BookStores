package uz.booker.bookstore.mapper;

import org.mapstruct.Mapper;
import uz.booker.bookstore.dto.AuthorDto;
import uz.booker.bookstore.entity.book.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper extends BaseMapper<Author, AuthorDto>{
}
