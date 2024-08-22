package uz.booker.bookstore.filter;


import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.booker.bookstore.dto.BookFilterDto;
import uz.booker.bookstore.entity.book.Book;

import java.util.ArrayList;
import java.util.List;

public class BookSpecifications {


    public static Specification<Book> withFilter(BookFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (filter.getTitle() != null && !filter.getTitle().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("title")), filter.getTitle().toLowerCase()));
            }

            if (filter.getAuthorId() != null) {
                predicates.add(cb.equal(root.get("authorId"), filter.getAuthorId()));
            }

            if (filter.getGenreIds() != null && !filter.getGenreIds().isEmpty()) {
                predicates.add(root.join("genres").get("id").in(filter.getGenreIds()));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
