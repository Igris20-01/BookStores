package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.user.UserCart;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCartRepository extends JpaRepository<UserCart, UUID> {
    Optional<UserCart> findByUserId(Long id);

    void deleteByUserId(Long id);
}
