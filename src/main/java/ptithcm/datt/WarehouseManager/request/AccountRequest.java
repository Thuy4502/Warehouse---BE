package ptithcm.datt.WarehouseManager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountRequest {
    private String email;
    private String username;
    private String password;
    private Long roleId;
}
