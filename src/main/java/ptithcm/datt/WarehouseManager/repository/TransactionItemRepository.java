package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.TransactionItem;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {
}
