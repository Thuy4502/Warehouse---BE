package ptithcm.datt.WarehouseManager.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private boolean authenticated;
    private String token;
}
