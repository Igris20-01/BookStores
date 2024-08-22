package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.other.FeedBack;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, UUID> {

    List<FeedBack> findByCreateAtBefore(LocalDateTime oneYearAgo);

}
