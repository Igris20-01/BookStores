package uz.booker.bookstore.entity.book;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "book_image")
public class BookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "original_name")
    private String originalName;

    @Column(name = "path_with_original_name")
    private String pathWithOriginalName;

    @Column(name = "generate_name")
    private String generateName;

    @Column(name = "book_id")
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private BookImage parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<BookImage> child = new ArrayList<>();

    @Column(name = "is_cover")
    private boolean isCover;
}
