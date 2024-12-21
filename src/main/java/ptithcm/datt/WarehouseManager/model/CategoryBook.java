package ptithcm.datt.WarehouseManager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category_book")
public class CategoryBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_book_id")
    private Long categoryBookId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @JsonBackReference  // Add this to prevent circular reference during serialization
    private Book book;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;
}

