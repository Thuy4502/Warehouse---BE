package ptithcm.datt.WarehouseManager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplierRequest {
    private String supplierName;
    private String email;
    private String address;
    private String phoneNumber;
}
