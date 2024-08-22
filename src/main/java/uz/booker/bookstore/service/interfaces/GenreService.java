package uz.booker.bookstore.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.booker.bookstore.dto.GenreDto;

public interface GenreService {
    GenreDto addGenre(GenreDto genreDto);

    GenreDto updateGenre(Long genreId, GenreDto genreDto);

    Page<GenreDto> getAllGenre(Pageable pageable);

    void deleteGenre(Long genreId);
}
