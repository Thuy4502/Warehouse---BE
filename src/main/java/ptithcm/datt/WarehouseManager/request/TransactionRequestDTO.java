package ptithcm.datt.WarehouseManager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.TransactionRequestItem;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionRequestDTO {
    private String createBy;
    private String position;
    private String phoneNumber;
    private String department;
    private String reason;
    private String status;
    private Set<TransactionRequestItemRequest> transactionRequestItems;
    private String transactionRequestCode;
    private Double totalValue;
    private Long staffId;
    private Long updateBy;
    private String type;

}
