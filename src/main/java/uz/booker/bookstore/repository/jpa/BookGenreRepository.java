package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.book.BookGenre;

import java.util.Set;
import java.util.UUID;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, UUID> {

    Set<BookGenre> findAllByBookId(Long bookId);

}

