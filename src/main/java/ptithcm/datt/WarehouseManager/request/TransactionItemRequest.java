package ptithcm.datt.WarehouseManager.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionItemRequest {
    private Long transactionItemId;
    private int requestQuantity;
    private int actualQuantity;
    private Double price;
    private String note;
    private Long bookId;
    private Long transactionId;
}
