package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.other.Otp;

import java.util.Optional;
@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {
    Optional<Otp> findByUserIdAndOtp(Long id, String otp);

    Optional<Otp> findByUserId(Long id);
}
