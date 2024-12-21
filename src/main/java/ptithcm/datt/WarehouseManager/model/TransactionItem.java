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
@Table(name = "transaction_item")
public class TransactionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_item_id")
    private Long transactionItemId;

    @Column(name = "request_quantity")
    private int requestQuantity;

    @Column(name = "actual_quantity")
    private int actualQuantity;

    private Double price;
    private String note;

    @Column(name = "start_qty")
    private int startQty;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @JsonIgnore
    @OneToOne(mappedBy = "transactionItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private InventoryLog inventoryLog; // Quan hệ với InventoryLog
}
