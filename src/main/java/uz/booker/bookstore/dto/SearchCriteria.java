package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {

    private String keyword;
    private String author;
    private String genre;


}
