package uz.booker.bookstore.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@RequiredArgsConstructor
public class BookFilterDto implements Serializable {

//   private String search = "";
   private Long id;
   private String title;
   private Long authorId;
   private Set<Long> genreIds;

}
