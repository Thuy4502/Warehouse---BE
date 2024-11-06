package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Type;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    Optional<Type> findByTypeName(String typeName);
}
