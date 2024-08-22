package uz.booker.bookstore.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.dto.AuthorDto;
import uz.booker.bookstore.dto.BookSearchDto;
import uz.booker.bookstore.dto.GenreDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class BookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<BookSearchDto> searchByBooks(String keyword) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT b.id, b.title, b.pages, b.price, b.description, ");
        sql.append("a.id as author_id, a.name as author_name, ");
        sql.append("g.id as genre_id, g.name as genre_name ");
        sql.append("FROM books b ");
        sql.append("JOIN authors a ON b.author_id = a.id ");
        sql.append("JOIN book_genre bg ON b.id = bg.book_id ");
        sql.append("JOIN genres g ON bg.genre_id = g.id ");
        sql.append("WHERE b.title = ?");

        List<BookSearchDto> books = new ArrayList<>();
        jdbcTemplate.query(sql.toString(), new Object[]{keyword}, rs -> {
            Long bookId = rs.getLong("id");
            BookSearchDto existingBook = findBookById(books, bookId);
            if (existingBook == null) {
                BookSearchDto book = new BookSearchDto();
                book.setId(bookId);
                book.setTitle(rs.getString("title"));
                book.setPages(rs.getShort("pages"));
                book.setPrice(rs.getBigDecimal("price"));
                book.setDescription(rs.getString("description"));

                AuthorDto author = new AuthorDto();
                author.setId(rs.getLong("author_id"));
                author.setName(rs.getString("author_name"));
                book.setAuthor(author);

                Set<GenreDto> genres = new HashSet<>();
                GenreDto genre = new GenreDto();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
                genres.add(genre);
                book.setGenres(genres);

                books.add(book);
            } else {
                GenreDto genre = new GenreDto();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
                existingBook.getGenres().add(genre);
            }
        });
        return books;
    }

    private BookSearchDto findBookById(List<BookSearchDto> books, Long id) {
        for (BookSearchDto book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

}
