package ptithcm.datt.WarehouseManager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "transaction_request")
public class TransactionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transaction_request_id")
    private Long transactionRequestId;

    private String position;

    @Column(name="create_by")
    private String createBy;

    @Column(name="phone_number")
    private String phoneNumber;

    private String department;
    private String reason;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name="transaction_request_code")
    private String transactionRequestCode;

    @Column(name="total_value")
    private Double totalValue;

    private String status;

    @ManyToOne
    @JoinColumn(name="staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name="update_by")
    private Staff staffUpdate;

    @JsonIgnore
    @OneToMany(mappedBy = "transactionRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;


    @OneToMany(mappedBy = "transactionRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TransactionRequestItem> transactionRequestItems;

    @ManyToOne
    @JoinColumn(name="type_id")
    private Type type;









}
