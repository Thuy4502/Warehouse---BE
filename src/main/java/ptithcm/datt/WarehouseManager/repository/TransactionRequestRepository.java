package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, Long> {
    @Query("SELECT tr FROM TransactionRequest tr JOIN tr.type t WHERE t.typeName = :type")
    List<TransactionRequest> findByType(@Param("type") String type);

}
