package ptithcm.datt.WarehouseManager.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Data
@Getter
@Setter
public class EntityResponse<T> {
    private T data;
    private HttpStatus status;
    private String message;
    private int code;


}
