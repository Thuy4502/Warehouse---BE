package ptithcm.datt.WarehouseManager.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="transaction_request_item")
public class TransactionRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int requestQuantity;
    private int acceptQuantity;
    private Double price;
    private String note;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "transaction_request_id")
    private TransactionRequest transactionRequest;
}
