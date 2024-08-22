package uz.booker.bookstore.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findByDeletedTrueAndDeletedAtBefore(LocalDate cutoffDate);

    @Modifying
    @Query("UPDATE User AS u SET u.isActive = false, u.deleted = true, u.deletedAt = local_date WHERE u.email = :email")
    void softDeleteByEmail(String email);


}


































//    Page<User> findAllByIsActiveTrue(Pageable pageable);
//
//    @Query("SELECT u FROM User AS u WHERE u.isActive = false ")
//    Page<User> findAllByIsActiveFalse(Pageable pageable);