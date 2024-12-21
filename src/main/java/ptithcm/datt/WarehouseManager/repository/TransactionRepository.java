package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.Transaction;

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

    @Query(value = "SELECT " +
            "   MONTH(t.create_at) AS month, " +
            "   SUM(CASE WHEN t.type_id = 1 THEN t.total_value ELSE 0 END) AS total_import_value, " +
            "   SUM(CASE WHEN t.type_id = 2 THEN t.total_value ELSE 0 END) AS total_export_value " +
            "FROM warehouse.transaction t " +
            "WHERE YEAR(t.create_at) = :year " +
            "GROUP BY MONTH(t.create_at) " +
            "ORDER BY MONTH(t.create_at)",
            nativeQuery = true)
    List<Object[]> findMonthlyRevenueByYear(int year);



    @Query(value = "SELECT " +
            "(SELECT COUNT(DISTINCT category_id) FROM warehouse.category) AS totalCategories, " +
            "(SELECT COUNT(*) FROM warehouse.book) AS totalBooks, " +
            "(SELECT COALESCE(SUM(t.total_value), 0) " +
            "FROM warehouse.transaction t " +
            "WHERE t.type_id = 1 " +
            "AND EXTRACT(MONTH FROM t.create_at) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM t.create_at) = EXTRACT(YEAR FROM CURRENT_DATE)) AS totalImportValue, " +
            "(SELECT COALESCE(SUM(t.total_value), 0) " +
            "FROM warehouse.transaction t " +
            "WHERE t.type_id = 2 " +
            "AND EXTRACT(MONTH FROM t.create_at) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM t.create_at) = EXTRACT(YEAR FROM CURRENT_DATE)) AS totalExportValue",
            nativeQuery = true)
    List<Object[]> getBookStatistics();

}
