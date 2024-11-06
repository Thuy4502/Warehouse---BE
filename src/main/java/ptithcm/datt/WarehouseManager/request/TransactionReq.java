package ptithcm.datt.WarehouseManager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionReq {
    private Long transactionId;
    private String businessPartner;
    private String address;
    private String phone_number;
    private Double totalValue;
    private String taxId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String transactionCode;
    private Long staffId;
    private Long updateBy;
    private Long transactionRequestId;
    private Long billId;
    private Set<TransactionItemRequest> transactionItems;
    private String Type;
}
