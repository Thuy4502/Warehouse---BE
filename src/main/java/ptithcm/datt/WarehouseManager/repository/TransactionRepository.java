package ptithcm.datt.WarehouseManager.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.dto.response.TransactionHistoryResponse;
import ptithcm.datt.WarehouseManager.model.Transaction;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT tr FROM Transaction tr JOIN tr.type t WHERE t.typeName = :typeName")
    List<Transaction> findByTypeName(@Param("typeName") String typeName);

    @Query(value = "SELECT DATE(t.create_at) AS createDate, t.transaction_code AS transactionCode, ti.book_id AS bookId, " +
            "b.book_name AS bookName, ti.price, ti.start_qty AS startQty, ti.actual_quantity AS actualQuantity, t.type_id " +
            "FROM warehouse.transaction t " +
            "LEFT JOIN warehouse.transaction_item ti ON t.transaction_id = ti.transaction_id " +
            "LEFT JOIN warehouse.book b ON ti.book_id = b.book_id " +
            "JOIN warehouse.type ty ON t.type_id = ty.type_id " +
            "WHERE (:type IS NULL OR :type = '' OR ty.type_name = :type)",
            nativeQuery = true)
    List<Object[]> transactionHistory(@Param("type") String type);



}
