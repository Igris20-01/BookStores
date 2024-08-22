package uz.booker.bookstore.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.book.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findAll(Specification<Book> spec, Sort sort);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.author a WHERE a.name ILIKE concat('%',:authorName,'%')")
    Page<Book> findDistinctByAuthorNameContaining(@Param("authorName") String authorName, Pageable pageable);

    Page<Book> findByGenres_NameIgnoreCase(String genreName, Pageable pageable);


}
