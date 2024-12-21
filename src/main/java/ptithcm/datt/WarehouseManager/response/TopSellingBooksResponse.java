package ptithcm.datt.WarehouseManager.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TopSellingBooksResponse {
    private String bookName;
    private String image;
    private int quantity;
    private BigDecimal totalSold;
    private double totalRevenue;
}
