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
@Table(name="transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transaction_id")
    private Long transactionId;

    @Column(name="business_partner")
    private String businessPartner;

    private String address;
    @Column(name="phone_number")
    private String phone_number;

    @Column(name="total_value")
    private Double totalValue;

    @Column(name="tax_id")
    private String taxId;

    @Column(name="create_at")
    private LocalDateTime createAt;

    @Column(name="update_at")
    private LocalDateTime updateAt;

    @Column(name="transaction_code")
    private String transactionCode;

    @Column(name="deliver_person")
    private String deliveryPerson;

    @ManyToOne
    @JoinColumn(name="staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name="update_by")
    private Staff staffUpdate;


    @ManyToOne
    @JoinColumn(name="transaction_request_id")
    private TransactionRequest transactionRequest;

    @ManyToOne
    @JoinColumn(name="bill_id")
    private Bill bill;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TransactionItem> transactionItems;

    @ManyToOne
    @JoinColumn(name="type_id")
    private Type type;

}
