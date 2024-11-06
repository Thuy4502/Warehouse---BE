package ptithcm.datt.WarehouseManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="author_book")
public class AuthorBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorBookId;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "book_id")
    private Long bookId;
}
