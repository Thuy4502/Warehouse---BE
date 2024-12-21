package ptithcm.datt.WarehouseManager.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionHistoryResponse {
    private Date createDate;
    private String transactionCode;
    private Long bookId;
    private String bookName;
    private double price;
    private int startQty;
    private int actualQuantity;
    private Long typeId;

}
