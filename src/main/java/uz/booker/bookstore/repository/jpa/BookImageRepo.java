package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.book.BookImage;


import java.util.List;


@Repository
public interface BookImageRepo extends JpaRepository<BookImage, Long> {


    BookImage findByBookIdAndIsCoverTrue(Long bookId);

    List<BookImage> findByParentId(Long imageId);
}

