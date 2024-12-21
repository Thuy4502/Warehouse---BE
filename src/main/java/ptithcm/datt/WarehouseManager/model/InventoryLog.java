package ptithcm.datt.WarehouseManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "inventory_log")
public class InventoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryLogId;

    private LocalDate logDate; // Ngày kỳ thống kê
    private int startQuantity; // Tồn đầu kỳ
    private int importQuantity; // Nhập trong kỳ
    private int exportQuantity; // Xuất trong kỳ
    private int endQuantity; // Tồn cuối kỳ

    private Double startPrice; // Giá tồn đầu kỳ
    private Double exportPrice; // Giá trung bình cuối kỳ
    private Double importPrice; // Giá trung bình nhập trong kỳ
    private Double endPrice; // Giá trị tồn cuối kỳ = endQuantity * averagePrice

    private Double startAmount; // Giá trị tồn đầu kỳ
    private Double exportAmount; // Giá trị xuất trong kỳ
    private Double importAmount; // Giá trị nhập trong kỳ
    private Double endAmount; // Giá trị tồn cuối kỳ = endQuantity * endPrice

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne
    @JoinColumn(name = "transaction_item_id", referencedColumnName = "transaction_item_id")
    private TransactionItem transactionItem; // Quan hệ với TransactionItem
}
