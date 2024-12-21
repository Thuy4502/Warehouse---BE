package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.response.InventoryResponse;

import java.util.List;
import java.util.Map;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = """
            SELECT b.book_name AS bookName, b.image AS image, b.quantity AS quantity, SUM(ti.actual_quantity) AS totalSold,
                   SUM(ti.price * ti.actual_quantity) AS totalRevenue
            FROM warehouse.book b
            JOIN warehouse.transaction_item ti ON ti.book_id = b.book_id
            JOIN warehouse.transaction t ON t.transaction_id = ti.transaction_id
            WHERE t.type_id = 2
            GROUP BY b.book_name, b.image, b.quantity
            ORDER BY totalRevenue DESC
            LIMIT 3
            """, nativeQuery = true)
    List<Object[]> findTopSellingBooks();

    @Query(value = """
        SELECT 
            b.book_id AS bookId,
            b.isbn AS isbn,  b.book_name AS bookName,
            SUM(CASE WHEN t.type_id = 1 OR t.type_id = 2 THEN il.start_quantity ELSE 0 END) AS startQuantity,
            SUM(CASE WHEN t.type_id = 1 THEN il.import_quantity ELSE 0 END) AS importQuantity,
            SUM(CASE WHEN t.type_id = 2 THEN il.export_quantity ELSE 0 END) AS exportQuantity,
            SUM(CASE WHEN t.type_id = 1 OR t.type_id = 2 THEN il.end_quantity ELSE 0 END) AS endQuantity,
            CASE 
                WHEN SUM(CASE WHEN t.type_id = 1 THEN il.import_quantity ELSE 0 END) = 0 
                THEN SUM(CASE WHEN t.type_id = 1 OR t.type_id = 2 THEN il.start_price ELSE 0 END)
                ELSE 
                    SUM(CASE WHEN t.type_id = 1 THEN il.import_quantity * il.import_price ELSE 0 END) /
                    NULLIF(SUM(CASE WHEN t.type_id = 1 THEN il.import_quantity ELSE 0 END), 0) 
            END AS endPrice,
            (SUM(CASE WHEN t.type_id = 1 OR t.type_id = 2 THEN il.end_quantity ELSE 0 END) *
            CASE 
                WHEN SUM(CASE WHEN t.type_id = 1 THEN il.import_quantity ELSE 0 END) = 0 
                THEN SUM(CASE WHEN t.type_id = 1 OR t.type_id = 2 THEN il.start_price ELSE 0 END)
                ELSE 
                    SUM(CASE WHEN t.type_id = 1 THEN il.import_quantity * il.import_price ELSE 0 END) /
                    NULLIF(SUM(CASE WHEN t.type_id = 1 THEN il.import_quantity ELSE 0 END), 0)
            END) AS endAmount
        FROM 
            warehouse.inventory_log il
        JOIN 
            warehouse.transaction_item ti ON il.transaction_item_id = ti.transaction_item_id
        JOIN 
            warehouse.book b ON ti.book_id = b.book_id
        JOIN 
            warehouse.transaction t ON ti.transaction_id = t.transaction_id
        WHERE 
            t.create_at BETWEEN :startDate AND :endDate
        GROUP BY 
            b.isbn, b.book_id
        ORDER BY 
            b.book_id
        """, nativeQuery = true)
    List<Object[]> getInventorySummary(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

}
