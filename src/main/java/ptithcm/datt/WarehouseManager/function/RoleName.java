package ptithcm.datt.WarehouseManager.function;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleName {
    ADMIN("Admin"),
    WAREHOUSE("Warehousekeeper"),
    SALE("Salesperson"),
    STAFF("Staff");
    private final String roleName;
}
