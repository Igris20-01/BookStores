package uz.booker.bookstore.entity.other;

import jakarta.persistence.*;
import lombok.Data;
import uz.booker.bookstore.entity.book.Book;

@Data
@Entity
@Table(name = "pdf_files")
public class Pdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "path_with_file")
    private String pathWithName;

    @Column(name = "generate_name")
    private String generateName;

    @Column(name = "book_id")
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;
}
