package uz.booker.bookstore.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.booker.bookstore.dto.AuthorDto;

public interface AuthorService {
    AuthorDto addAuthor(AuthorDto authorDto);

    AuthorDto updateAuthor(Long authorId, AuthorDto authorDto);

    Page<AuthorDto> getAllAuthor(Pageable pageable);

    void deleteAuthor(Long authorId);
}
