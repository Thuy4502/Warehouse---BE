package ptithcm.datt.WarehouseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.InventoryLog;
import ptithcm.datt.WarehouseManager.model.TransactionItem;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;
import ptithcm.datt.WarehouseManager.repository.BookRepository;
import ptithcm.datt.WarehouseManager.repository.InventoryLogRepository;
import ptithcm.datt.WarehouseManager.repository.TransactionItemRepository;
import ptithcm.datt.WarehouseManager.repository.TransactionRepository;
import ptithcm.datt.WarehouseManager.response.InventoryLogResponse;
import ptithcm.datt.WarehouseManager.response.InventoryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryLogService {
    @Autowired
    InventoryLogRepository inventoryLogRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TransactionItemRepository transactionItemRepository;

    public List<InventoryLogResponse> getAllInventoryLogs(String type) {
        List<Object[]> results = inventoryLogRepository.findInventoryLogsByType(type);

        return results.stream().map(result -> {
            LocalDate createDate = ((java.sql.Date) result[0]).toLocalDate();
            String transactionCode = (String) result[1];
            Long inventoryLogId = ((Number) result[2]).longValue();
            Long transactionItemId = ((Number) result[3]).longValue();
            Long bookId = ((Number) result[4]).longValue();
            String bookName = (String) result[5];

            int startQuantity = result[6] != null ? ((Number) result[6]).intValue() : 0;
            Double startPrice = result[7] != null ? (Double) result[7] : 0.0;
            Double startAmount = result[8] != null ? (Double) result[8] : 0.0;

            int importQuantity = result[9] != null ? ((Number) result[9]).intValue() : 0;
            Double importPrice = result[10] != null ? (Double) result[10] : 0.0;
            Double importAmount = result[11] != null ? (Double) result[11] : 0.0;

            int exportQuantity = result[12] != null ? ((Number) result[12]).intValue() : 0;
            Double exportPrice = result[13] != null ? (Double) result[13] : 0.0;
            Double exportAmount = result[14] != null ? (Double) result[14] : 0.0;

            int endQuantity = result[15] != null ? ((Number) result[15]).intValue() : 0;
            Double endAmount = result[16] != null ? (Double) result[16] : 0.0;

            Book book = bookRepository.findById(bookId).orElse(null);
            TransactionItem transactionItem = transactionItemRepository.findById(transactionItemId).orElse(null);

            return new InventoryLogResponse(
                    inventoryLogId,
                    createDate,
                    startQuantity,
                    importQuantity,
                    exportQuantity,
                    endQuantity,
                    bookId,
                    bookName,
                    startPrice,
                    exportPrice,
                    importPrice,
                    endQuantity * exportPrice,
                    startAmount,
                    exportAmount,
                    importAmount,
                    endAmount,
                    transactionCode,
                    book,
                    transactionItem
            );
        }).collect(Collectors.toList());
    }

    public List<InventoryResponse> getInventorySummary(String startDate, String endDate) {
        List<Object[]> results = bookRepository.getInventorySummary(startDate, endDate);

        return results.stream()
                .map(row -> new InventoryResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        ((Number) row[3]).intValue(),
                        ((Number) row[4]).intValue(),
                        ((Number) row[5]).intValue(),
                        ((Number) row[6]).intValue(),
                        (row[7] != null) ? ((Number) row[7]).doubleValue() : 0.0,
                        (row[8] != null) ? ((Number) row[8]).doubleValue() : 0.0
                ))
                .collect(Collectors.toList());
    }


}
