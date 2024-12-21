package ptithcm.datt.WarehouseManager.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookRequest {
    private Long bookId;
    private String bookName;
    private String image;
    private String title;
    private int publicationYear;
    private int edition;
    private String language;
    private int numberOfPage;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Double price;
    private int quantity;
    private String status;
    private List<Long> categoryIds;
    private Long staffId;
    private Long publisherId;
    private List<Long> authorIds;
    private String ISBN;
}
