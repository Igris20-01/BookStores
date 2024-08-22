package uz.booker.bookstore.service.interfaces;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import uz.booker.bookstore.dto.BookDto;
import uz.booker.bookstore.dto.BookFilterDto;
import uz.booker.bookstore.dto.BookGetDto;
import uz.booker.bookstore.dto.BookSearchDto;
import uz.booker.bookstore.entity.book.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {

    void uploadCoverImage(MultipartFile file, Long bookId) throws IOException;

    void uploadAdditionalImage(List<MultipartFile> files, Long bookId) throws IOException;

    void deleteImage(Long imageId);

    void deleteFile(String directoryPath, String fileName);

    void uploadPdf(Long bookId, MultipartFile file) throws IOException;

    void downloadPdf(Long pdfId, HttpServletResponse response) throws IOException;

    BookDto addBook(BookDto bookDto);

    BookDto updateBook(Long id, BookDto bookDto);


    Page<BookGetDto> getAllBook(Pageable pageable);

    void deleteBook(Long bookId);

    List<Book> findBooksByFilter(BookFilterDto filter);

    List<BookSearchDto> searchingByBook(String keyword);

    Page<BookSearchDto> findBooksByAuthorName(String authorName, Pageable pageable);

    Page<BookSearchDto> findBooksByGenreNameIgnoreCase(String genreName, Pageable pageable);
}
