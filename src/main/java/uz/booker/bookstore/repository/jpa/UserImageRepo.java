package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.user.UserImage;

import java.util.Optional;

@Repository
public interface UserImageRepo extends JpaRepository<UserImage, Long> {

    Optional<UserImage> findByUserId(Long userId);
}
