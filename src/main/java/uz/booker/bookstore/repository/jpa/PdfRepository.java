package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.other.Pdf;


@Repository
public interface PdfRepository extends JpaRepository<Pdf, Long> {
}
