package ptithcm.datt.WarehouseManager.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryResponse {
    private Long bookId;
    private String isbn;
    private String bookName;
    private int startQuantity;
    private int importQuantity;
    private int exportQuantity;
    private int endQuantity;
    private double endPrice;
    private double endAmount;

}
