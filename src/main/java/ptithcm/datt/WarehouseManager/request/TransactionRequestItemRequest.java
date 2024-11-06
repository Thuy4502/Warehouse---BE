package ptithcm.datt.WarehouseManager.request;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptithcm.datt.WarehouseManager.model.Book;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionRequestItemRequest {
    private int quantity;
    private Double price;
    private String note;
    private Long bookId;

}
