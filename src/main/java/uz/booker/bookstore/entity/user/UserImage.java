package uz.booker.bookstore.entity.user;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "user_image")
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "original_name")
    private String originalName;

    @Column(name = "path_with_original_name")
    private String pathWithOriginalName;

    @Column(name = "generate_name")
    private String generateName;


    @Column(name = "user_id")
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", insertable = false, updatable = false , referencedColumnName = "id")
    private User user;
}
