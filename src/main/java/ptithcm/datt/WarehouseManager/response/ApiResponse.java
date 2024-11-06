package ptithcm.datt.WarehouseManager.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
public class ApiResponse {
    private String message;
    private HttpStatus status;
    private int code;

}
