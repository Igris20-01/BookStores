package uz.booker.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.booker.bookstore.dao.BookDao;
import uz.booker.bookstore.dto.*;
import uz.booker.bookstore.entity.book.*;
import uz.booker.bookstore.entity.other.Pdf;
import uz.booker.bookstore.filter.BookSpecifications;
import uz.booker.bookstore.mapper.BookMapper;
import uz.booker.bookstore.mapper.BookSearchMapper;
import uz.booker.bookstore.mapper.BooksGetMapper;
import uz.booker.bookstore.repository.jpa.*;
import uz.booker.bookstore.service.interfaces.BookService;
import uz.booker.bookstore.util.FileUtil;
import uz.booker.bookstore.util.ImageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookServiceI implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookGenreRepository bookGenreRepository;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BooksGetMapper booksGetMapper;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookDao bookDao;

    @Autowired
    BookSearchMapper bookSearchMapper;

    @Autowired
    BookImageRepo bookImageRepo;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    PdfRepository pdfRepository;

    @Value("${photo.book.storage.path}")
    String photoBookStoragePath;

    @Value("${photo.book.poster.storage.path}")
    String photoBookPosterStoragePath;

    @Override
    public void uploadCoverImage(MultipartFile file, Long bookId) throws IOException {
        bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String filePath = photoBookPosterStoragePath + "/" + fileName;
        byte[] resizedImageBytes = ImageUtil.resizeImage(file.getBytes(), 1080, 1080);
        saveImageToFile(resizedImageBytes, filePath);
        BookImage currentCover = bookImageRepo.findByBookIdAndIsCoverTrue(bookId);
        if (currentCover != null) {
            currentCover.setOriginalName(file.getOriginalFilename());
            currentCover.setPathWithOriginalName(filePath);
            currentCover.setGenerateName(fileName);
            bookImageRepo.save(currentCover);
        } else {
            BookImage bookImage = new BookImage();
            bookImage.setOriginalName(file.getOriginalFilename());
            bookImage.setPathWithOriginalName(filePath);
            bookImage.setGenerateName(fileName);
            bookImage.setBookId(bookId);
            bookImage.setCover(true);
            bookImageRepo.save(bookImage);
        }
    }

    @Override
    public void uploadAdditionalImage(List<MultipartFile> files, Long bookId) throws IOException {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        for (MultipartFile file : files) {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String filePath = photoBookStoragePath + "/" + fileName;
            byte[] resizedImageBytes = ImageUtil.resizeImage(file.getBytes(), 1080, 1080);
            saveImageToFile(resizedImageBytes, filePath);
            BookImage bookImage = new BookImage();
            bookImage.setOriginalName(file.getOriginalFilename());
            bookImage.setPathWithOriginalName(filePath);
            bookImage.setGenerateName(fileName);
            bookImage.setBookId(bookId);
            // Установка parentId для дополнительного изображения
            if (book.getImages() != null && !book.getImages().isEmpty()) {
                BookImage parentImage = book.getImages().get(0); // Просто для примера, вы можете выбрать родительское изображение по-другому
                bookImage.setParentId(parentImage.getId());
            }
            bookImage.setCover(false);
            bookImageRepo.save(bookImage);
        }
    }


    private void saveImageToFile(byte[] imageBytes, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, imageBytes);
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }

    @Override
    public void deleteImage(Long imageId) {
        BookImage bookImage = bookImageRepo.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));
        // Удаляем файл изображения
        deleteFile(bookImage.isCover() ? photoBookPosterStoragePath : photoBookStoragePath, bookImage.getGenerateName());
        // Если это обложка, удаляем все связанные дополнительные изображения
        if (bookImage.isCover()) {
            List<BookImage> additionalImages = bookImageRepo.findByParentId(imageId);
            for (BookImage additionalImage : additionalImages) {
                deleteFile(photoBookStoragePath, additionalImage.getGenerateName());
            }
        }
        bookImageRepo.delete(bookImage);
    }

    @Override
    public void deleteFile(String directoryPath, String fileName) {
        try {
            Path filePath = Paths.get(directoryPath, fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void uploadPdf(Long bookId, MultipartFile file) throws IOException {
        String fileName = fileUtil.uploadFile(file);
        Pdf pdf = new Pdf();
        pdf.setFileName(file.getOriginalFilename());
        pdf.setGenerateName(fileName);
        pdf.setBookId(bookId);
        pdfRepository.save(pdf);
    }

    @Override
    public void downloadPdf(Long pdfId, HttpServletResponse response) throws IOException {
        Pdf pdf = pdfRepository.findById(pdfId)
                .orElseThrow(() -> new IllegalArgumentException("PDF с указанным ID не найден"));
        fileUtil.downloadFile(pdf.getGenerateName(), response);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BookDto addBook(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        if (bookDto.getGenres() != null && !bookDto.getGenres().isEmpty()) {
            Set<Genre> genres = new HashSet<>();
            for (GenreDto genreDto : bookDto.getGenres()) {
                Genre existingGenre = genreRepository.findById(genreDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Жанр с id " + genreDto.getId() + " не найден"));
                genres.add(existingGenre);
            }
            book.setGenres(genres);
        }
        book = this.saveBook(book);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public BookDto updateBook(Long bookId, BookDto bookDto) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга с id " + bookId + " не найдена"));
        existingBook.setTitle(bookDto.getTitle());
        existingBook.setPages(bookDto.getPages());
        existingBook.setPrice(bookDto.getPrice());
        existingBook.setDescription(bookDto.getDescription());
        if (bookDto.getAuthorId() != null && !Objects.equals(existingBook.getAuthorId(), bookDto.getAuthorId())) {
            existingBook.setAuthorId(bookDto.getAuthorId());
        }
        if (bookDto.getGenres() != null && !bookDto.getGenres().isEmpty()) {
            Set<Genre> genres = new HashSet<>();
            for (GenreDto genreDto : bookDto.getGenres()) {
                Genre existingGenre = genreRepository.findById(genreDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Жанр с id " + genreDto.getId() + " не найден"));
                genres.add(existingGenre);
            }
            existingBook.setGenres(genres);
        }
        existingBook = bookRepository.save(existingBook);
        return bookMapper.toDto(existingBook);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookGetDto> getAllBook(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        for (Book book : bookPage.getContent()) {
            Long bookId = book.getId();
            Set<Author> authors = new HashSet<>();
            Set<BookGenre> bookGenres = bookGenreRepository.findAllByBookId(bookId);
            Set<Genre> genres = new HashSet<>();
            authors.add(authorRepository.findById(book.getAuthorId()).orElse(null));
            for (BookGenre bookGenre : bookGenres) {
                genres.add(genreRepository.findById(bookGenre.getGenreId()).orElse(null));
            }
            book.setGenres(genres);
        }
        return bookPage.map(booksGetMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга с id " + bookId + " не найдена"));
        bookRepository.delete(existingBook);
    }

    @Override
    public List<Book> findBooksByFilter(BookFilterDto filter) {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        return bookRepository.findAll(BookSpecifications.withFilter(filter), sort);
    }

    @Override
    public List<BookSearchDto> searchingByBook(String keyword){
        return bookDao.searchByBooks(keyword);
    }

    @Override
    public Page<BookSearchDto> findBooksByAuthorName(String authorName, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.ASC, "title"); // Пример сортировки по названию
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Book> bookPage = bookRepository.findDistinctByAuthorNameContaining(authorName, pageable);
        Page<BookSearchDto> bookSearchDtoPage = bookPage.map(bookSearchMapper::toDto);
        return bookSearchDtoPage;

    }

    @Override
    public Page<BookSearchDto> findBooksByGenreNameIgnoreCase(String genreName, Pageable pageable) {
        Page<Book> books = bookRepository.findByGenres_NameIgnoreCase(genreName, pageable);
        return books.map(bookSearchMapper::toDto);
    }

    @Transactional(propagation = Propagation.NEVER)
    public Book saveBook(Book book){
        bookRepository.save(book);
        return book;
    }
}
