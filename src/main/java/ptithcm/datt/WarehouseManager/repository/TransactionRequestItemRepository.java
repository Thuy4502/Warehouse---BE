package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;
import ptithcm.datt.WarehouseManager.model.TransactionRequestItem;

import java.util.List;

@Repository
public interface TransactionRequestItemRepository extends JpaRepository<TransactionRequestItem, Long> {


}

