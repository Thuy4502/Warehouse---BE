package ptithcm.datt.WarehouseManager.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MonthlyTransactionResponse {
    private int month;
    private String importValue;
    private String exportValue;
}
