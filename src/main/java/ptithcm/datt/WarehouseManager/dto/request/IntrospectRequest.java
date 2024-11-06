package ptithcm.datt.WarehouseManager.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class IntrospectRequest {
    private String token;
}
