package ptithcm.datt.WarehouseManager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffRequest {
    private String username;
    private String roleName;
    private String staffName;
    private String phoneNumber;
    private String email;
    private String picture;
    private String address;
    private Date dob;


}
