package ptithcm.datt.WarehouseManager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    private Long accountId;
    private String email;
    private String username;
    private String password;
    private String status;
    @Column(name="create_at")
    private LocalDateTime createAt;

    @Column(name="update_at")
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    private Staff staff;

}
