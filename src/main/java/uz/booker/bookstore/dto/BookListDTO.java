package uz.booker.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BookListDTO implements Serializable {

    private Long id;
    private String title;
    private String authorName;
}
