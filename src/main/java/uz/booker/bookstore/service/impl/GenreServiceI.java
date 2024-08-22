package uz.booker.bookstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.booker.bookstore.dto.GenreDto;
import uz.booker.bookstore.entity.book.Genre;
import uz.booker.bookstore.mapper.GenreMapper;
import uz.booker.bookstore.repository.jpa.GenreRepository;
import uz.booker.bookstore.service.interfaces.GenreService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreServiceI implements GenreService {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    GenreMapper genreMapper;


    @Override
    public GenreDto addGenre(GenreDto genreDto){
        Genre genre = genreMapper.toGenre(genreDto);
        genre = genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    @Override
    @CachePut(value = "genres", key = "#genreId") // обновляем кеш при обновлении жанра
    public GenreDto updateGenre(Long genreId, GenreDto genreDto){
        Genre existingGenre = genreRepository.findById(genreId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Жанр с id " + genreId + "не найден"));
        existingGenre.setName(genreDto.getName());
        existingGenre = genreRepository.save(existingGenre);
        return genreMapper.toDto(existingGenre);
    }

    @Override
    @Cacheable("genres") // кешируем результаты запроса на получение всех жанров
    public Page<GenreDto> getAllGenre(Pageable pageable){
        Page<Genre> genrePage = genreRepository.findAll(pageable);
        return genrePage.map(genreMapper::toDto);
    }

    @Override
    @CacheEvict(value = "genres", allEntries = true) // очищаем кеш при удалении жанра
    public void deleteGenre(Long genreId){
        Genre existingGenre = genreRepository.findById(genreId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Жанр с id " + genreId + "не найден"));
        genreRepository.delete(existingGenre);
    }
}
