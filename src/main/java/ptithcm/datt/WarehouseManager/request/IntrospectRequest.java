package ptithcm.datt.WarehouseManager.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class IntrospectRequest {
    private String token;
}
