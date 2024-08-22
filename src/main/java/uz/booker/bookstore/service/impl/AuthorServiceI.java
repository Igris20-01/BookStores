package uz.booker.bookstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.booker.bookstore.dto.AuthorDto;
import uz.booker.bookstore.entity.book.Author;
import uz.booker.bookstore.mapper.AuthorMapper;
import uz.booker.bookstore.repository.jpa.AuthorRepository;
import uz.booker.bookstore.service.interfaces.AuthorService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorServiceI implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AuthorMapper authorMapper;

    public AuthorServiceI(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorDto addAuthor (AuthorDto authorDto){
        Author author = authorMapper.toEntity(authorDto);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Override
    public AuthorDto updateAuthor(Long authorId, AuthorDto authorDto){
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Автор с id " + authorId + "не найден"));
        existingAuthor.setName(authorDto.getName());
        existingAuthor = authorRepository.save(existingAuthor);
        return authorMapper.toDto(existingAuthor);
    }

    @Override
    public Page<AuthorDto> getAllAuthor(Pageable pageable){
        Page<Author> authorPage = authorRepository.findAll(pageable);
        return authorPage.map(authorMapper::toDto);
    }
    @Override

    public void deleteAuthor(Long authorId){
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Автор с id " + authorId + "не найден"));
        authorRepository.delete(existingAuthor);

    }
}
