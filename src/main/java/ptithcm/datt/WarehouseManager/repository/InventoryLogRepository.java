package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.InventoryLog;
import ptithcm.datt.WarehouseManager.model.Role;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.response.InventoryLogResponse;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {

    @Query("SELECT i FROM InventoryLog i WHERE i.transactionItem.id = :transactionItemId")
    Optional<InventoryLog> findByTransactionItemId(@Param("transactionItemId") Long transactionItemId);


    @Query(value = """
    SELECT 
        DATE(t.create_at) AS create_date, 
        t.transaction_code,
        il.inventory_log_id, 
        ti.transaction_item_id, 
        b.book_id, 
        b.book_name, 
        il.start_quantity,
        il.start_price, 
        il.start_amount, 
        il.import_quantity, 
        il.import_price, 
        il.import_amount, 
        il.export_quantity,
        il.export_price,
        il.export_amount,
        il.end_quantity, 
        il.end_amount 
    FROM 
        warehouse.inventory_log il
    JOIN 
        warehouse.transaction_item ti ON il.transaction_item_id = ti.transaction_item_id
    JOIN 
        warehouse.book b ON ti.book_id = b.book_id
    JOIN 
        warehouse.transaction t ON ti.transaction_id = t.transaction_id
    JOIN 
        warehouse.type ty ON ty.type_id = t.type_id
    WHERE (:type = 'ALL' OR ty.type_name = :type)
    ORDER BY 
        il.log_date DESC
    """, nativeQuery = true)
    List<Object[]> findInventoryLogsByType(@Param("type") String type); // Make sure to match the parameter name in @Param with the SQL query.


}
