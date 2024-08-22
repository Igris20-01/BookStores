package uz.booker.bookstore.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.booker.bookstore.dto.BookDto;
import uz.booker.bookstore.dto.BookFilterDto;
import uz.booker.bookstore.dto.BookGetDto;
import uz.booker.bookstore.dto.BookSearchDto;
import uz.booker.bookstore.entity.book.Book;
import uz.booker.bookstore.mapper.BookSearchMapper;
import uz.booker.bookstore.service.interfaces.BookService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book", description = "Book management APIs")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    BookSearchMapper bookSearchMapper;

    @Operation(summary = "add-book", description = "bar")
    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto){
        BookDto addBook = bookService.addBook(bookDto);
        return new ResponseEntity<>(addBook, HttpStatus.OK);
    }

    @Operation(summary = "update-book", description = "bar")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto){
        BookDto updateBook = bookService.updateBook(id,bookDto);
        return new ResponseEntity<>(updateBook,HttpStatus.OK);
    }

    @Operation(summary = "get-all-book", description = "bar")
    @GetMapping
    public ResponseEntity<Page<BookGetDto>> getAllBook(Pageable pageable){
        Page<BookGetDto> book = bookService.getAllBook(pageable);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "delete-book", description = "bar")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId){
        bookService.deleteBook(bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "find-book-by-filter", description = "bar")
    @GetMapping("/filter")
    public List<BookSearchDto> getBooksByFilter(@ModelAttribute BookFilterDto filter) {
        List<Book> books = bookService.findBooksByFilter(filter);
        return books.stream().map(bookSearchMapper::toDto).collect(Collectors.toList());
    }

    @Operation(summary = "search-book", description = "bar")
    @GetMapping("/search-by-book")
    public List<BookSearchDto> searchBooks(@RequestParam("keyword") String keyword) {
        return bookService.searchingByBook(keyword);
    }

    @Operation(summary = "get-by-author", description = "bar")
    @GetMapping("/author")
    public ResponseEntity<Page<BookSearchDto>> getBooksByAuthorName(
            @RequestParam("authorName") String authorName,
            Pageable pageable) {
        Page<BookSearchDto> books = bookService.findBooksByAuthorName(authorName, pageable);
        return ResponseEntity.ok().body(books);
    }

    @Operation(summary = "get-by-genre", description = "bar")
    @GetMapping("/genres")
    public ResponseEntity<Page<BookSearchDto>> getBooksByGenreName(@RequestParam String genreName, Pageable pageable) {
        Page<BookSearchDto> books = bookService.findBooksByGenreNameIgnoreCase(genreName, pageable);
        return ResponseEntity.ok().body(books);
    }

    @Operation(summary = "poster-upload", description = "bar")
    @PostMapping("/{bookId}/cover")
    public ResponseEntity<String> uploadCoverImage(@RequestParam("file") MultipartFile file,
                                                   @PathVariable Long bookId) {
        try {
            bookService.uploadCoverImage(file, bookId);
            return ResponseEntity.ok("Cover image uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload cover image: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found: " + e.getMessage());
        }
    }

    @Operation(summary = "add-image-book", description = "bar")
    @PostMapping("/{bookId}/add")
    public ResponseEntity<String> uploadAdditionalImage(@RequestParam("file") List<MultipartFile> file,
                                                        @PathVariable Long bookId) {
        try {
            bookService.uploadAdditionalImage(file, bookId);
            return ResponseEntity.ok("Additional image uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload additional image: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found: " + e.getMessage());
        }
    }

    @Operation(summary = "delete-image", description = "bar")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        try {
            bookService.deleteImage(imageId);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
    }

    @Operation(summary = "upload-pdf", description = "bar")
    @PostMapping("/{bookId}/pdf")
    public ResponseEntity<String> uploadPdf(@PathVariable Long bookId, @RequestParam("file") MultipartFile file) {
        try {
            bookService.uploadPdf(bookId, file);
            return ResponseEntity.ok("PDF успешно загружен");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка загрузки PDF: " + e.getMessage());
        }
    }

    @Operation(summary = "download-pdf", description = "bar")
    @GetMapping("/pdf/{pdfId}")
    public void downloadPdf(@PathVariable Long pdfId, HttpServletResponse response) {
        try {
            bookService.downloadPdf(pdfId, response);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
