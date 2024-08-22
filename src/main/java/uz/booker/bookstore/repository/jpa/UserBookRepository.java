package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.user.UserBook;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {
}
