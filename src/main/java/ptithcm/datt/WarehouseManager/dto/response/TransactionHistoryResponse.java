package ptithcm.datt.WarehouseManager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;


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
